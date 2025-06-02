package rx;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SchedulerTest {
    @Test
    public void testSubscribeOnObserveOn() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Observable.create(observer -> {
                    assertFalse(Thread.currentThread().getName().startsWith("main"));
                    observer.onNext(1);
                    observer.onComplete();
                })
                .subscribeOn(Scheduler.io())
                .observeOn(Scheduler.computation())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onNext(Integer item) {
                        assertTrue(Thread.currentThread().getName().startsWith("pool"));
                        System.out.println("Received on thread: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable t) {
                        fail("Unexpected error");
                    }

                    @Override
                    public void onComplete() {
                        assertTrue(Thread.currentThread().getName().startsWith("pool"));
                        latch.countDown();
                    }
                });

        assertTrue(latch.await(1, TimeUnit.SECONDS));
    }
}