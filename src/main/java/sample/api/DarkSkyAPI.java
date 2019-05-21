package sample.api;

import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Queries the DarkSky API.
 * Used by DataQuery.
 */
public class DarkSkyAPI {

    // Root of the DarkSky API forecast
    private static final String address = "https://api.darksky.net/forecast/";

    /**
     * @param darkSkyKey API key from apiKeys.json  =>  NEVER PUSH TO GIT!!!
     * @param latLonInfo Co-ordinates of location (given by Google API)
     * @return A DarkSkyData object, containing all the relevant data from the API
     * @throws LocationOutOfReachException If latLonInfo is outside of supported area
     */
    public static DarkSkyData getDarkSkyData(String darkSkyKey, LatLonInfo latLonInfo) throws LocationOutOfReachException {

        // Parameter sanitising
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

        //Adding flags: need to extend query to cover 3 days + units should be UK format
        String completeAddress = addressParameters + "?extend=hourly&units=uk2";

        try {
            // Query forecast
            System.out.println("Complete Address: " + completeAddress);
            JSONObject response = APIUtils.getJsonFromUrl(completeAddress);

            // Build up Java object from JSON
            DarkSkyData darkSkyData = new DarkSkyData(response);

            // For the past hours
            Timestamp ts = new Timestamp(new Date().getTime());

            // Queries all previous entries in the day containing the current timestamp
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