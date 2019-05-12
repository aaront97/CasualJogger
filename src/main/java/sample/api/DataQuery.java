package sample.api;

import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class DataQuery {

    public static WeatherData queryData(String cityName) {

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

        GoogleAPI googleAPI = new GoogleAPI(googleKey);
        LatLonInfo latLonInfo = googleAPI.getCoord(cityName);

        DarkSkyData darkSkyData = DarkSkyAPI.getDarkSkyData(darkSkyKey, latLonInfo);
        BreezometerAPI breezometerAPI = new BreezometerAPI(breezometerKey,
                Double.toString(latLonInfo.lat), Double.toString(latLonInfo.lon));

        return new WeatherData(darkSkyData, breezometerAPI);
    }

}
