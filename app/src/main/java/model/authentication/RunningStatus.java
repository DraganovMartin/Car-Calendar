package model.authentication;

/**
 * Singleton for monitoring status of program
 */
public class RunningStatus {
    private static RunningStatus ourInstance = new RunningStatus();

    public static RunningStatus getInstance() {
        return ourInstance;
    }
    private boolean status = false;

    /**
     * Checks app status
     * @return - true if app hasn't been closed, false otherwise
     */
    public boolean stillRunning() {
        return status;
    }

    /**
     * set false if app is being closed, true when started(running)
     * @param status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }




    private RunningStatus() {
    }
}
