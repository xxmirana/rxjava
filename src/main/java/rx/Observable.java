package rx;

import rx.operators.*;
import rx.schedulers.Scheduler;
import java.util.function.Function;
import java.util.function.Predicate;

public class Observable<T> {
    private final OnSubscribe<T> onSubscribe;

    private Observable(OnSubscribe<T> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    public static <T> Observable<T> create(OnSubscribe<T> onSubscribe) {
        return new Observable<>(onSubscribe);
    }

    public Disposable subscribe(Observer<T> observer) {
        onSubscribe.call(observer);
        return () -> {}; // Простая реализация Disposable
    }

    public interface OnSubscribe<T> {
        void call(Observer<T> observer);
    }

    // Операторы
    public <R> Observable<R> map(Function<T, R> mapper) {
        return new Map<>(this, mapper);
    }

    public Observable<T> filter(Predicate<T> predicate) {
        return new Filter<>(this, predicate);
    }

    public <R> Observable<R> flatMap(Function<T, Observable<R>> mapper) {
        return new FlatMap<>(this, mapper);
    }

    // Schedulers
    public Observable<T> subscribeOn(Scheduler scheduler) {
        return new Observable<>(observer ->
                scheduler.execute(() -> this.subscribe(observer))
        );
    }

    public Observable<T> observeOn(Scheduler scheduler) {
        return new Observable<>(observer ->
                this.subscribe(new Observer<T>() {
                    @Override
                    public void onNext(T item) {
                        scheduler.execute(() -> observer.onNext(item));
                    }

                    @Override
                    public void onError(Throwable t) {
                        scheduler.execute(() -> observer.onError(t));
                    }

                    @Override
                    public void onComplete() {
                        scheduler.execute(() -> observer.onComplete());
                    }
                })
        );
    }
}