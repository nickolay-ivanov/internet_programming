package org.example;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;


@WebServlet("/urlProcessor")
public class URLProcessorServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String url = request.getParameter("url");
        if (url == null || url.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"URL is required\"}");
            return;
        }

        if (!isValidDomain(url)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Invalid domain format\"}");
            return;
        }

        if (!isURLAccessible(url)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"URL is not accessible\"}");
            return;
        }

        try {
            ArrayList<String> links = extractLinks(url);
            String jsonResponse = buildJsonResponse(links);
            out.write(jsonResponse);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\":\"Failed to process the URL\"}");
        }
    }

    private boolean isValidDomain(String url) {
        String domainRegex = ".*://.*\\d.*\\..*";
        Pattern pattern = Pattern.compile(domainRegex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    private boolean isURLAccessible(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode < 400);
        } catch (Exception e) {
            return false;
        }
    }

    private ArrayList<String> extractLinks(String url) throws IOException {
        ArrayList<String> links = new ArrayList<>();
        Document document = Jsoup.connect(url).get();
        Elements anchorTags = document.select("a[href]");
        for (Element anchor : anchorTags) {
            links.add(anchor.attr("abs:href"));
        }
        return links;
    }

    private String buildJsonResponse(ArrayList<String> links) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (int i = 0; i < links.size(); i++) {
            json.append("\"").append(links.get(i).replace("\"", "\\\"")).append("\"");
            if (i < links.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }
}
