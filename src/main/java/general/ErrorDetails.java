package general;

public class ErrorDetails {
    private int code;
    private String message;
    private String details;
    private String field;

    public ErrorDetails() {

    }

    public ErrorDetails withCode(int code){
        this.code = code;
        return this;
    }

    public ErrorDetails withMessage(String msg){
        this.message = msg;
        return this;
    }

    public ErrorDetails withDetails(String details){
        this.details = details;
        return this;
    }

    public ErrorDetails withField(String field){
        this.field = field;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}

