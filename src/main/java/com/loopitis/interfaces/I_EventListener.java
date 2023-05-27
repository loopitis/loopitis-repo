package com.loopitis.interfaces;

import com.loopitis.enums.eEvent;

public interface I_EventListener {
    void fire(eEvent event, Object data);
}
