package com.loopitis.importers;

import com.loopitis.managers.DBManager;
import com.loopitis.ent.HttpNotifierRequestEntity;

public class RequestImporter {
    public Long saveRequest(HttpNotifierRequestEntity notif) {
        DBManager.getInstance().saveRequest(notif);
        return notif.getId();

    }
}
