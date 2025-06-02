package rx;

public interface Scheduler {
    void execute(Runnable task);

    static Scheduler io() {
        return new schedulers.IO();
    }

    static Scheduler computation() {
        return new schedulers.Computation();
    }

    static Scheduler single() {
        return new schedulers.Single();
    }
}