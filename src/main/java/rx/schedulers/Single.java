package rx.schedulers;

import rx.Scheduler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Single implements Scheduler {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }
}