package sample.api;

import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class DataQuery {

    public static WeatherData queryData(String cityName) throws LocationNotFoundException, APIReadException, LocationOutOfReachException {

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
            throw new APIReadException();
        }

        GoogleAPI googleAPI = new GoogleAPI(googleKey);
        LatLonInfo latLonInfo;
        try{
            latLonInfo = googleAPI.getCoord(cityName);
        }
        catch(LocationNotFoundException e){
            //throw the exception in order to be handled by the Controller object
            throw new LocationNotFoundException(e.getMessage());
        }


        DarkSkyData darkSkyData = DarkSkyAPI.getDarkSkyData(darkSkyKey, latLonInfo);
        BreezometerAPI breezometerAPI = new BreezometerAPI(breezometerKey,
                Double.toString(latLonInfo.lat), Double.toString(latLonInfo.lon));

        WeatherData weatherData;
        try{
            weatherData = new WeatherData(darkSkyData, breezometerAPI);
        }
        catch(LocationOutOfReachException e){
            //throw the exception in order to be handled by the Controller object
            throw new LocationOutOfReachException();
        }
        weatherData.location = latLonInfo.address;

        return weatherData;
    }

}
