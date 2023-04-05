package general;

public class GetNotifierCheckResult {
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
