package sample.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
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
            JSONObject response = getJSONObject(url);
            return response.getJSONObject("data").getJSONObject("indexes").getJSONObject("baqi").getInt("aqi");

        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("URL errors");
            return null;
        }
    }

    public List<BreezometerRecord> getPollenCount(){
        String PCFuture = "https://api.breezometer.com/pollen/v2/forecast/daily?lat={lat}&lon={lon}&key={key}&days={Number_of_Days}";
        PCFuture = PCFuture.replace("{lat}", latitude);
        PCFuture = PCFuture.replace("{lon}", longitude);
        PCFuture = PCFuture.replace("{key}", key);
        PCFuture = PCFuture.replace("{Number_of_Days}", "3");


        try{
            URL url = new URL(PCFuture);
            JSONObject response = getJSONObject(url);
            JSONArray dataArray = response.getJSONArray("data");
            List<BreezometerRecord> result = new LinkedList<BreezometerRecord>();

            for(int i = 0; i < 3; i++){
                JSONObject object = dataArray.getJSONObject(i);
                String date = object.getString("date");
                JSONObject types = object.getJSONObject("types");
                Iterator<String> iterator = types.keys();
                int total_pc = 0;
                while(iterator.hasNext()){
                    String key = iterator.next();
                    if (types.get(key) instanceof JSONObject) {
                        JSONObject child = (JSONObject)types.get(key);
                        if(child.getBoolean("in_season")){
                            total_pc += child.getJSONObject("index").getInt("value");
                        }
                    }
                }
                result.add(new BreezometerRecord(date, "Pollen", total_pc));
            }
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("URL errors");
            return null;
        }
    }

    public List<BreezometerRecord> getFutureAirQuality(){
        int offset = 3;
        String BaseURL = "https://api.breezometer.com/air-quality/v2/{type}/hourly?lat={lat}&lon={lon}&key={key}";
        BaseURL = BaseURL.replace("{lat}", latitude);
        BaseURL = BaseURL.replace("{lon}", longitude);
        BaseURL = BaseURL.replace("{key}", key);
        String AQFutureURL = BaseURL.replace("{type}", "forecast");
        String AQHistoricalURL = BaseURL.replace("{type}", "historical");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar old = Calendar.getInstance();
        Calendar current = Calendar.getInstance();
        old.set(Calendar.HOUR_OF_DAY, 0);
        current.set(Calendar.HOUR_OF_DAY, current.get(Calendar.HOUR_OF_DAY) - offset);
        String oldDate = formatter.format(old.getTime());
        String currentDate = formatter.format(current.getTime());
        oldDate += "T00:00:00";
        currentDate += "T" + current.get(Calendar.HOUR_OF_DAY) + ":00:00";

        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int nextHours = 72 - (current.get(Calendar.HOUR_OF_DAY) + offset);
        AQFutureURL += "&hours=" + String.valueOf(nextHours);
        AQHistoricalURL += "&start_datetime=" + oldDate + "&end_datetime=" + currentDate;

        try{
            JSONObject historicalResponse = getJSONObject(new URL(AQHistoricalURL));
            JSONObject forecastResponse = getJSONObject(new URL(AQFutureURL));

            JSONArray dataHistorical = historicalResponse.getJSONArray("data");
            JSONArray dataForecast = forecastResponse.getJSONArray("data");

            List<JSONObject> today = new LinkedList<>();
            List<JSONObject> tomorrow = new LinkedList<>();
            List<JSONObject> afterTomorrow = new LinkedList<>();

            int hoursTilMidnight = 24 - currentHour;
            int historicalLength = dataHistorical.length();
            int forecastLength = dataForecast.length();
            int max_size = historicalLength + forecastLength;
            for(int i = 0; i < max_size; i++){
                if(i < dataHistorical.length()){
                    today.add(dataHistorical.getJSONObject(i));
                }
                else if(i - historicalLength < hoursTilMidnight ){
                    today.add(dataForecast.getJSONObject(i-historicalLength));
                }
                else if(i - historicalLength - hoursTilMidnight < 24){
                    tomorrow.add(dataForecast.getJSONObject(i-historicalLength-hoursTilMidnight));
                }
                else{
                    afterTomorrow.add(dataForecast.getJSONObject(i-historicalLength-hoursTilMidnight));
                }
            }

            BreezometerRecord maxToday = new BreezometerRecord("0", "0", Integer.MIN_VALUE);
            BreezometerRecord maxTomorrow = new BreezometerRecord("0", "0", Integer.MIN_VALUE);
            BreezometerRecord maxAfterTomorrow = new BreezometerRecord("0", "0", Integer.MIN_VALUE);

            for(JSONObject object : today){
                int value = object.getJSONObject("indexes").getJSONObject("baqi").getInt("aqi");
                if(value > maxToday.value){
                    maxToday = new BreezometerRecord(object.getString("datetime"), "AQ", value );
                }
            }
            for(JSONObject object : tomorrow){
                int value = object.getJSONObject("indexes").getJSONObject("baqi").getInt("aqi");
                if(value > maxTomorrow.value){
                    maxTomorrow = new BreezometerRecord(object.getString("datetime"), "AQ", value );
                }
            }
            for(JSONObject object : afterTomorrow){
                int value = object.getJSONObject("indexes").getJSONObject("baqi").getInt("aqi");
                if(value > maxAfterTomorrow.value){
                    maxAfterTomorrow = new BreezometerRecord(object.getString("datetime"), "AQ", value );
                }
            }
            List<BreezometerRecord> result = new LinkedList<>();
            result.add(maxToday);
            result.add(maxTomorrow);
            result.add(maxAfterTomorrow);
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error with URL");
        }
        return null;
    }



    public JSONObject getJSONObject(URL url) throws ProtocolException, IOException {
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
        return response;
    }

//    public static void main(String[] args){
//
////        String AQFutureURL = "https://api.breezometer.com/air-quality/v2/historical/hourly?lat={lat}&lon={lon}&key={key}";
////        AQFutureURL = AQFutureURL.replace("{lat}", latitude);
////        AQFutureURL = AQFutureURL.replace("{lon}", longitude);
////        AQFutureURL = AQFutureURL.replace("{key}", key);
////        AQFutureURL += "&hours=" + String.valueOf(2);
//
//        BreezometerAPI test = new BreezometerAPI(key, latitude, longitude);
//        for(BreezometerRecord r: test.getPollenCount()){
//            System.out.println(r);
//        }
//
//
//    }
}
