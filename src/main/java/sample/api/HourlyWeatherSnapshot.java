package sample.api;

import org.json.JSONObject;

public class HourlyWeatherSnapshot {

    public int time;
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

}
