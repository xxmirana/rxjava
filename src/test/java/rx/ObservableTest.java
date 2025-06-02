package rx;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObservableTest {
    @Test
    public void testCreateAndSubscribe() {
        Observable.create((Observer<Integer> observer) -> {
            observer.onNext(1);
            observer.onNext(2);
            observer.onNext(3);
            observer.onComplete();
        }).subscribe(new Observer<Integer>() {
            int count = 0;

            @Override
            public void onNext(Integer item) {
                count++;
                System.out.println("Received: " + item);
            }

            @Override
            public void onError(Throwable t) {
                fail("Unexpected error");
            }

            @Override
            public void onComplete() {
                assertEquals(3, count);
                System.out.println("Completed");
            }
        });
    }
}