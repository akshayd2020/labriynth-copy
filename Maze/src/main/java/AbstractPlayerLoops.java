/**
 * This player will go into an infinite loop after calling the infiniteLoopOnKthMethodCall()
 * method the specified number of times.
 */
public abstract class AbstractPlayerLoops extends Player {
    // This is the number of times infiniteLoopOnKthMethodCall() must be called to go into an infinite loop.
    private final int numCallsToInfiniteLoop;
    private int timesMethodWasCalled;

    public AbstractPlayerLoops(String name, IStrategy strategy, int numCallsToInfiniteLoop) {
        super(name, strategy);
        this.numCallsToInfiniteLoop = numCallsToInfiniteLoop;
        this.timesMethodWasCalled = 0;
    }

    /**
     *  Increments the number of times this method was called. When this method is called
     *  for the Kth time (where K is numCallsToInfiniteLoop), an infinite loop will occur.
     */
    protected void infiniteLoopOnKthMethodCall() {
        timesMethodWasCalled++;
        if (timesMethodWasCalled == numCallsToInfiniteLoop) {
            infiniteLoop();
        }
    }

    private void infiniteLoop() {
        while (true) {
            // infinite loop
        }
    }
}