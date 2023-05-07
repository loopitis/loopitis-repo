package ent;

import enums.eCallbackType;
import enums.eRequestStatus;
import pojos.HttpNotifierRequest;
import pojos.HttpNotifierRequestTranslated;
import services.Services;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;

@Entity
@Table(name = "notifier.requests")
public class HttpNotifierRequestEntity {


    public HttpNotifierRequestEntity(){}

    public HttpNotifierRequestEntity(HttpNotifierRequestTranslated entity) {
        this.id = entity.getId();
        this.externalId = entity.getExternal_id();
        this.name = entity.getName();
        this.returnUrl = entity.getReturn_url();
        this.delay = entity.getDelay();
        this.interval = entity.getInterval();
        this.occurrences = entity.getOccurrences();
        this.payload = entity.getPayload();
        long currentMillis = Calendar.getInstance().getTimeInMillis();
        this.requestTime = new Timestamp(currentMillis);
        this.firstExecTime = new Timestamp(currentMillis+this.delay);
        this.callbackType = entity.getCallback_type();

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i_id")
    private Long id;

    @Column(name = "callbackType")
    @Enumerated(EnumType.STRING)
    private eCallbackType callbackType;


    @Column(name = "e_id")
    private String externalId;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private eRequestStatus status;

    @Column(name = "return_url")
    private String returnUrl;

    @Column(name = "delay")
    private Long delay;

    @Column(name = "interval")
    private Long interval;

    @Column(name = "occurances")
    private Integer occurrences;

    @Column(name = "done")
    private Integer done;


    @Lob
    @Column(name = "payload")
    private String payload;

    @Column(name = "request_time")
    private Timestamp requestTime;

    @Column(name = "first_exec_time")
    private Timestamp firstExecTime;

    // getters and setters

    public Timestamp getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Timestamp requestTime) {
        this.requestTime = requestTime;
    }

    public Timestamp getFirstExecTime() {
        return firstExecTime;
    }

    public void setFirstExecTime(Timestamp firstExecTime) {
        this.firstExecTime = firstExecTime;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public eRequestStatus getStatus() {
        return status;
    }

    public void setStatus(eRequestStatus status) {
        this.status = status;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    public Integer getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(Integer occurrences) {
        this.occurrences = occurrences;
    }

    public Integer getDone() {
        return done;
    }

    public void setDone(Integer done) {
        this.done = done;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public eCallbackType getCallbackType() {
        return callbackType;
    }

    public void setCallbackType(eCallbackType callbackType) {
        this.callbackType = callbackType;
    }
}

