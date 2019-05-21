package sample.api;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Contains utility functions for the API section.
 * Since data is not always available, use the extended
 * versions of jsonObject.get*
 */
public class APIUtils {

    /**
     * @return If jsonObject contains the key, then it tries to retrieve it
     *         Returns null if key is not contained
     */
    public static int tryToGetInt(JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            return jsonObject.getInt(key);
        }
        return 0;
    }

    /**
     * @return If jsonObject contains the key, then it tries to retrieve it
     *         Returns null if key is not contained
     */
    public static double tryToGetDouble(JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            return jsonObject.getDouble(key);
        }
        return 0;
    }

    /**
     * @return If jsonObject contains the key, then it tries to retrieve it
     *         Returns null if key is not contained
     */
    public static String tryToGetString(JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            return jsonObject.getString(key);
        }
        return null;
    }

    /**
     * @param address The address to be queried
     * @return The JSONObject queried from the website
     * @throws IOException Network error
     */
    public static JSONObject getJsonFromUrl(String address) throws IOException {

        // Connecting to the address
        URL url = new URL(address);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // Parsing response
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