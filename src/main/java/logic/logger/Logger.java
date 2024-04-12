package logic.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private BufferedWriter writer;

    public Logger(String filePath) {
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logEvent(String event) {
        try {
            writer.write(event);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
