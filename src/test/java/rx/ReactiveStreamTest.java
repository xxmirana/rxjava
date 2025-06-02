package rx;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ReactiveStreamTest {
    @Test
    public void shouldProcessSequenceOfElements() {
        AtomicInteger counter = new AtomicInteger();
        
        ReactiveStream.build((DataSubscriber<Integer> subscriber) -> {
            subscriber.receiveElement(10);
            subscriber.receiveElement(20);
            subscriber.receiveElement(30);
            subscriber.onSequenceComplete();
        })
        .subscribe(new DataSubscriber<Integer>() {
            @Override
            public void receiveElement(Integer element) {
                counter.addAndGet(element);
            }

            @Override
            public void handleError(Throwable error) {
                fail("Unexpected error");
            }

            @Override
            public void onSequenceComplete() {
                assertEquals(60, counter.get());
            }
        });
    }
}