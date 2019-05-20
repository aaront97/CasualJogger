package sample.api;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WeatherData {

    private static final HashMap<Integer, String> pollenLookupTable = new HashMap<>();
    public String location;

    public int timestamp;

    // Data fields
    public double currentTemperature;
    public double currentApparentTemperature;

    // Minutely
    public double[] immediatePrecipitationForecast = new double[60];

    // 0 -> today, 1-> tomorrow, 2-> day after tomorrow
    public double[][] temperatureForecast = new double[3][24];
    public double[][] apparentTemperatureForecast = new double[3][24];
    public double[][] precipitationForecast = new double[3][24];

    public double currentWindSpeed;
    public double currentWindBearing;
    public int currentUV;
    public double currentAQI;
    public String currentPollen;


    // 0 -> tomorrow, 1 -> day after tomorrow (no today!!!)
    public double[] maxWindSpeedForecast = new double[2];
    public int[] maxWindSpeedTime = new int[2];
    public double[] minWindSpeedForecast = {10000, 10000};
    public int[] minWindSpeedTime = new int[2];
    public int[] maxUVForecast = new int[2];
    public int[] maxUVTime = new int[2];
    public int[] maxAQIForecast = new int[2];
    public String[] maxAQITime = new String[2];
    public int[] minAQIForecast = new int[2];
    public String[] minAQITime = new String[2];

    //we have no time for pollen - only one data reading per day
    //pollen arranged to qualitative values per day
    public String[] pollen = new String[2];

    // 0 -> today, 1-> tomorrow, 2-> day after tomorrow
    public int[] sunriseTimes = new int[3];
    public int[] sunsetTimes = new int[3];

    WeatherData(DarkSkyData darkSkyData, BreezometerAPI breezometerData) throws LocationOutOfReachException {
        timestamp = darkSkyData.currently.time;
        currentTemperature = darkSkyData.currently.temperature;
        currentApparentTemperature = darkSkyData.currently.apparentTemperature;
        currentWindSpeed = darkSkyData.currently.windSpeed;
        currentWindBearing = darkSkyData.currently.windBearing;
        currentUV = darkSkyData.currently.uvIndex;

        //BREEZOMETER COMMENTED DAILY LIMIT EXCEEDED

        currentAQI = breezometerData.getCurrentAirQuality();
        List<BreezometerRecord> records = breezometerData.getFutureAirQuality();
        //assumes order of linked list
        for(int i = 0; i < records.size(); i++){
            if(records.get(i).key.equals("max")){
                maxAQITime[i] = records.get(i).datetime;
                maxAQIForecast[i] = records.get(i).value;
            }
            else{
                minAQITime[i - minAQITime.length] = records.get(i).datetime;
                minAQIForecast[i-minAQIForecast.length] = records.get(i).value;
            }
        }

        //POLLEN
        pollenLookupTable.put(1, "Very Low");
        pollenLookupTable.put(2, "Low");
        pollenLookupTable.put(3, "Moderate");
        pollenLookupTable.put(4, "High");
        pollenLookupTable.put(5, "Very High");

        List<BreezometerRecord> pollenReadings = breezometerData.getPollenCount();
        for(int i = 0; i < pollenReadings.size(); i++){
            int value = pollenReadings.get(i).value;
            String result;
            if(value < 1){
                result = "Negligible";
            }
            else{
                result = pollenLookupTable.getOrDefault(value, "Very High");
            }

            if(i == 0 ){
                currentPollen = result;
            }
            else{
                pollen[i - 1] = result;
            }

        }
        //END OF BREEZOMETER

        if(darkSkyData.minutely.size() == 0){
            throw new LocationOutOfReachException();
        }

        for (int i = 0; i < 60; i++) {
            immediatePrecipitationForecast[i] = darkSkyData.minutely.get(i).precipProbability;
        }



        int[] cur = {0};    // Stupid lambda-expressions and final variables...
        darkSkyData.previous.forEach((HourlyWeatherSnapshot snapshot) -> {
            temperatureForecast[0][cur[0]] = snapshot.temperature;
            apparentTemperatureForecast[0][cur[0]] = snapshot.apparentTemperature;
            precipitationForecast[0][cur[0]] = snapshot.precipProbability;
            cur[0]++;
        });
        int prevRecord = cur[0];
        darkSkyData.hourly.forEach((HourlyWeatherSnapshot snapshot) -> {
            if (cur[0] < 3*24) {
                temperatureForecast[cur[0] / 24][cur[0] % 24] = snapshot.temperature;
                apparentTemperatureForecast[cur[0] / 24][cur[0] % 24] = snapshot.apparentTemperature;
                precipitationForecast[cur[0] / 24][cur[0] % 24] = snapshot.precipProbability;
            }
            cur[0]++;
        });

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 24; j++) {
                HourlyWeatherSnapshot snapshot = darkSkyData.hourly.get(24 - prevRecord + 24*i + j);
                if (snapshot.windSpeed > maxWindSpeedForecast[i]) {
                    maxWindSpeedForecast[i] = snapshot.windSpeed;
                    maxWindSpeedTime[i] = snapshot.time;
                }
                if (snapshot.windSpeed < minWindSpeedForecast[i]) {
                    minWindSpeedForecast[i] = snapshot.windSpeed;
                    minWindSpeedTime[i] = snapshot.time;
                }
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
