package starters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import enums.eRequestStatus;
import general.ShowExecutionsRequest;
import general.ShowRequestsRequest;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import pojos.HttpNotifierRequest;
import services.FileUtils;
import services.RESTServices;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Scanner;


@Command(name = "CLI", mixinStandardHelpOptions = true, version = "1.0",
        subcommands = {CLI.AddRequest.class, CLI.ShowExecutions.class, CLI.ShowRequests.class})
public class CLI implements Runnable {

    private static Gson g = new GsonBuilder().setPrettyPrinting().create();
    private static String HOST = "https://call.loopitis.com";
//    private static String HOST = "http://localhost:8080";

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "display this help message")
    boolean helpRequested;


    public void run() {
//        CommandLine commandLine = new CommandLine(this);
//        commandLine.usage(System.out, CommandLine.Help.Ansi.ON);

//        CommandLine.usage(new MyApp(), System.out, CommandLine.Help.Ansi.ON, new MyCustomColorScheme());
    }



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
        String interval;

        @Option(names = {"-l", "--delay"}, description = "The delay before the first execution (could be either a number in ms such as 1000 or 1m, 2h, 3w etc.)")
        String delay;

        @Option(names = {"-p", "--payload"}, description = "The payload to send with the request")
        String payload;

        @Option(names = {"-f", "--file"}, description = "The file to print the result to")
        String file;

        public void run() {
            HttpNotifierRequest req = new HttpNotifierRequest();
            req.setOccurrences(occurrences);
            req.setReturn_url(returnUrl);
            req.setName(name);
            req.setInterval(interval);
            req.setDelay(delay);
            req.setPayload(payload);

            var result = RESTServices.POST(HOST+"/set/getNotifier", g.toJson(req));
            if(result != null){
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(result.body());
                String test = g.toJson(jsonElement);
                System.out.println(test);

                if(file != null) {
                    try {
                        FileUtils.writeToFile(file, test);
                    } catch (Exception e) {
                        System.out.println("Error writing result to file: " + e.getMessage());
                    }
                }
            }
        }
    }



    @Command(name = "show-executions", description = "Show executions for a request")
    static class ShowExecutions implements Runnable {
        @Parameters(index = "0",description = "The ID of the request to show executions for")
        String requestId;

        @Option(names = {"-f", "--file"}, description = "The file to print the result to")
        String file;

        public void run() {
            ShowExecutionsRequest req = new ShowExecutionsRequest();
            req.setRequestId(requestId);
            HttpResponse<String> result = RESTServices.POST(HOST + "/executions/list", g.toJson(req));
            if (result != null){
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(result.body());
                String test = g.toJson(jsonElement);
                System.out.println(test);
                if(file != null) {
                    try {
                        FileUtils.writeToFile(file, test);
                    } catch (Exception e) {
                        System.out.println("Error writing result to file: " + e.getMessage());
                    }
                }
             }
            else{
                System.out.println("Error");
            }
        }
    }



    @Command(name = "show-requests", description = "Show requests")
    static class ShowRequests implements Runnable {
        @Option(names = {"-s", "--status"}, required = true, description = "Filter requests by status (ongoing, in-progress, finished, pending)")
        String status;

        @Option(names = {"-f", "--file"}, description = "The file to print the result to")
        String file;

        @Option(names = {"-h", "--host"}, description = "The host server")
        String host;

        public void run() {
            host = host != null ? host : HOST;
            ShowRequestsRequest req = new ShowRequestsRequest();
            eRequestStatus estatus = eRequestStatus.tryGetValueOf(status);
            if(estatus == null){
                System.out.println("Unknown status "+status+" use one of "+ Arrays.asList(eRequestStatus.values()));
                return;
            }
            req.setStatus(estatus);
            HttpResponse<String> result = RESTServices.POST(host+"/requests/list", g.toJson(req));


            if(result != null) {
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(result.body());
                String test = g.toJson(jsonElement);
                System.out.println(test);
                if(file != null) {
                    try {
                        FileUtils.writeToFile(file, test);
                    } catch (Exception e) {
                        System.out.println("Error writing result to file: " + e.getMessage());
                    }
                }
            }
            else{
                System.out.println("Error");
            }
        }
    }



    public static void main(String[] args) {
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String command = scanner.nextLine();

            if(command.toLowerCase().equals("exit") || command.toLowerCase().equals("quit")){
                System.exit(0);
            }
            if(command.equals("1")){//for testing
                command = "show-requests -s FINISHED";
            }
//        String test = "add-request -o 12 -u https://google.com -n cliTest -i 10000 -l 7000 -m GET";
//        String test = "show-executions 2823e775-488d-4243-9850-8f8965f2d80f";
//            String test = "show-requests -s FINISHED";

            args = command.split("\\s");
            CommandLine.run(new CLI(), args);


        }
    }
}

