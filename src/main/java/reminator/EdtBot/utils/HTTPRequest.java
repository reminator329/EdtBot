package reminator.EdtBot.utils;


import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HTTPRequest {
    private final String url;

    private final Map<String, String> parametersGET = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();

    public HTTPRequest(String url) {
        this.url = url;
    }

    public HTTPRequest withHeader(String header, String value) {
        headers.put(header, value);
        return this;
    }

    public HTTPRequest withGETParameter(String key, String value) {
        parametersGET.put(key, value);
        return this;
    }

    public String GET() throws IOException {
        String args = "";
        if(parametersGET.size() > 0) {
            args = "?" + parametersGET.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
            this.parametersGET.clear();
        }
        HttpsURLConnection connection = newConnection(this.url+args);
        connection.setRequestMethod("GET");


        return InputStreamUtils.readAsString(connection.getInputStream());
    }

//    public String POST(String path, String data) throws IOException {
//        HttpsURLConnection connection = newConnection(path);
//        connection.setRequestMethod("POST");
//
//        this.parametersGET.clear();
//
//        OutputStream out = connection.getOutputStream();
//        out.write(data.getBytes());
//        out.flush();
//        out.close();
//
//        connection.getResponseCode();
//        connection.getResponseMessage();
//
//        return readInputStream(connection.getInputStream());
//    }

    private HttpsURLConnection newConnection(String url) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        return connection;
    }
}
