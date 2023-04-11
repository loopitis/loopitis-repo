package general;

import enums.eEvent;

import java.util.List;

public class ClientConnectionDetails {
    private List<eEvent> events;
    private Integer failures;

    public ClientConnectionDetails(List<eEvent> eventsToListen) {
        events = eventsToListen;
        failures =0;
    }
}
