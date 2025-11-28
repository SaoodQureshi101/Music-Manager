import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;


public class iTunesFetcher {

    public static Song fetchFirstMatch(String query) {
        try {
            String q = URLEncoder.encode(query, "UTF-8");
            String api = "https://itunes.apple.com/search?term=" + q + "&limit=1&entity=song";
            URI uri = URI.create(api);
            URL url = uri.toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int status = conn.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();
            String json = content.toString();
            if (json.contains("\"resultCount\":0")) return null;

            String trackName = extractString(json, "\"trackName\":\""); 
            String artistName = extractString(json, "\"artistName\":\""); 
            String collectionName = extractString(json, "\"collectionName\":\""); 
            Integer trackTimeMs = extractInt(json, "\"trackTimeMillis\":"); 

            if (trackName == null || artistName == null) return null;
            return new Song(trackName, artistName, collectionName == null ? "" : collectionName, trackTimeMs == null ? 0 : trackTimeMs);
        } catch (Exception e) {
            System.out.println("Error fetching from iTunes: " + e.getMessage());
            return null;
        }
    }

    private static String extractString(String json, String key) {
        int idx = json.indexOf(key);
        if (idx == -1) return null;
        int start = idx + key.length();
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '\\') {
                if (i + 1 < json.length()) {
                    char next = json.charAt(i+1);
                    if (next == '"' || next == '\\' || next == '/') { sb.append(next); i++; continue; }
                    if (next == 'n') { sb.append('\n'); i++; continue; }
                }
            } else if (c == '"') {
                break;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static Integer extractInt(String json, String key) {
        int idx = json.indexOf(key);
        if (idx == -1) return null;
        int start = idx + key.length();
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (Character.isDigit(c)) sb.append(c);
            else break;
        }
        if (sb.length() == 0) return null;
        try { return Integer.parseInt(sb.toString()); } catch (Exception e) { return null; }
    }
}
