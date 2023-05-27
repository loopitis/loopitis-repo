package com.loopitis.general;

public class NotifierCheckResult {
    private ErrorDetails error;
    private boolean isError;

    public boolean isError() {
        return isError;
    }


    public void setError(ErrorDetails details) {
        isError = true;
        error = details;
    }
}
