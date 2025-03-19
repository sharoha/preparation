## How to estimate for Number of request a Single instance service can handle?

1. Based on [here](https://www.baeldung.com/java-web-thread-pool-config) Tomcat on Spring supports 25-200 worker threads as defaults.  
2. We need to understand, what is processing time of this service for individual request. Let's assume it to be `50ms`  
3. Based on this,
```
Maximum request per second = concurrent threads/ avg request time
= 200/0.05 = 4000 requests/second
```
