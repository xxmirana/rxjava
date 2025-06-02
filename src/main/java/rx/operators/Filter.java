package rx.operators;

import rx.Observable;
import rx.Observer;
import java.util.function.Predicate;

public class Filter<T> extends Observable<T> {
    private final Observable<T> source;
    private final Predicate<T> predicate;

    public Filter(Observable<T> source, Predicate<T> predicate) {
        super(observer -> source.subscribe(new FilterObserver<>(observer, predicate)));
        this.source = source;
        this.predicate = predicate;
    }

    static final class FilterObserver<T> implements Observer<T> {
        final Observer<T> downstream;
        final Predicate<T> predicate;

        FilterObserver(Observer<T> downstream, Predicate<T> predicate) {
            this.downstream = downstream;
            this.predicate = predicate;
        }

        @Override
        public void onNext(T item) {
            try {
                if (predicate.test(item)) {
                    downstream.onNext(item);
                }
            } catch (Exception e) {
                downstream.onError(e);
            }
        }

        @Override
        public void onError(Throwable t) {
            downstream.onError(t);
        }

        @Override
        public void onComplete() {
            downstream.onComplete();
        }
    }
}