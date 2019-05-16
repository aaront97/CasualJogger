package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import sample.api.DataQuery;
import sample.api.WeatherData;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Controller {

    private WeatherData weatherData;
    private boolean isDarkMode = false;
    private boolean isFeelTemp = false;

    // 0 -> Today, 1 -> Tomorrow, 2 -> The day after tomorrow
    private int currentlyDisplayedDay = 2;

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
    LineChart lineChartTemp;

    @FXML
    BarChart barChartPrecip;

    @FXML
    NumberAxis barChartYAxis;

    @FXML
    Label chartDayLabel;

    @FXML
    RadioButton todayButton;

    @FXML
    RadioButton tomorrowButton;

    @FXML
    RadioButton dayAfterTomorrowButton;

    @FXML
    Label windSpeed;

    @FXML
    Label uvIndex;

    @FXML
    Label airQuality;

    @FXML
    Label pollenCount;

    @FXML
    ImageView windBearing;

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

        // Setting the label above the graph to today
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 2);
        String[] possibleTexts = {"Today", "Tomorrow", c.get(Calendar.DAY_OF_MONTH) + " " +
                c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK)};
        chartDayLabel.setText(possibleTexts[currentlyDisplayedDay]);

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


        windSpeed.setText(Math.round(weatherData.currentWindSpeed) + "mph");
        windBearing.setRotate(windBearing.getRotate() + weatherData.currentWindBearing);
        uvIndex.setText(weatherData.currentUV + "");
        airQuality.setText(Math.round(weatherData.currentAQI) + "");
        pollenCount.setText(weatherData.currentPollen);

    }

    @FXML
    protected void todayButtonClicked() {
        if (currentlyDisplayedDay != 0) {
            currentlyDisplayedDay = 0;
            tomorrowButton.setSelected(false);
            dayAfterTomorrowButton.setSelected(false);
            updateWeatherData(weatherData);
        }
        todayButton.setSelected(true);
    }

    @FXML
    protected void tomorrowButtonClicked() {
        if (currentlyDisplayedDay != 1) {
            currentlyDisplayedDay = 1;
            todayButton.setSelected(false);
            dayAfterTomorrowButton.setSelected(false);
            updateWeatherData(weatherData);
        }
        tomorrowButton.setSelected(true);
    }

    @FXML
    protected void dayAfterTomorrowButtonClicked() {
        if (currentlyDisplayedDay != 2) {
            currentlyDisplayedDay = 2;
            todayButton.setSelected(false);
            tomorrowButton.setSelected(false);
            updateWeatherData(weatherData);
        }
        dayAfterTomorrowButton.setSelected(true);
    }
}
