package sample;

import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataQuery {

    public static sample.WeatherData queryData(String cityName) {

        String breezometerKey = "";
        String darkSkyKey = "";
        String googleKey = "";


        try {

            String apiKeysString = new String(
                    Files.readAllBytes(Paths.get(System.getProperty("user.dir") +"\\src\\main\\java\\sample\\apiKeys.json")));
            JSONObject apiKeys = new JSONObject(apiKeysString);
            breezometerKey = apiKeys.getString("breezometer");
            darkSkyKey = apiKeys.getString("darkSky");
            googleKey = apiKeys.getString("google");


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
