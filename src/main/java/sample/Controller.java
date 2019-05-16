package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import sample.api.DataQuery;
import sample.api.WeatherData;
import javafx.scene.control.TextField;

import java.awt.*;

public class Controller {

    private WeatherData weatherData;
    private boolean isDarkMode = false;
    private boolean isFeelTemp = false;

    @FXML
    Label NotificationLabel;

    @FXML
    TextField cityName;

    @FXML
    ToggleButton lowerToggle;

    @FXML
    Button refreshButton;

    @FXML
    Label mainTempLabel;

    @FXML
    Label windSpeed;

    @FXML
    Label uvIndex;

    @FXML
    Label airQuality;

    @FXML
    Label pollenCount;

    @FXML
    protected void ClickMeHandler(Event event) {
        System.out.println(lowerToggle.isSelected());
    }

    @FXML
    protected void refreshHandler(Event event){
        WeatherData newWeatherData = DataQuery.queryData(cityName.getText());
        this.updateWeatherData(newWeatherData);


    }

    public void updateWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
        mainTempLabel.setText(Math.round(weatherData.currentTemperature) + " " + "\u00B0C");


    }


}
