package v0_1;

import com.github.apachefoundation.jerrymouse2.util.LimitLatch;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @Author: xiantang
 * @Date: 2019/9/10 14:20
 */
public class TestLimitLatch {
    private static final long THREAD_WAIT_TIME = 60000;

    @Test
    public void testNoThreads() {
        LimitLatch latch = new LimitLatch(0);
        Assert.assertFalse("No threads should be waiting", latch.hasQueuedThreads());
    }

    @Test
    public void testOneThreadNoWait() throws Exception {
        LimitLatch latch = new LimitLatch(1);
        Object lock = new Object();
        checkWaitingThreadCount(latch, 0);
        TestThread testThread = new TestThread(latch, lock);
        testThread.start();
        if (!waitForThreadToStart(testThread)) {
            Assert.fail("Test thread did not start");
        }
        checkWaitingThreadCount(latch, 0);
        if (!waitForThreadToStop(testThread, lock)) {
            Assert.fail("Test thread did not stop");
        }
        checkWaitingThreadCount(latch, 0);
    }


    @Test
    public void testTenWait() throws Exception {
        LimitLatch latch = new LimitLatch(10);
        Object lock = new Object();
        checkWaitingThreadCount(latch, 0);
        new TestThread(latch, lock) {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    try {
                        latch.countUpOrAwait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        TimeUnit.MILLISECONDS.sleep(100);
        new TestThread(latch, lock) {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    latch.countDown();
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        TimeUnit.MILLISECONDS.sleep(1000);
    }

    private class TestThread extends Thread {
        private final Object lock;
        private final LimitLatch latch;
        private volatile int stage = 0;
        public TestThread(LimitLatch latch, Object lock) {
            this.latch = latch;
            this.lock = lock;
        }
        public int getStage() {
            return stage;
        }

        @Override
        public void run() {
            stage = 1;
            try {
                latch.countUpOrAwait();
                stage = 2;
                if (lock != null) {
                    synchronized (lock) {
                        lock.wait();
                    }
                }
                latch.countDown();
                stage = 3;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }

    private boolean waitForThreadToStart(TestThread t) throws InterruptedException {
        long wait = 0;
        while (t.getStage() == 0 && wait < THREAD_WAIT_TIME) {
            Thread.sleep(100);
            wait += 100;
        }
        return t.getStage() > 0;
    }



    private boolean waitForThreadToStop(TestThread t, Object lock) throws InterruptedException {
        long wait = 0;
        while (t.getStage() < 3 && wait < THREAD_WAIT_TIME) {
            Thread.sleep(100);
            wait += 100;
            synchronized (lock) {
                lock.notifyAll();
            }
        }
        return t.getStage() == 3;
    }

    private void checkWaitingThreadCount(LimitLatch latch, int target) throws InterruptedException {
        long wait = 0;
        while (latch.getQueuedThreads().size() != target && wait < THREAD_WAIT_TIME) {
            Thread.sleep(100);
            wait += 100;
        }
        Assert.assertEquals(target,  latch.getQueuedThreads().size());
    }


}
