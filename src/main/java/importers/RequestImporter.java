package importers;

import ent.HttpNotifierRequestEntity;
import managers.DBhibernetManager;
import pojos.HttpNotifierRequest;

public class RequestImporter {
    public Long saveRequest(HttpNotifierRequest notif) {
        var entity = new HttpNotifierRequestEntity(notif);
        DBhibernetManager.getInstance().saveRequest(entity);
        return entity.getId();

    }
}
