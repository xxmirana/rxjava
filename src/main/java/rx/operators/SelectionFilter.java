package rx.transformers;

import rx.ReactiveStream;
import rx.DataSubscriber;
import java.util.function.Predicate;

/**
 * Filters elements based on predicate
 */
public final class SelectionFilter<T> extends ReactiveStream<T> {
    private final ReactiveStream<T> upstream;
    private final Predicate<T> selector;

    public SelectionFilter(ReactiveStream<T> upstream, Predicate<T> selector) {
        super(subscriber -> 
            upstream.subscribe(new FilteringAdapter<>(subscriber, selector))
        );
        this.upstream = upstream;
        this.selector = selector;
    }

    static final class FilteringAdapter<T> implements DataSubscriber<T> {
        private final DataSubscriber<T> downstream;
        private final Predicate<T> selector;

        FilteringAdapter(DataSubscriber<T> downstream, Predicate<T> selector) {
            this.downstream = downstream;
            this.selector = selector;
        }

        @Override
        public void receiveElement(T element) {
            try {
                if (selector.test(element)) {
                    downstream.receiveElement(element);
                }
            } catch (Exception e) {
                downstream.handleError(e);
            }
        }

        @Override
        public void handleError(Throwable error) {
            downstream.handleError(error);
        }

        @Override
        public void onSequenceComplete() {
            downstream.onSequenceComplete();
        }
    }
}