package rx.operators;

import rx.Observable;
import rx.Observer;
import java.util.function.Function;

public class Map<T, R> extends Observable<R> {
    private final Observable<T> source;
    private final Function<T, R> mapper;

    public Map(Observable<T> source, Function<T, R> mapper) {
        super(observer -> source.subscribe(new MapObserver<>(observer, mapper)));
        this.source = source;
        this.mapper = mapper;
    }

    static final class MapObserver<T, R> implements Observer<T> {
        final Observer<R> downstream;
        final Function<T, R> mapper;

        MapObserver(Observer<R> downstream, Function<T, R> mapper) {
            this.downstream = downstream;
            this.mapper = mapper;
        }

        @Override
        public void onNext(T item) {
            try {
                downstream.onNext(mapper.apply(item));
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