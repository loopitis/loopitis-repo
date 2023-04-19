package cli_server;

import com.example.demo.ConfigurationManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CLIServer {
    private static final int SERVER_PORT = ConfigurationManager.getInstance().getCliPort();;

    public void startCLIServer(){
        Thread cliThread = new Thread(()->listen());
        cliThread.setName("CLI Thread");
        cliThread.start();
    }

    public void listen(){
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started");

            while (true) {
                Socket socket = serverSocket.accept();

                new Thread(() -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                         PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

                        String input;
                        while ((input = reader.readLine()) != null) {
                            System.out.println("Received CLI input: " + input);


                            String response = CLI_MessageProcessor.process(OptionsCLI.getOptions(),  input.split("\\s+"));

                            String[] responseLines = response.split("\n");
                            for (String line : responseLines) {
                                if(line.equals(""))continue;
                                writer.print(line + "\n");
                            }
                            writer.println("END");
                            writer.flush();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
