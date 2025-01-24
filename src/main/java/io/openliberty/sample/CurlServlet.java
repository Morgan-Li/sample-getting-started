package io.openliberty.sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/**
CurlServlet checks a URL's availability via HTTP request.
 */
@WebServlet("/curl")
public class CurlServlet extends HttpServlet {
    public CurlServlet() {
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "https://www.ibm.com"; // URL to check
        int httpResponseCode = checkHttpResponse(url);
        PrintWriter pw = response.getWriter();
        response.setContentType("text/plain");
        pw.println("Checked URL: " + url);
        if (httpResponseCode == -1) {
            pw.println("Unable to connect to the host.");
        } else {
            pw.println("HTTP Response Code: " + httpResponseCode);
            pw.println("Host reachable: " + (httpResponseCode >= 200 && httpResponseCode < 400 ? "Yes" : "No"));
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    private int checkHttpResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // 5-second timeout
            connection.setReadTimeout(5000);
            connection.connect();
            return connection.getResponseCode();
        } catch (IOException e) {
            return -1;
        }
    }
}