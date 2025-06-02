package rx.schedulers;

import rx.Scheduler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Computation implements Scheduler {
    private final ExecutorService executor;

    public Computation() {
        this(Runtime.getRuntime().availableProcessors());
    }

    public Computation(int threads) {
        executor = Executors.newFixedThreadPool(threads);
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }
}