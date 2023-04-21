package enums;

public enum eRequestStatus {
    PENDING("PENDING"),
    CANCELED("CANCELED"),
    FINISHED("FINISHED"),
    ON_GOING("ON_GOING");

    private final String dbName;

    private eRequestStatus(String dbName){
        this.dbName = dbName;
    }

    public static eRequestStatus tryGetValueOf(String status) {
        try{
            return valueOf(status.toUpperCase());
        }
        catch (Exception ex){
            return null;
        }
    }

    public String getDbName(){
        return this.dbName;
    }
}
