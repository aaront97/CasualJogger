package sample.api;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GoogleAPI {

    private static final String address = "https://maps.googleapis.com/maps/api/geocode/json";
    private String apiKey ;

    GoogleAPI(String apiKey){
        this.apiKey = apiKey;
    }

    /**
        @param city : UK city name to be looked up by the GoogleAPI
        returns a LatLonInfo object, which contains the latitude, longitude, and address of the city
     */
    public LatLonInfo getCoord(String city) throws LocationNotFoundException {
        if (apiKey == null) {
            try {
                //try to read API key if API key not specified
                String apiKeysString = new String(
                        Files.readAllBytes(Paths.get(System.getProperty("user.dir") +"\\src\\main\\java\\sample\\apiKeys.json")));
                JSONObject apiKeys = new JSONObject(apiKeysString);
                apiKey = apiKeys.getString("google");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        URL url;
        JSONObject response;
        try{
            //initiating HTTP request to retrieve JSON data from Google
            url = new URL(address + "?address=" + URLEncoder.encode(city, "UTF-8") + "&key=" + apiKey);
            System.out.println(url.toString());
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            StringBuffer json_response = new StringBuffer();
            String line;
            while((line = input.readLine()) != null){
                json_response.append(line);
            }
            response = new JSONObject(json_response.toString());
            con.disconnect();


            String status = response.getString("status");
            System.out.println(status);

            //error-handling code which throws a bad location exception if Google can't find an address
            if(status.equals("ZERO_RESULTS")){
                throw new LocationNotFoundException(city);
            }

            //getting the required data from the API response
            JSONObject properties = response.getJSONArray("results").getJSONObject(0);
            String latitude = String.valueOf(
                    properties.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
            String longitude = String.valueOf(
                    properties.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
            String formatted_address = properties.getString("formatted_address");

            //error-handling code which throws a bad location exception to handle cases such as
            //"asdf" returning a valid location
            if(!formatted_address.contains(city.split(",")[0])){
                throw new LocationNotFoundException(city);
            }

            //packaging the data returned to one object
            LatLonInfo result = new LatLonInfo();
            result.lat = Double.parseDouble(latitude);
            result.lon = Double.parseDouble(longitude);
            result.address = formatted_address;
            return result;
        }
        catch(LocationNotFoundException e) {
            throw new LocationNotFoundException(e.getMessage());
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }


}