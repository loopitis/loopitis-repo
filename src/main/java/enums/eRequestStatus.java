package enums;

public enum eRequestStatus {
    PENDING("PENDING"),
    CANCELED("CANCELED"),
    FINISHED("FINISHED"),
    IN_PROGRESS("ON_GOING");

    private final String dbName;

    private eRequestStatus(String dbName){
        this.dbName = dbName;
    }

    public String getDbName(){
        return this.dbName;
    }
}
