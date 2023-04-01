package importers;

import managers.DBManager;
import pojos.HttpNotifierRequest;

public class RequestImporter {
    public Integer saveRequest(HttpNotifierRequest notif) {
        ResultSet res = DBManager.getInstance().saveRequest(notif);
        return res.getInt(1);


    }
}
