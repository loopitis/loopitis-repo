package general;

public class NotifierConstants {

    public static final String CONF_DB_ENDPOINTS_CONNECTION_PULL_MAX_SIZE  = "db_max_pool_size";
    public static final String KAFKA_TASKS_TOPIC_NAME = "topic_name_tasks";
    public static final String CONF_DB_NAME = "db_name";
    public static final String CONF_DB_SERVER_NAME = "db_host";
    public static final String CONF_DB_PASSWORD = "db_password";
    public static final String CONF_DB_USER = "db_user";
    public static final String CONF_DB_PORT = "db_port";
    public static final String CONF_DB_READ_ONLY_MODE = "db_read_only";

    public static final int CONF_DB_DEFAULT_CONNECTION_PULL_MAX_SIZE = 1;
    public static final String CONF_IS_DEVELOPMENT = "isDev";
    public static final String CONF_MINIMUM_INTERVAL_TIME_MS = "minimum_interval_time";
    public static final String CONF_EXECUTION_HTTP_VERSION = "notifications_http_version";
    public static final String CONF_NUMBER_OF_THREADS_CONSUMER = "timer_threads_in_process";
    public static final String CONF_REDIS_HOST = "redis_host";
    public static final String CONF_REDIS_PORT = "redis_port";
    public static final String CONF_REDIS_LISTENERS_KEY = "redis_listeners_key";
    public static final String CONF_KAFKA_HOST = "kafka_host";
    public static final String CONF_REDIS_CANCEL_CHANNEL = "cancel_request_channel";
    public static final String CONF_CLI_PORT = "cli_port";
}
