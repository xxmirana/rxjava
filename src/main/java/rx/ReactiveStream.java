package rx;

import rx.transformers.*;
import rx.concurrency.TaskScheduler;
import java.util.function.*;

/**
 * Represents a stream of data that can be observed
 */
public final class ReactiveStream<T> {
    private final StreamSource<T> source;

    private ReactiveStream(StreamSource<T> source) {
        this.source = source;
    }

    /**
     * Creates new stream from source
     */
    public static <T> ReactiveStream<T> build(StreamSource<T> source) {
        return new ReactiveStream<>(source);
    }

    /**
     * Subscribes to stream events
     */
    public Subscription subscribe(DataSubscriber<T> subscriber) {
        source.emitEvents(subscriber);
        return new BasicSubscription();
    }

    /**
     * Transforms each element using mapper function
     */
    public <R> ReactiveStream<R> transform(Function<T, R> mapper) {
        return new ElementTransformer<>(this, mapper);
    }

    /**
     * Filters elements using predicate
     */
    public ReactiveStream<T> select(Predicate<T> predicate) {
        return new SelectionFilter<>(this, predicate);
    }

    /**
     * Flattens nested streams
     */
    public <R> ReactiveStream<R> flatten(Function<T, ReactiveStream<R>> mapper) {
        return new StreamFlattener<>(this, mapper);
    }

    /**
     * Specifies execution context for subscription
     */
    public ReactiveStream<T> withSubscriptionContext(TaskScheduler scheduler) {
        return ReactiveStream.build(subscriber -> 
            scheduler.scheduleTask(() -> this.subscribe(subscriber))
        );
    }

    /**
     * Specifies execution context for events
     */
    public ReactiveStream<T> withEventContext(TaskScheduler scheduler) {
        return ReactiveStream.build(subscriber -> 
            this.subscribe(new DataSubscriber<T>() {
                @Override
                public void receiveElement(T element) {
                    scheduler.scheduleTask(() -> subscriber.receiveElement(element));
                }

                @Override
                public void handleError(Throwable error) {
                    scheduler.scheduleTask(() -> subscriber.handleError(error));
                }

                @Override
                public void onSequenceComplete() {
                    scheduler.scheduleTask(subscriber::onSequenceComplete);
                }
            })
        );
    }

    public interface StreamSource<T> {
        void emitEvents(DataSubscriber<T> subscriber);
    }
}