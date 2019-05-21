package sample.api;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIUtils {

    public static int tryToGetInt(JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            return jsonObject.getInt(key);
        }
        return 0;
    }

    public static double tryToGetDouble(JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            return jsonObject.getDouble(key);
        }
        return 0;
    }

    public static String tryToGetString(JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            return jsonObject.getString(key);
        }
        return null;
    }

    public static JSONObject getJsonFromUrl(String address) throws IOException {
        URL url = new URL(address);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader input = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );
        StringBuffer json_response = new StringBuffer();
        String line;
        while((line = input.readLine()) != null){
            json_response.append(line);
        }
        return new JSONObject(json_response.toString());
    }
}
