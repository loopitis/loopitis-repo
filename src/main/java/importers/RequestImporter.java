package importers;

import ent.HttpNotifierRequestEntity;
import managers.DBManager;

public class RequestImporter {
    public Long saveRequest(HttpNotifierRequestEntity notif) {
        DBManager.getInstance().saveRequest(notif);
        return notif.getId();

    }
}
