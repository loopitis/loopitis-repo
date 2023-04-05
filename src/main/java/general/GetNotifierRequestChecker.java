package general;

import com.example.demo.ConfigurationManager;
import pojos.HttpNotifierRequest;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class GetNotifierRequestChecker {


    private static final Long MINIMUM_INTERVAL_TIME = ConfigurationManager.getInstance().getMinimumIntervalTime();

    public static GetNotifierCheckResult check(HttpNotifierRequest g) {
        var res = new GetNotifierCheckResult();
        if(g.getInterval() == null){
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withMessage("Value Error")
                    .withDetails("Interval must be provided")
                    .withField("Interval");

            res.setError(details);
            return res;
        }
        if(g.getInterval() < MINIMUM_INTERVAL_TIME) {
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withMessage("Value Error")
                    .withDetails("Interval cannot be less than "+MINIMUM_INTERVAL_TIME)
                    .withField("Interval");
            res.setError(details);
        }
        if(g.getDelay() < 0){
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withMessage("Value Error")
                    .withDetails("Delay cannot be negative ")
                    .withField("delay");
        }
        if(g.getOccurrences() < 2){
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withMessage("Value Error")
                    .withDetails("Occurrences cannot be negative ")
                    .withField("occurrences");
        }
        if(g.getReturn_url() == null){
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withMessage("Value missing")
                    .withDetails("Return url must be provided ")
                    .withField("return_url");
        }
        if(!isValidURL(g.getReturn_url())){
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withMessage("Value Error")
                    .withDetails("Return url must be valid ")
                    .withField("return_url");
        }
        return res;
    }

    private static boolean isValidURL(String returnUrl) {
        try {
            new URL(returnUrl).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

}
