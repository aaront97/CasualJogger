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

    public LatLonInfo getCoord(String city) {
        if (apiKey == null) {
            try {
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

            JSONObject properties = response.getJSONArray("results").getJSONObject(0);
            String latitude = String.valueOf(
                    properties.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
            String longitude = String.valueOf(
                    properties.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
            String formatted_address = properties.getString("formatted_address");

            LatLonInfo result = new LatLonInfo();
            result.lat = Double.parseDouble(latitude);
            result.lon = Double.parseDouble(longitude);
            result.address = formatted_address;
            return result;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }

    }


}