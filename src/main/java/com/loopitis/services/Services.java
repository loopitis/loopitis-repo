package com.loopitis.services;

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

    public static Long convertToMilliseconds(String duration) {
        if (isLongValue(duration)) return Long.valueOf(duration);//in case number is given

        long milliseconds = 0;

        // Split duration into components
        String[] components = duration.split("\\+");
        for (String component : components) {
            // Extract value and unit from each component
            String unit = component.substring(component.length() - 1);
            Integer value = tryParseInt(component);

            if (value == null) return null;

            // Convert component duration to milliseconds
            switch (unit) {
                case "s":
                    milliseconds += value * 1000;
                    break;
                case "m":
                    milliseconds += value * 60 * 1000;
                    break;
                case "h":
                    milliseconds += value * 60 * 60 * 1000;
                    break;
                case "d":
                    milliseconds += value * 24 * 60 * 60 * 1000;
                    break;
                case "w":
                    milliseconds += value * 7 * 24 * 60 * 60 * 1000;
                    break;
                default:
                    throw null;
            }
        }

        return milliseconds;
    }

    private static Integer tryParseInt(String component) {
        try {
            return Integer.parseInt(component.substring(0, component.length() - 1));
        } catch (Exception ex) {
            return null;
        }
    }


    private static boolean isLongValue(String duration) {
        try {
            Long.valueOf(duration);
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

}
