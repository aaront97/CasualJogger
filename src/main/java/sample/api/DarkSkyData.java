package sample.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class DarkSkyData {

    public CurrentWeatherSnapshat currently;
    public ArrayList<MinutelyWeatherSnapshot> minutely;
    public ArrayList<HourlyWeatherSnapshot> hourly;
    public ArrayList<DailyWeatherSnapshot> daily;

    DarkSkyData(JSONObject jsonObject) {

        try {
            currently = new CurrentWeatherSnapshat(jsonObject.getJSONObject("currently"));

            {
                minutely = new ArrayList<>();
                JSONArray minutelyJson = jsonObject.getJSONObject("minutely").getJSONArray("data");
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
        } catch (Exception e) {
            System.err.println("Something went wrong while parsing DarkSky response JSON");
            e.printStackTrace();
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
