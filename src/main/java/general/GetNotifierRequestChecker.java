package general;

import pojos.HttpNotifierRequest;

public class GetNotifierRequestChecker {
    public static GetNotifierCheckResult check(HttpNotifierRequest g) {
        return new GetNotifierCheckResult();
    }
}
