package io.openliberty.sample;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/curl")
public class CurlServlet extends HttpServlet {
    public CurlServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "http://150.239.109.74:8080"; 
        HttpResponse result = fetchHttpResponse(url);

        PrintWriter pw = response.getWriter();
        response.setContentType("text/plain");

        pw.println("Checked URL: " + url);
        if (result.responseCode == -1) {
            pw.println("Unable to connect to the host.");
        } else {
            pw.println("HTTP Response Code: " + result.responseCode);
            pw.println("Host reachable: " + (result.responseCode >= 200 && result.responseCode < 400 ? "Yes" : "No"));
            pw.println("Response Body:");
            pw.println(result.responseBody);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private HttpResponse fetchHttpResponse(String urlString) {
        HttpResponse result = new HttpResponse();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); 
            connection.setReadTimeout(5000);
            connection.connect();

            result.responseCode = connection.getResponseCode();

            if (result.responseCode >= 200 && result.responseCode < 400) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBody = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBody.append(line).append("\n");
                }
                reader.close();
                result.responseBody = responseBody.toString();
            } else {
                result.responseBody = "No response body available (HTTP code " + result.responseCode + ")";
            }

        } catch (IOException e) {
            result.responseCode = -1;
            result.responseBody = "Error: " + e.getMessage();
        }
        return result;
    }

    private static class HttpResponse {
        int responseCode;
        String responseBody;
    }
}
