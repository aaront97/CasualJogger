package sample.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class DarkSkyData {

    public CurrentWeatherSnapshot currently;
    public ArrayList<MinutelyWeatherSnapshot> minutely;
    public ArrayList<HourlyWeatherSnapshot> hourly;
    public ArrayList<DailyWeatherSnapshot> daily;
    public ArrayList<HourlyWeatherSnapshot> previous = new ArrayList<>();

    DarkSkyData(JSONObject jsonObject) throws LocationOutOfReachException {

        try {
            currently = new CurrentWeatherSnapshot(jsonObject.getJSONObject("currently"));

            {
                minutely = new ArrayList<>();
                JSONArray minutelyJson = jsonObject.getJSONObject("minutely").getJSONArray("data");
                if(minutelyJson.length() == 0){
                    throw new LocationOutOfReachException();
                }


                int len = minutelyJson.length();
                for (int i = 0; i < len; i++) {
                    minutely.add(new MinutelyWeatherSnapshot(minutelyJson.getJSONObject(i)));
                }
            }

            {
                hourly = new ArrayList<>();
                JSONArray hourlyJson = jsonObject.getJSONObject("hourly").getJSONArray("data");
                int len = hourlyJson.length();
                for (int i = 0; i < len; i++) {
                    hourly.add(new HourlyWeatherSnapshot(hourlyJson.getJSONObject(i)));
                }
            }

            {
                daily = new ArrayList<>();
                JSONArray dailyJson = jsonObject.getJSONObject("daily").getJSONArray("data");
                int len = dailyJson.length();
                for (int i = 0; i < len; i++) {
                    daily.add(new DailyWeatherSnapshot(dailyJson.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            throw new LocationOutOfReachException();
        }

    }

    public void addPrevious(JSONObject jsonObject) {
        // Should contain
        JSONArray jsonArray = jsonObject.getJSONObject("hourly").getJSONArray("data");
        Timestamp ts = new Timestamp(new Date().getTime());
        int numberOfHours = (int) (ts.getTime() / (1000*60*60)) % 24;
        System.out.println("Number of hours in previous: " + numberOfHours);
        for (int i = 0; i <= numberOfHours; i++) {
            previous.add(new HourlyWeatherSnapshot(jsonArray.getJSONObject(i)));
        }
    }

    @Override
    public String toString() {
        Field[] fields = getClass().getFields();
        StringBuilder s = new StringBuilder("{ ");
        for (Field field : fields) {
            try {
                s.append(field.getName());
                s.append(" = ");
                s.append(field.get(this));
                s.append(" , ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        s.append("}");
        return s.toString();
    }
}