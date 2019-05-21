package sample.api;

import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Contains weather data about one hour.
 * Used by DarkSkyAPI.
 */
public class HourlyWeatherSnapshot {

    public int time;        // In UNIX timestamps
    public String summary;
    public String icon;
    public double precipIntensity;
    public double precipProbability;
    public String precipType;
    public double temperature;
    public double apparentTemperature;
    public double windSpeed;
    public double windBearing;
    public double cloudCover;
    public int uvIndex;
    public double visibility;

    /**
     * Populates the data fields from the given JSON object.     *
     * @param jsonObject Data source in JSONFormat
     */
    HourlyWeatherSnapshot(JSONObject jsonObject) {
        time = APIUtils.tryToGetInt(jsonObject, "time");
        summary = APIUtils.tryToGetString(jsonObject, "summary");
        icon = APIUtils.tryToGetString(jsonObject, "icon");
        precipIntensity = APIUtils.tryToGetDouble(jsonObject, "precipIntensity");
        precipProbability = APIUtils.tryToGetDouble(jsonObject, "precipProbability");
        precipType = APIUtils.tryToGetString(jsonObject, "precipType");
        temperature = APIUtils.tryToGetDouble(jsonObject, "temperature");
        apparentTemperature = APIUtils.tryToGetDouble(jsonObject, "apparentTemperature");
        windSpeed = APIUtils.tryToGetDouble(jsonObject, "windSpeed");
        windBearing = APIUtils.tryToGetDouble(jsonObject, "windBearing");
        cloudCover = APIUtils.tryToGetDouble(jsonObject, "cloudCover");
        uvIndex = APIUtils.tryToGetInt(jsonObject, "uvIndex");
        visibility = APIUtils.tryToGetDouble(jsonObject, "visibility");
    }

    /**
     * Used for printing out the data contained in the object.
     * Useful for debugging. System.out.println() uses this form.
     */
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