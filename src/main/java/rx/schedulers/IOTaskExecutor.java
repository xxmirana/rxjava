package rx.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Scheduler optimized for I/O bound work
 */
public final class IOTaskExecutor implements TaskScheduler {
    private final ExecutorService threadPool;
    
    public IOTaskExecutor() {
        this.threadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void scheduleTask(Runnable task) {
        threadPool.execute(task);
    }

    @Override
    public void shutdown() {
        threadPool.shutdown();
    }
}