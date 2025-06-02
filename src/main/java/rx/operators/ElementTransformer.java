package rx.transformers;

import rx.ReactiveStream;
import rx.DataSubscriber;
import java.util.function.Function;

/**
 * Transforms stream elements using mapping function
 */
public final class ElementTransformer<T, R> extends ReactiveStream<R> {
    private final ReactiveStream<T> upstream;
    private final Function<T, R> transformation;

    public ElementTransformer(ReactiveStream<T> upstream, Function<T, R> transformation) {
        super(subscriber -> 
            upstream.subscribe(new TransformationAdapter<>(subscriber, transformation))
        );
        this.upstream = upstream;
        this.transformation = transformation;
    }

    static final class TransformationAdapter<T, R> implements DataSubscriber<T> {
        private final DataSubscriber<R> downstream;
        private final Function<T, R> transformation;

        TransformationAdapter(DataSubscriber<R> downstream, Function<T, R> transformation) {
            this.downstream = downstream;
            this.transformation = transformation;
        }

        @Override
        public void receiveElement(T element) {
            try {
                downstream.receiveElement(transformation.apply(element));
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