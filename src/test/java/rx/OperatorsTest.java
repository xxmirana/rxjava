package rx;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class OperatorsTest {
    @Test
    public void testMapAndFilter() {
        List<Integer> received = new ArrayList<>();

        Observable.create((Observer<Integer> observer) -> {
                    for (int i = 0; i < 10; i++) {
                        observer.onNext(i);
                    }
                    observer.onComplete();
                })
                .filter(x -> x % 2 == 0)
                .map(x -> x * 2)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onNext(Integer item) {
                        received.add(item);
                        System.out.println("Received: " + item);
                    }

                    @Override
                    public void onError(Throwable t) {
                        fail("Unexpected error");
                    }

                    @Override
                    public void onComplete() {
                        assertEquals(List.of(0, 4, 8, 12, 16), received);
                        System.out.println("Completed");
                    }
                });
    }
}