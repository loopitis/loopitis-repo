package com.loopitis.general;

import com.loopitis.endpoints.ConfigurationManager;
import com.loopitis.enums.eCallbackType;
import com.loopitis.pojos.HttpNotifierRequestTranslated;

import static com.loopitis.services.Services.isValidURL;

public class NotifierRequestChecker {


    private static final Long MINIMUM_INTERVAL_TIME = ConfigurationManager.getInstance().getMinimumIntervalTime();

    public static NotifierCheckResult check(HttpNotifierRequestTranslated g) {
        var res = new NotifierCheckResult();
        if (g.getInterval() == null) {
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withMessage("Value Error")
                    .withDetails("Interval must be provided")
                    .withField("Interval");

            res.setError(details);
            return res;
        }
        if (g.getInterval() < MINIMUM_INTERVAL_TIME) {
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withMessage("Value Error")
                    .withDetails("Interval cannot be less than " + MINIMUM_INTERVAL_TIME)
                    .withField("Interval");
            res.setError(details);
            return res;
        }
        if (g.getDelay() == null || g.getDelay() < 0) {
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withMessage("Value Error")
                    .withDetails("Delay cannot be negative ")
                    .withField("delay");
            res.setError(details);
            return res;
        }
        if (g.getOccurrences() == null || g.getOccurrences() < 2) {
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withMessage("Value Error")
                    .withDetails("Occurrences cannot be negative ")
                    .withField("occurrences");
        }
        if (g.getReturn_url() == null) {
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withMessage("Value missing")
                    .withDetails("Return url must be provided ")
                    .withField("return_url");
            res.setError(details);
            return res;
        }
        if (!isValidURL(g.getReturn_url())) {
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withMessage("Value Error")
                    .withDetails("Return url must be valid ")
                    .withField("return_url");
            res.setError(details);
            return res;
        }
        if (g.getNotify_status_not_ok() != null && !isValidURL(g.getReturn_url())) {
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withMessage("Value Error")
                    .withDetails("Return notify if \"status not ok\" must be valid url")
                    .withField("return_url");
            res.setError(details);
            return res;
        }
        return res;
    }


    public static void setDefaultsForMissingValues(HttpNotifierRequestTranslated notif) {
        if (notif.getCallback_type() == null) {
            notif.setCallback_type(eCallbackType.POST);
        }
    }
}
