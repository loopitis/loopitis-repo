package consumer;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "notifier.executions")
public class ExecutionRequest {

    public ExecutionRequest(){}

    public ExecutionRequest(String executionExternalId, String request_externalId, Long now) {
        this.executionId = executionExternalId;
        this.requestId = request_externalId;
        this.timeExecuted = new Timestamp(now);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i_id")
    private Long id;

    @Column(name = "e_id")
    private String executionId;

    @Column(name = "e_request_id")
    private String requestId;

    @Column(name = "time_executed")
    private Timestamp timeExecuted;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "comment")
    private String comment;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Timestamp getTimeExecuted() {
        return timeExecuted;
    }

    public void setTimeExecuted(Timestamp timeExecuted) {
        this.timeExecuted = timeExecuted;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

