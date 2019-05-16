package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import sample.api.WeatherData;

public class Controller {

    private WeatherData weatherData;
    private boolean isDarkMode = false;
    private boolean isFeelTemp = false;

    // 0 -> Today, 1 -> Tomorrow, 2 -> The day after tomorrow
    private int currentlyDisplayedDay = 1;

    @FXML
    Label NotifictionLabel;

    @FXML
    ToggleButton lowerToggle;

    @FXML
    Label mainTempLabel;

    @FXML
    LineChart lineChartTemp;

    @FXML
    BarChart barChartPrecip;

    @FXML
    NumberAxis barChartYAxis;

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

        // Populate temperature graph
        XYChart.Series<String, Double> lineSeries = new XYChart.Series<>();
        for (int i = 0; i < 24; i++) {
            String label = i+":00";
            lineSeries.getData().add(new XYChart.Data<>(label, weatherData.temperatureForecast[currentlyDisplayedDay][i]));
        }
        lineChartTemp.getData().clear();
        lineChartTemp.getData().add(lineSeries);

        // Populate bar chart
        barChartYAxis.setAutoRanging(false);
        barChartYAxis.setUpperBound(1);
        XYChart.Series<String, Double> barSeries = new XYChart.Series<>();
        for (int i = 0; i < 24; i++) {
            String label = i+":00";
            barSeries.getData().add(new XYChart.Data<>(label, weatherData.precipitationForecast[currentlyDisplayedDay][i]));
        }
        barChartPrecip.getData().clear();
        barChartPrecip.getData().add(barSeries);
        barChartPrecip.setBarGap(0);
        barChartPrecip.setCategoryGap(0);
    }

}
