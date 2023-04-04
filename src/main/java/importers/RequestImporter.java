package importers;

import ent.HttpNotifierRequestEntity;
import managers.DBhibernetManager;
import pojos.HttpNotifierRequest;

public class RequestImporter {
    public Long saveRequest(HttpNotifierRequestEntity notif) {
        DBhibernetManager.getInstance().saveRequest(notif);
        return notif.getId();

    }
}
