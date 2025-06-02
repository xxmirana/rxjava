package rx.operators;

import rx.Observable;
import rx.Observer;
import rx.Disposable;
import java.util.function.Function;

public class FlatMap<T, R> extends Observable<R> {
    private final Observable<T> source;
    private final Function<T, Observable<R>> mapper;

    public FlatMap(Observable<T> source, Function<T, Observable<R>> mapper) {
        super(observer -> {
            FlatMapObserver<T, R> parent = new FlatMapObserver<>(observer, mapper);
            source.subscribe(parent);
        });
        this.source = source;
        this.mapper = mapper;
    }

    static final class FlatMapObserver<T, R> implements Observer<T>, Disposable {
        final Observer<R> downstream;
        final Function<T, Observable<R>> mapper;
        volatile boolean disposed;

        FlatMapObserver(Observer<R> downstream, Function<T, Observable<R>> mapper) {
            this.downstream = downstream;
            this.mapper = mapper;
        }

        @Override
        public void onNext(T item) {
            if (disposed) return;
            try {
                Observable<R> observable = mapper.apply(item);
                observable.subscribe(new Observer<R>() {
                    @Override
                    public void onNext(R item) {
                        if (!disposed) {
                            downstream.onNext(item);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (!disposed) {
                            downstream.onError(t);
                        }
                    }

                    @Override
                    public void onComplete() {
                        // Ничего не делаем, ждем завершения основного потока
                    }
                });
            } catch (Exception e) {
                downstream.onError(e);
            }
        }

        @Override
        public void onError(Throwable t) {
            if (!disposed) {
                downstream.onError(t);
            }
        }

        @Override
        public void onComplete() {
            if (!disposed) {
                downstream.onComplete();
            }
        }

        @Override
        public void dispose() {
            disposed = true;
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }
    }
}