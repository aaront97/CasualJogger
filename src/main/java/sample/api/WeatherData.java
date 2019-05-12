package sample.api;

import java.lang.reflect.Field;
import java.util.Arrays;

public class WeatherData {

    public int timestamp;

    // Data fields
    public double currentTemperature;
    public double currentApparentTemperature;

    // Minutely
    public double[] immediatePrecipitationForecast = new double[60];

    // 0 -> today, 1-> tomorrow, 2-> day after tomorrow
    public double[][] temperatureForecast = new double[3][24];
    public double[][] precipitationForecast = new double[3][24];

    public double currentWindSpeed;
    public double currentWindBearing;
    public int currentUV;
    public double currentAQI;
    // TODO Pollen

    // 0 -> tomorrow, 1 -> day after tomorrow (no today!!!)
    public double[] maxWindSpeedForecast = new double[2];
    public double[] minWindSpeedForecast = {10000, 10000};
    public int[] maxUVForecast = new int[2];
    public int[] maxUVTime = new int[2];
    public int[] maxAQIForecast = new int[2];
    public int[] maxAQITime = new int[2];
    public int[] minAQIForecast = new int[2];
    public int[] minAQITime = new int[2];
    // TODO pollen

    // 0 -> today, 1-> tomorrow, 2-> day after tomorrow
    public int[] sunriseTimes = new int[3];
    public int[] sunsetTimes = new int[3];

    WeatherData(DarkSkyData darkSkyData, BreezometerAPI breezometerRecord) {
        timestamp = darkSkyData.currently.time;
        currentTemperature = darkSkyData.currently.temperature;
        currentApparentTemperature = darkSkyData.currently.apparentTemperature;
        currentWindSpeed = darkSkyData.currently.windSpeed;
        currentWindBearing = darkSkyData.currently.windBearing;
        currentUV = darkSkyData.currently.uvIndex;
        // TODO AQI

        for (int i = 0; i < 60; i++) {
            immediatePrecipitationForecast[i] = darkSkyData.minutely.get(i).precipProbability;
        }

        int[] cur = {0};    // Stupid lambda-expressions and final variables...
        darkSkyData.previous.forEach((HourlyWeatherSnapshot snapshot) -> {
            temperatureForecast[0][cur[0]] = snapshot.temperature;
            precipitationForecast[0][cur[0]] = snapshot.precipProbability;
            cur[0]++;
        });
        int prevRecord = cur[0];
        darkSkyData.hourly.forEach((HourlyWeatherSnapshot snapshot) -> {
            if (cur[0] < 3*24) {
                temperatureForecast[cur[0] / 24][cur[0] % 24] = snapshot.temperature;
                precipitationForecast[cur[0] / 24][cur[0] % 24] = snapshot.precipProbability;
            }
            cur[0]++;
        });

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 24; j++) {
                HourlyWeatherSnapshot snapshot = darkSkyData.hourly.get(24 - prevRecord + 24*i + j);
                maxWindSpeedForecast[i] = Math.max(maxWindSpeedForecast[i], snapshot.windSpeed);
                minWindSpeedForecast[i] = Math.min(minWindSpeedForecast[i], snapshot.windSpeed);
            }
        }

        for (int i = 0; i < 3; i++) {
            DailyWeatherSnapshot snapshot = darkSkyData.daily.get(i);
            sunriseTimes[i] = snapshot.sunriseTime;
            sunsetTimes[i] = snapshot.sunsetTime;
            if (i > 0) {
                maxUVForecast[i-1] = snapshot.uvIndex;
                maxUVTime[i-1] = snapshot.uvIndexTime;
            }
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
                if (field.getType().isArray()) {
                    if (field.getType().getComponentType().isArray()) {
                        s.append(Arrays.deepToString((Object[]) field.get(this)));
                    } else {
                        try {
                            s.append(Arrays.toString((double[]) field.get(this)));
                        } catch (Exception e) {}
                        try {
                            s.append(Arrays.toString((int[]) field.get(this)));
                        } catch (Exception e) {}
                        try {
                            s.append(Arrays.toString((Object[]) field.get(this)));
                        } catch (Exception e) {}
                    }
                } else {
                    s.append(field.get(this));
                }
                s.append(" , ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        s.append("}");
        return s.toString();
    }

}
