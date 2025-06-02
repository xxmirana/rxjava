package rx.transformers;

import rx.ReactiveStream;
import rx.DataSubscriber;
import rx.Subscription;
import java.util.function.Function;

/**
 * Transforms and flattens nested streams
 */
public final class StreamFlattener<T, R> extends ReactiveStream<R> {
    private final ReactiveStream<T> upstream;
    private final Function<T, ReactiveStream<R>> mapper;

    public StreamFlattener(ReactiveStream<T> upstream, Function<T, ReactiveStream<R>> mapper) {
        super(subscriber -> {
            FlatteningAdapter<T, R> adapter = new FlatteningAdapter<>(subscriber, mapper);
            upstream.subscribe(adapter);
        });
        this.upstream = upstream;
        this.mapper = mapper;
    }

    static final class FlatteningAdapter<T, R> implements DataSubscriber<T>, Subscription {
        private final DataSubscriber<R> downstream;
        private final Function<T, ReactiveStream<R>> mapper;
        private volatile boolean cancelled;

        FlatteningAdapter(DataSubscriber<R> downstream, Function<T, ReactiveStream<R>> mapper) {
            this.downstream = downstream;
            this.mapper = mapper;
        }

        @Override
        public void receiveElement(T element) {
            if (cancelled) return;
            try {
                ReactiveStream<R> innerStream = mapper.apply(element);
                innerStream.subscribe(new DataSubscriber<R>() {
                    @Override
                    public void receiveElement(R element) {
                        if (!cancelled) {
                            downstream.receiveElement(element);
                        }
                    }

                    @Override
                    public void handleError(Throwable error) {
                        if (!cancelled) {
                            downstream.handleError(error);
                        }
                    }

                    @Override
                    public void onSequenceComplete() {
                        // Do nothing for inner completion
                    }
                });
            } catch (Exception e) {
                downstream.handleError(e);
            }
        }

        @Override
        public void handleError(Throwable error) {
            if (!cancelled) {
                downstream.handleError(error);
            }
        }

        @Override
        public void onSequenceComplete() {
            if (!cancelled) {
                downstream.onSequenceComplete();
            }
        }

        @Override
        public void cancel() {
            cancelled = true;
        }

        @Override
        public boolean isActive() {
            return !cancelled;
        }
    }
}