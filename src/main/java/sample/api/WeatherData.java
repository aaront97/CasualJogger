package sample.api;

import java.lang.reflect.Field;

public class WeatherData {

    public int timestamp;

    // Data fields
    public double currentTemperature;
    public double currentApperantTemperature;

    public double[] immediatePrecipitationForecast;

    // 0 -> today, 1-> tomorrow, 2-> day after tomorrow
    public double[][] temperatureForecast;
    public double[][] percipitationForecast;

    public double currentWindSpeed;
    public double currentWindBearing;
    public int currentUV;
    public double currentAQI;
    // TODO Pollen

    // 0 -> tomorrow, 1 -> day after tomorrow (no today!!!)
    public double[] maxWindSpeedForecast;
    public double[] minWindSpeedForecast;
    public int[] maxUVForecast;
    public int[] maxUVTime;
    public int[] maxAQIForecast;
    public int[] maxAQITime;
    public int[] minAQIForecast;
    public int[] minAQITime;
    // TODO pollen

    // 0 -> today, 1-> tomorrow, 2-> day after tomorrow
    public int[] sunriseTimes;
    public int[] sunsetTimes;

    WeatherData(DarkSkyData darkSkyData, BreezometerRecord breezometerRecord) {
        // TODO
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
