package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import sample.api.WeatherData;

public class Controller {

    private WeatherData weatherData;
    private boolean isDarkMode = false;
    private boolean isFeelTemp = false;

    @FXML
    Label NotifictionLabel;

    @FXML
    ToggleButton lowerToggle;

    @FXML
    Label mainTempLabel;

    @FXML
    protected void ClickMeHandler(Event event) {
        System.out.println(lowerToggle.isSelected());
//        Window owner = bigTextArea.getScene().getWindow();
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("No more cookies :(");
//        alert.setContentText("Unfortunately, we have ran out of cookies. Apologies.");
//        alert.initOwner(owner);
//        alert.show();
    }

    public void updateWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
        mainTempLabel.setText(Math.round(weatherData.currentTemperature) + " " + "\u00B0C");
    }
}
