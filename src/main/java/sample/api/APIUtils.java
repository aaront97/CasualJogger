package sample.api;

import org.json.JSONObject;

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
}
