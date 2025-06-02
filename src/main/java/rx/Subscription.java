package rx;

/**
 * Represents a connection between publisher and subscriber
 */
public interface Subscription {
    /**
     * Cancels the subscription
     */
    void cancel();
    
    /**
     * Checks if subscription is active
     */
    boolean isActive();
}

**src/main/java/rx/BasicSubscription.java**
```java
package rx;

/**
 * Simple subscription implementation
 */
public class BasicSubscription implements Subscription {
    private volatile boolean active = true;

    @Override
    public void cancel() {
        active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}