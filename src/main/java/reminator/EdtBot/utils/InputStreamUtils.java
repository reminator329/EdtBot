package reminator.EdtBot.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class InputStreamUtils {
    public static String readAsString(InputStream inputStream) throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            while ((line = reader.readLine()) != null) {
                strBuilder.append(line.trim()).append("\n");
            }
        } catch (IOException ignored) {
        }
        inputStream.close();
        return strBuilder.toString();
    }
}
