package services;

import general.HttpNotifier;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class RESTServices {
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(RESTServices.class);

    public static HttpResponse<String> GET(String url){
        try {
            log.debug("About to fire a GET request ");

            StringBuilder uriBuilder = new StringBuilder(url);



            URI uri = URI.create(uriBuilder.toString());

            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .timeout(Duration.ofSeconds(3))
                    .build();


            HttpClient client = HttpClient.newBuilder()
                    .version(HttpNotifier.HTTP_VERSION)
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response != null)
                log.info(response.body());

            return response;

        }
        catch(Exception ex){
            ex.printStackTrace();
            return null;
        }

    }

    public static HttpResponse<String> POST(String url, String requestBody) {
        try {
            log.debug("About to fire a POST request "+url+" body: "+requestBody);

            URI uri = URI.create(url);

            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .uri(uri)
                    .timeout(Duration.ofSeconds(3))
                    .header("Content-Type", "application/json") // replace with your actual content type
                    .build();

            HttpClient client = HttpClient.newBuilder()
                    .version(HttpNotifier.HTTP_VERSION)
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response != null)
                log.info(response.body());

            return response;

        }
        catch(Exception ex){
            ex.printStackTrace();
            return null;
        }


    }
}
