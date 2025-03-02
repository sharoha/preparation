
// Design a webcrawler of Depth n
//

import java.net.MalformedURLException;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.io.IOException;
import java.util.concurrent.*;

class WebCrawler {
    private final int maxDepth;
    private final ExecutorService executor;
    private final Set<String> visitedUrls;
    private final UrlQueue urlQueue;

    public WebCrawler(int maxDepth) {
        this.maxDepth = maxDepth;
        this.executor = Executors.newFixedThreadPool(10);
        this.visitedUrls = ConcurrentHashMap.newKeySet();
        this.urlQueue = UrlQueue.getInstance();
    }

    public void startCrawling(String startUrl) {
        urlQueue.addUrl(startUrl, 0);
        while (!urlQueue.isEmpty()) {
            UrlDepthPair next = urlQueue.getNextUrl();
            System.out.println("Trying with: " + next);
            if (next.depth() <= maxDepth) {
                executor.execute(new CrawlerTask(next.url(), next.depth(), this));
            }
        }
    }

    public void stopCrawling() {
        executor.shutdown();
    }

    public boolean isVisited(String url) {
        return visitedUrls.contains(url);
    }

    public void markVisited(String url) {
        visitedUrls.add(url);
    }

    public UrlQueue getUrlQueue() {
        return urlQueue;
    }
}

record UrlDepthPair(String url, int depth) {};

class UrlQueue {
    private static final UrlQueue instance = new UrlQueue();
    private final BlockingQueue<UrlDepthPair> queue = new LinkedBlockingQueue<>();

    private UrlQueue() {}

    public static UrlQueue getInstance() {
        return instance;
    }

    public void addUrl(String url, int depth) {
        queue.add(new UrlDepthPair(url, depth));
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public UrlDepthPair getNextUrl() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }
}

class CrawlerTask implements Runnable {
    private final String url;
    private final int depth;
    private final WebCrawler crawler;

    public CrawlerTask(String url, int depth, WebCrawler crawler) {
        this.url = url;
        this.depth = depth;
        this.crawler = crawler;
    }


    @Override
    public void run() {
        if (crawler.isVisited(url)) return;
        crawler.markVisited(url);
        System.out.println("Crawling: " + url + " at depth: " + depth);

        String html = UrlFetcher.fetch(url);
        List<String> links = UrlParser.parseLinks(html);
        System.out.println("Found the following link: " + links);
        for (String link: links) {
            if (UrlFilter.isValid(link)) {
                crawler.getUrlQueue().addUrl(link, depth + 1);
            }
        }
    }
}


class UrlFetcher {

    public static String fetch(String url) {
        try {
            URL website = new URL(url);
            Scanner scanner = new Scanner(website.openStream(), StandardCharsets.UTF_8);
            StringBuilder content = new StringBuilder();
            while (scanner.hasNext()) {
                content.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
            return content.toString();
        } catch (IOException e) {
            return "";
        }
    }
}


class UrlParser {
    private static final Pattern LINK_PATTERN = Pattern.compile("href=[\"'](https?://.*?)[\"']");

    public static List<String> parseLinks(String html) {
        List<String> links = new ArrayList<>();
        Matcher matcher = LINK_PATTERN.matcher(html);
        while (matcher.find()) {
            links.add(matcher.group(1));
        }
        return links;
    }
}


class UrlFilter {
    public static boolean isValid(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}


public class WebCrawlerDepthN {

    public static void main(String[] args) {
        var crawler = new WebCrawler(2);
        crawler.startCrawling("https://en.wikipedia.org/wiki/Main_Page");
//        crawler.stopCrawling();
    }
}


