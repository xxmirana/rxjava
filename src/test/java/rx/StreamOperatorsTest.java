package rx;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class StreamOperatorsTest {
    @Test
    public void shouldTransformAndFilterElements() {
        List<Integer> results = new ArrayList<>();
        
        ReactiveStream.build((DataSubscriber<Integer> subscriber) -> {
            for (int i = 0; i < 10; i++) {
                subscriber.receiveElement(i);
            }
            subscriber.onSequenceComplete();
        })
        .select(x -> x % 2 == 0)
        .transform(x -> x * 3)
        .subscribe(new DataSubscriber<Integer>() {
            @Override
            public void receiveElement(Integer element) {
                results.add(element);
            }

            @Override
            public void handleError(Throwable error) {
                fail("Unexpected error");
            }

            @Override
            public void onSequenceComplete() {
                assertEquals(List.of(0, 6, 12, 18, 24), results);
            }
        });
    }
}