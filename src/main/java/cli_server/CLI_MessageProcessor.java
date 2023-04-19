package cli_server;

import org.apache.commons.cli.*;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CLI_MessageProcessor {

    public static String process(Options options, String[] args) {
        if(args == null || (args.length ==1 && args[0].isEmpty()))
            return "";
        // create command line parser
        CommandLineParser parser = new DefaultParser();

        try {
            // parse the options and commands
            CommandLine cmd = parser.parse(options, args);

            // check if help flag is set
            if (cmd.hasOption("h")) {
                return getHelpString(options);
            }

            // check if version flag is set
            if (cmd.hasOption("v")) {
                // show version
                System.out.println("Version 1.0.0");
                System.exit(0);
            }

            // check if debug flag is set
            if (cmd.hasOption("d")) {
                // enable debug mode
                // ...
            }

            // handle commands
            if (cmd.hasOption("add-request")) {
                // handle add-request command
                String occurrences = cmd.getOptionValue("o");
                String returnUrl = cmd.getOptionValue("u");
                String callbackMethod = cmd.getOptionValue("m");
                String name = cmd.getOptionValue("n");
                String interval = cmd.getOptionValue("i");
                String delay = cmd.getOptionValue("l");
                String payload = cmd.getOptionValue("p");

                // Process the values obtained from command line options
                // ...

                System.out.println("Add request command executed.");
                System.out.println("Occurrences: " + occurrences);
                System.out.println("Return URL: " + returnUrl);
                System.out.println("Callback Method: " + callbackMethod);
                System.out.println("Name: " + name);
                System.out.println("Interval: " + interval);
                System.out.println("Delay: " + delay);
                System.out.println("Payload: " + payload);

            } else if (cmd.hasOption("show-requests")) {
                // handle show-requests command
                String status = cmd.getOptionValue("s");

                // Process the value obtained from command line option
                // ...

                System.out.println("Show requests command executed.");
                System.out.println("Status: " + status);

            } else if (cmd.hasOption("show-executions")) {
                // handle show-executions command
                String requestId = cmd.getOptionValue("r");

                // Process the value obtained from command line option
                // ...

                System.out.println("Show executions command executed.");
                System.out.println("Request ID: " + requestId);

            } else if (cmd.hasOption("show-comments")) {
                // handle show-comments command
                String executionId = cmd.getOptionValue("e");

                // Process the value obtained from command line option
                // ...

                System.out.println("Show comments command executed.");
                System.out.println("Execution ID: " + executionId);
            } else {
                // handle unrecognized command or no command provided
                System.out.println("Unrecognized command or no command provided. Use -h or --help option for help.");
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }




        return getHelpString(options);



    }

    private static String getHelpString(Options options) {
        // show help message
        StringWriter sw = new StringWriter();

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(new PrintWriter(sw), 150, "app", null, options, 2, 2, null, false);
        String helpString = sw.toString();
        return helpString;
    }
}
