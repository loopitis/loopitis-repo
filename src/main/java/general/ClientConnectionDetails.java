package general;

import enums.eEvent;

import java.util.List;

public class ClientConnectionDetails {
    private List<eEvent> events;
    private Integer failures;

    public ClientConnectionDetails(List<eEvent> eventsToListen) {
        events = eventsToListen;
        failures = 0;
    }

    public List<eEvent> getEvents() {
        return events;
    }

    public void setEvents(List<eEvent> events) {
        this.events = events;
    }

    public Integer getFailures() {
        return failures;
    }

    public void setFailures(Integer failures) {
        this.failures = failures;
    }
}
