package services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    public static void writeToFile(String filePath, String content) {
        try {
            File file = new File(filePath);

            // Create the file if it doesn't exist
            if (!file.exists()) {
                file.createNewFile();
            }

            // Write the content to the file
            FileWriter writer = new FileWriter(file, true);
            writer.write(content);
            writer.close();

            System.out.println("Content written to file " + filePath);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to file " + filePath + ": " + e.getMessage());
        }
    }
}
