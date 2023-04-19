package cli_client;

import com.example.demo.ConfigurationManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CLI_Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = ConfigurationManager.getInstance().getCliPort();

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to server");
            System.out.print(">");

            String input;
            while ((input = scanner.nextLine()) != null) {
                writer.println(input);

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.equals("END")) {
                        response.append(">");
                        break;
                    }
                    response.append(line);
                    response.append(System.lineSeparator());
                }
                System.out.print(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
