package rx;

public interface DataSubscriber<T> {

    void receiveElement(T element);

    void handleError(Throwable error);
    
    void onSequenceComplete();
}