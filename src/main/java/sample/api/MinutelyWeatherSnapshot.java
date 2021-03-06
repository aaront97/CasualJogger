package sample.api;

import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Contains weather data about one minute.
 * Only for the next 60 mins.
 * Used by DarkSkyAPI.
 */
public class MinutelyWeatherSnapshot {

    public int time;        // UNIX timestamp
    public double precipIntensity;
    public double precipProbability;
    public String precipType;

    /**
     * Populates the data fields from the given JSON object.
     * @param jsonObject Data source in JSONFormat
     */
    MinutelyWeatherSnapshot(JSONObject jsonObject) {
        time = APIUtils.tryToGetInt(jsonObject, "time");
        precipIntensity = APIUtils.tryToGetDouble(jsonObject, "precipIntensity");
        precipProbability = APIUtils.tryToGetDouble(jsonObject, "precipProbability");
        precipType = APIUtils.tryToGetString(jsonObject, "precipType");
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