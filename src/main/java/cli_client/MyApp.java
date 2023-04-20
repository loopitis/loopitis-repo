package cli_client;

import com.google.gson.Gson;
import general.ShowExecutionsRequest;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import pojos.HttpNotifierRequest;
import services.RESTServices;

import java.net.http.HttpResponse;
import java.util.Scanner;


@Command(name = "MyApp", mixinStandardHelpOptions = true, version = "1.0",
        subcommands = {MyApp.AddRequest.class, MyApp.ShowExecutions.class, MyApp.ShowComments.class, MyApp.ShowRequests.class})
public class MyApp implements Runnable {

    private static Gson g = new Gson();
    private static final String HOST = "http://localhost";
    private static final int PORT = 8080;

    @Command(name = "add-request", description = "Add a new request")
    static class AddRequest implements Runnable {
        @Option(names = {"-o", "--occurrences"}, required = true, description = "The number of times the request should be executed")
        int occurrences;

        @Option(names = {"-u", "--return-url"}, required = true, description = "The URL to return the response")
        String returnUrl;

        @Option(names = {"-m", "--callback-method"}, required = true, description = "The HTTP method to use for the callback (GET or POST)")
        String callbackMethod;

        @Option(names = {"-n", "--name"}, description = "The name of the request")
        String name;

        @Option(names = {"-i", "--interval"}, required = true, description = "The interval between executions (in milliseconds)")
        Long interval;

        @Option(names = {"-l", "--delay"}, description = "The delay before the first execution (in milliseconds)")
        Long delay;

        @Option(names = {"-p", "--payload"}, description = "The payload to send with the request")
        String payload;

        public void run() {
            HttpNotifierRequest req = new HttpNotifierRequest();
            req.setOccurrences(occurrences);
            req.setReturn_url(returnUrl);
            req.setName(name);
            req.setInterval(interval);
            req.setDelay(delay);
            req.setPayload(payload);

            RESTServices.POST(HOST+":"+PORT+"/set/getNotifier", g.toJson(req));
        }
    }

    @Command(name = "show-executions", description = "Show executions for a request")
    static class ShowExecutions implements Runnable {
        @Parameters(index = "0",description = "The ID of the request to show executions for")
        String requestId;

        public void run() {
            ShowExecutionsRequest req = new ShowExecutionsRequest();
            req.setRequestId(requestId);
            HttpResponse<String> result = RESTServices.POST(HOST+":"+PORT+"/executions/list", g.toJson(req));
            if(result != null)
                System.out.println(result.body());
            else{
                System.out.println("Error");
            }
        }
    }

    @Command(name = "show-comments", description = "Show comments for an execution")
    static class ShowComments implements Runnable {
        @Parameters(index = "0", description = "The ID of the execution to show comments for")
        Integer executionId;

        public void run() {
            // implementation for show-comments command
            System.out.println("showwwwwwww");
        }
    }

    @Command(name = "show-requests", description = "Show requests")
    static class ShowRequests implements Runnable {
        @Option(names = {"-s", "--status"}, description = "Filter requests by status (ongoing, in-progress, finished, pending)")
        String status;

        public void run() {
            // implementation for show-requests command
        }
    }

    public void run() {
        // implementation for main command
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        while(true) {
            String command = scanner.nextLine();


//        String test = "add-request -o 12 -u https://google.com -n cliTest -i 10000 -l 7000 -m GET";
//        String test = "show-executions 2823e775-488d-4243-9850-8f8965f2d80f";
            args = command.split("\\s");
            CommandLine.run(new MyApp(), args);
        }
    }
}

