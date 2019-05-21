package sample.api;

import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *  Queries all API sources for weather data and merge them into a WeatherData object.
 */
public class DataQuery {

    public static WeatherData queryData(String cityName) throws LocationNotFoundException, APIReadException, LocationOutOfReachException {

        // The API keys are kept in a different file since
        // we should NEVER EVER push them to the public repo
        String breezometerKey = "";
        String darkSkyKey = "";
        String googleKey = "";

        // Reading the API keys from the JSON file
        try {
            String apiKeysString = new String(
                    Files.readAllBytes(Paths.get(System.getProperty("user.dir") +"\\src\\main\\java\\sample\\apiKeys.json")));
            JSONObject apiKeys = new JSONObject(apiKeysString);
            breezometerKey = apiKeys.getString("breezometer");
            darkSkyKey = apiKeys.getString("darkSky");
            googleKey = apiKeys.getString("google");
        } catch (Exception e) {
            throw new APIReadException();
        }

        // Querying the Google API for co-ordinates
        GoogleAPI googleAPI = new GoogleAPI(googleKey);
        LatLonInfo latLonInfo = googleAPI.getCoord(cityName);

        // Querying the DarkSky and Breezometer API using co-ordinates from Google
        DarkSkyData darkSkyData = DarkSkyAPI.getDarkSkyData(darkSkyKey, latLonInfo);
        BreezometerAPI breezometerAPI = new BreezometerAPI(breezometerKey,
                Double.toString(latLonInfo.lat), Double.toString(latLonInfo.lon));

        // Constructing WeatherData object encapsulating
        // all relevant API data used by the UI
        WeatherData weatherData;
        weatherData = new WeatherData(darkSkyData, breezometerAPI);
        weatherData.location = latLonInfo.address;

        return weatherData;
    }

}