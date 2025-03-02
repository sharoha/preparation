import java.util.concurrent.locks.*;

public class Print123 {

    static class NumberPrinter {
        private int startNumber = 0;

        public synchronized void printNumber(int id) {
            for (int i = 0; i < 10; i++) {
                while (startNumber != id) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(id + 1);
                startNumber = (startNumber + 1) % 3;
                notifyAll();
            }
        }
    }

    static class NumberPrinterLock {
        private int number = 0;
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition condition = lock.newCondition();

        public void printNumber(int id) {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (number != id) {
                        condition.await();
                    }
                    System.out.println(id + 1);
                    number = (number + 1) % 3;
                    condition.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        var printer = new NumberPrinter();
        var lockPrinter = new NumberPrinterLock();
        Thread t1 = new Thread(() -> lockPrinter.printNumber(0));
        Thread t2 = new Thread(() -> lockPrinter.printNumber(1));
        Thread t3 = new Thread(() -> lockPrinter.printNumber(2));
        t1.start();
        t2.start();
        t3.start();

    }
}
