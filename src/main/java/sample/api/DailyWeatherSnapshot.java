package sample.api;

import org.json.JSONObject;

public class DailyWeatherSnapshot {

    public int time;
    public String summary;
    public String icon;
    public int sunriseTime;
    public int sunsetTime;
    public double precipIntensity;
    public double precipProbability;
    public String precipType;
    public double temperatureHigh;
    public int temperatureHighTime;
    public double temperatureLow;
    public int temperatureLowTime;
    public double apparentTemperatureHigh;
    public int apparentTemperatureHighTime;
    public double apparentTemperatureLow;
    public int apparentTemperatureLowTime;
    public double windSpeed;
    public double windBearing;
    public double cloudCover;
    public int uvIndex;
    public int uvIndexTime;
    public double visibility;

    DailyWeatherSnapshot(JSONObject jsonObject) {
        time = APIUtils.tryToGetInt(jsonObject, "time");
        summary = APIUtils.tryToGetString(jsonObject, "summary");
        icon = APIUtils.tryToGetString(jsonObject, "icon");
        precipIntensity = APIUtils.tryToGetDouble(jsonObject, "precipIntensity");
        precipProbability = APIUtils.tryToGetDouble(jsonObject, "precipProbability");
        precipType = APIUtils.tryToGetString(jsonObject, "precipType");
        windSpeed = APIUtils.tryToGetDouble(jsonObject, "windSpeed");
        windBearing = APIUtils.tryToGetDouble(jsonObject, "windBearing");
        cloudCover = APIUtils.tryToGetDouble(jsonObject, "cloudCover");
        uvIndex = APIUtils.tryToGetInt(jsonObject, "uvIndex");
        visibility = APIUtils.tryToGetDouble(jsonObject, "visibility");
        sunriseTime = APIUtils.tryToGetInt(jsonObject,"sunriseTime");
        sunsetTime = APIUtils.tryToGetInt(jsonObject, "sunsetTime");
        temperatureHigh = APIUtils.tryToGetDouble(jsonObject, "temperatureHigh");
        temperatureHighTime = APIUtils.tryToGetInt(jsonObject, "temperatureHighTime");
        temperatureLow = APIUtils.tryToGetDouble(jsonObject, "temperatureLow");
        temperatureLowTime = APIUtils.tryToGetInt(jsonObject, "temperatureLowTime");
        apparentTemperatureHigh = APIUtils.tryToGetDouble(jsonObject, "apparentTemperatureHigh");
        apparentTemperatureHighTime = APIUtils.tryToGetInt(jsonObject, "apparentTemperatureHighTime");
        apparentTemperatureLow = APIUtils.tryToGetDouble(jsonObject, "apparentTemperatureLow");
        apparentTemperatureLowTime = APIUtils.tryToGetInt(jsonObject, "apparentTemperatureLowTime");
        uvIndexTime = APIUtils.tryToGetInt(jsonObject, "uvIndexTime");
    }
}
