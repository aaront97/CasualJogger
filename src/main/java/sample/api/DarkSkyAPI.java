package sample.api;

import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

public class DarkSkyAPI {

    private static final String address = "https://api.darksky.net/forecast/";

    public static DarkSkyData getDarkSkyData(String darkSkyKey, LatLonInfo latLonInfo) throws LocationOutOfReachException {

        if (darkSkyKey == null) {
            System.err.println("API key not found for DarkSky");
            return null;
        }

        if (latLonInfo == null) {
            System.err.println("No latitude, longitude info found");
            return null;
        }

        // Basic query
        String addressParameters = address + darkSkyKey + "/" + latLonInfo.lat + "," + latLonInfo.lon;

        //Adding flags
        String completeAddress = addressParameters + "?extend=hourly&units=uk2";

        try {
            System.out.println("Complete Address: " + completeAddress);
            JSONObject response = APIUtils.getJsonFromUrl(completeAddress);

            DarkSkyData darkSkyData = new DarkSkyData(response);

            // For previous data
            Timestamp ts = new Timestamp(new Date().getTime());
            String previousDataAddress = addressParameters+ "," + ts.getTime() / 1000;
            previousDataAddress += "?exclude=currently&units=uk2";
            System.out.println(previousDataAddress);
            response = APIUtils.getJsonFromUrl(previousDataAddress);
            darkSkyData.addPrevious(response);

            return darkSkyData;

        }
        catch(LocationOutOfReachException e){
            throw new LocationOutOfReachException();
        }
        catch (IOException e) {
            System.err.println("Something went wrong while looking up Dark Sky");
            e.printStackTrace();
            return null;
        }
    }
}
