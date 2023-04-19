package cli_server;

import org.apache.commons.cli.*;

public class OptionsCLI {

    public static Options getOptions() {
        // create global Options object
        OptionGroup globalOptionGroup = new OptionGroup();
        globalOptionGroup.addOption(new Option("h", "help", false, "Show help message"));
        globalOptionGroup.addOption(new Option("v", "version", false, "Show version of the app"));
        globalOptionGroup.addOption(new Option("d", "debug", false, "Enable debug mode"));

        // create Options object for add-request command
        OptionGroup addReqOptions = new OptionGroup();
        addReqOptions.addOption(Option.builder("o")
                .longOpt("occurrences")
                .desc("The number of times the request should be executed (required)")
                .hasArg()
                .argName("OCCURRENCES")
                .required()
                .build());
        addReqOptions.addOption(Option.builder("u")
                .longOpt("return-url")
                .desc("The URL to return the response (required)")
                .hasArg()
                .argName("RETURN_URL")
                .required()
                .build());
        addReqOptions.addOption(Option.builder("m")
                .longOpt("callback-method")
                .desc("The HTTP method to use for the callback (GET or POST, required)")
                .hasArg()
                .argName("CALLBACK_METHOD")
                .required()
                .build());
        addReqOptions.addOption(Option.builder("n")
                .longOpt("name")
                .desc("The name of the request (optional)")
                .hasArg()
                .argName("NAME")
                .build());
        addReqOptions.addOption(Option.builder("i")
                .longOpt("interval")
                .desc("The interval between executions (in milliseconds, optional)")
                .hasArg()
                .argName("INTERVAL")
                .build());
        addReqOptions.addOption(Option.builder("l")
                .longOpt("delay")
                .desc("The delay before the first execution (in milliseconds, optional)")
                .hasArg()
                .argName("DELAY")
                .build());
        addReqOptions.addOption(Option.builder("p")
                .longOpt("payload")
                .desc("The payload to send with the request (optional)")
                .hasArg()
                .argName("PAYLOAD")
                .build());

// create Options object for show-requests command
        OptionGroup showReqOptions = new OptionGroup();
        showReqOptions.addOption(Option.builder("s")
                .longOpt("status")
                .desc("Filter requests by status (ongoing, in-progress, finished, pending)")
                .hasArg()
                .argName("STATUS")
                .build());

// create Options object for show-executions command
        OptionGroup showExecOptions = new OptionGroup();
        showExecOptions.addOption(Option.builder("r")
                .longOpt("request-id")
                .desc("The ID of the request to show executions for (required)")
                .hasArg()
                .argName("REQUEST_ID")
                .required()
                .build());

// create Options object for show-comments command
        OptionGroup showCommentsOptions = new OptionGroup();
        showCommentsOptions.addOption(Option.builder("e")
                .longOpt("execution-id")
                .desc("The ID of the execution to show comments for (required)")
                .hasArg()
                .argName("EXECUTION_ID")
                .required()
                .build());

        // create Options object for the entire application
        Options allOptions = new Options();

        allOptions.addOptionGroup(globalOptionGroup);
        allOptions.addOptionGroup(addReqOptions);
        allOptions.addOptionGroup(showReqOptions);
        allOptions.addOptionGroup(showExecOptions);
        allOptions.addOptionGroup(showCommentsOptions);

        return allOptions;
    }

}
