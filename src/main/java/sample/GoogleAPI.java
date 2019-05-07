package sample;


import org.json.JSONArray;
import org.json.JSONObject;
import sun.misc.IOUtils;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

public class GoogleAPI {

    private static final String address = "https://maps.googleapis.com/maps/api/geocode/json";
    private static String apiKey ;

    public static Map<String, String> getCoord(String city) {
        Map<String, String> result = new HashMap<>();
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


            result.put("latitude", latitude);
            result.put("longitude", longitude);
            result.put("address", formatted_address);
            return result;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void main(String[] args) {
        Map<String, String> s = getCoord("Pennsylvania, US");
        System.out.println(s);
    }
}