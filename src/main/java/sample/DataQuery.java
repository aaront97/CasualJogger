package sample;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class DataQuery {

    public static WeatherData queryData(String cityName) {

        String breezometerKey = "";
        String darkSkyKey = "";
        String googleKey = "";

        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject json = (JSONObject) jsonParser.parse(new FileReader("apiKeys.json"));

            breezometerKey = (String) json.get("breezometer");
            darkSkyKey = (String) json.get("darkSky");
            googleKey = (String) json.get("google");

        } catch (Exception e) {
            System.out.println("An error while reading API keys from JSON");
            e.printStackTrace();
            return null;
        }

        // TODO: Query each API and fill up WeatherData
        return null;
    }

    // FOR DEBUGGING ONLY
    public static void main(String[] s) {

    }

}
