package org.khannex.action;

public class Context {
    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException( Exception exception ) {
        this.exception = exception;
    }

    public boolean hasException() {
        return exception != null;
    }

    public void clear() {
        this.exception = null;
    }

}
