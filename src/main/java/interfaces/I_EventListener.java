package interfaces;

import enums.eEvent;

public interface I_EventListener {
    void fire(eEvent event, Object data);
}
