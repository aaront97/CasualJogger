package sample.api;

import org.json.JSONObject;

public class MinutelyWeatherSnapshot {

    public int time;
    public double precipIntensity;
    public double precipProbability;
    public String precipType;

    MinutelyWeatherSnapshot(JSONObject jsonObject) {
        time = APIUtils.tryToGetInt(jsonObject, "time");
        precipIntensity = APIUtils.tryToGetDouble(jsonObject, "precipIntensity");
        precipProbability = APIUtils.tryToGetDouble(jsonObject, "precipProbability");
        precipType = APIUtils.tryToGetString(jsonObject, "precipType");
    }
}
