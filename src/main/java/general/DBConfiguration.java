package general;

public class DBConfiguration {
    private int _maxPoolSize;
    private String _dbName;
    private String _dbHost;
    private String _dbUser;
    private String _dbPassword;
    private int _dbPort;
    private boolean isReadOnly;

    public int get_maxPoolSize() {
        return _maxPoolSize;
    }
    public void set_maxPoolSize(int _maxPoolSize) {
        this._maxPoolSize = _maxPoolSize;
    }
    public String get_dbName() {
        return _dbName;
    }
    public void set_dbName(String _dbName) {
        this._dbName = _dbName;
    }
    public String get_dbHost() {
        return _dbHost;
    }
    public void set_dbHost(String _dbHost) {
        this._dbHost = _dbHost;
    }
    public String get_dbUser() {
        return _dbUser;
    }
    public void set_dbUser(String _dbUser) {
        this._dbUser = _dbUser;
    }
    public String get_dbPassword() {
        return _dbPassword;
    }
    public void set_dbPassword(String _dbPassword) {
        this._dbPassword = _dbPassword;
    }
    public int get_dbPort() {
        return _dbPort;
    }
    public void set_dbPort(int _dbPort) {
        this._dbPort = _dbPort;
    }
    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

}