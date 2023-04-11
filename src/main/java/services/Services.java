package services;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Services {

    public static boolean isValidURL(String returnUrl) {
        try {
            new URL(returnUrl).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}
