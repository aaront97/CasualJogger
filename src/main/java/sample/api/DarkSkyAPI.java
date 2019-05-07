package sample;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DarkSkyAPI {

    private static final String address = "https://api.darksky.net/forecast/";

    public static DarkSkyData getDarkSkyData(String darkSkyKey, LatLonInfo latLonInfo) {

        if (darkSkyKey == null) {
            System.err.println("API key not found for DarkSky");
            return null;
        }

        if (latLonInfo == null) {
            System.err.println("No latitude, longitude info found");
            return null;
        }

        // Basic query
        String completeAddress = address + darkSkyKey + "/" + latLonInfo.lat + "," + latLonInfo.lon;

        //Adding flags
        completeAddress += "?extend=hourly&units=uk2";

        try {
            System.out.println("Complete Address: " + completeAddress);
            URL url = new URL(completeAddress);
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
            JSONObject response = new JSONObject(json_response.toString());
            return new DarkSkyData(response);
        } catch (Exception e) {
            System.err.println("Something went wrong while looking up Dark Sky");
            e.printStackTrace();
        }

        return null;
    }
}
