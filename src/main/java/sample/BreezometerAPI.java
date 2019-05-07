package sample;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class BreezometerAPI {
    private static final int nextDays = 2;
    String key;
    String latitude;
    String longitude;
    BreezometerAPI(String key, String latitude, String longitude){
        this.key = key;
        this.latitude =latitude;
        this.longitude = longitude;
    }

    public Integer getCurrentAirQuality(){
        String AQCurrent = "https://api.breezometer.com/air-quality/v2/current-conditions?lat={lat}&lon={lon}&key={key}";
        AQCurrent = AQCurrent.replace("{lat}", latitude);
        AQCurrent = AQCurrent.replace("{lon}", longitude);
        AQCurrent = AQCurrent.replace("{key}", key);
        try{
            URL url =  new URL(AQCurrent);
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
            JSONObject response = new JSONObject(json_response.toString());
            con.disconnect();
            return response.getJSONObject("data").getJSONObject("indexes").getJSONObject("baqi").getInt("aqi");

        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("URL errors");
            return null;
        }
    }

    public Map<String, Integer> getFutureAirQuality(){
        String AQFutureURL = "https://api.breezometer.com/air-quality/v2/historical/hourly?lat={lat}&lon={lon}&key={key}";
        AQFutureURL = AQFutureURL.replace("{lat}", latitude);
        AQFutureURL = AQFutureURL.replace("{lon}", longitude);
        AQFutureURL = AQFutureURL.replace("{key}", key);
        AQFutureURL += "&hours=" + String.valueOf(24 * nextDays);
        try{
            URL url = new URL(AQFutureURL);
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
            JSONObject response = new JSONObject(json_response.toString());
            con.disconnect();

            JSONArray dataArray = response.getJSONArray("data");


            Calendar old = Calendar.getInstance();
            Calendar current = Calendar.getInstance();
            old.set(Calendar.HOUR_OF_DAY, 0);
            current.set(Calendar.HOUR_OF_DAY, current.get(Calendar.HOUR_OF_DAY) - 2);
            String oldDate = formatter.format(old.getTime());
            String currentDate = formatter.format(current.getTime());
            oldDate += "T00:00:00";
            currentDate += "T" + current.get(Calendar.HOUR_OF_DAY) + ":00:00";






        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error with URL");
        }

        return null;

    }

    public static void main(String[] args){
//        String key = "39c2d17a2ceb411796fe9b63c79d1197";
//        String latitude = "43.3616211";
//        String longitude = "-80.3144276";
//        String AQFutureURL = "https://api.breezometer.com/air-quality/v2/historical/hourly?lat={lat}&lon={lon}&key={key}";
//        AQFutureURL = AQFutureURL.replace("{lat}", latitude);
//        AQFutureURL = AQFutureURL.replace("{lon}", longitude);
//        AQFutureURL = AQFutureURL.replace("{key}", key);
//        AQFutureURL += "&hours=" + String.valueOf(2);
//        System.out.println(AQFutureURL);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Calendar old = Calendar.getInstance();
            Calendar current = Calendar.getInstance();
            old.set(Calendar.HOUR_OF_DAY, 0);
            current.set(Calendar.HOUR_OF_DAY, current.get(Calendar.HOUR_OF_DAY) - 2);
            String oldDate = formatter.format(old.getTime());
            String currentDate = formatter.format(current.getTime());
            oldDate += "T00:00:00";
            currentDate += "T" + current.get(Calendar.HOUR_OF_DAY) + ":00:00";


            String key = "39c2d17a2ceb411796fe9b63c79d1197";
            String latitude = "43.3616211";
            String longitude = "-80.3144276";
            String AQFutureURL = "https://api.breezometer.com/air-quality/v2/historical/hourly?lat={lat}&lon={lon}&key={key}";
            AQFutureURL = AQFutureURL.replace("{lat}", latitude);
            AQFutureURL = AQFutureURL.replace("{lon}", longitude);
            AQFutureURL = AQFutureURL.replace("{key}", key);
            AQFutureURL += "&start_datetime=" + oldDate + "&end_datetime=" + currentDate;
            System.out.println(AQFutureURL);
        }
        catch(Exception e){e.printStackTrace();}

    }
}
