package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.api.DataQuery;
import sample.api.WeatherData;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Controller {

    private WeatherData weatherData;
    private boolean isNightMode = false;
    private boolean isFeelTemp = false;

    // 0 -> Today, 1 -> Tomorrow, 2 -> The day after tomorrow
    private int currentlyDisplayedDay = 0;

    private Image toggledImage = new Image(getClass().getClassLoader().getResource("images/toggled.png").toString());
    private Image notToggledImage = new Image(getClass().getClassLoader().getResource("images/notToggled.png").toString());

    @FXML
    Label windLabel;

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
    ImageView toggleRealFeel;

    @FXML
    ImageView toggleNightMode;

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

        // Setting the toggles
        if (isNightMode) {
            toggleNightMode.setImage(toggledImage);
        } else {
            toggleNightMode.setImage(notToggledImage);
        }
        if (isFeelTemp) {
            toggleRealFeel.setImage(toggledImage);
        } else {
            toggleRealFeel.setImage(notToggledImage);
        }

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

        if(currentlyDisplayedDay == 0){
            windBearing.setVisible(true);
            windBearing.setTranslateX(-15);
            windBearing.setRotate(weatherData.currentWindBearing);

            windSpeed.setTranslateX(-10);
            windSpeed.setStyle("-fx-font: 20 system;");
            windSpeed.setText(Math.round(weatherData.currentWindSpeed) + "mph");

            uvIndex.setStyle("-fx-font: 20 system;");
            uvIndex.setText(weatherData.currentUV + "");

            boolean apiCallLimitExceeded = weatherData.currentAQI == 0 ? true : false;

            if(apiCallLimitExceeded){
               airQuality.setStyle("-fx-font: 12 system;");
               airQuality.setText("API Call Limit Exceeded");
               pollenCount.setStyle("-fx-font: 12 system;");
               pollenCount.setText("API Call Limit Exceeded");
            }
            else{
                airQuality.setText(Math.round(weatherData.currentAQI) + "");

                pollenCount.setText(weatherData.currentPollen);
            }

        }
        else {
            int index = currentlyDisplayedDay - 1;
            windBearing.setVisible(false);
            windBearing.setTranslateX(-1000);


            windSpeed.setStyle("-fx-font: 12 system;");
            windSpeed.setWrapText(true);

//            String windMax = extractHourFromTimestamp((long)weatherData.maxWindSpeedForecast[index]);
//            String windMin = (long)weatherData.minWindSpeedForecast[index]);
            windSpeed.setText("Max: " + Math.round(weatherData.maxWindSpeedForecast[index]) + "mph at " + "" + " \n" +
                              "Min: " + Math.round(weatherData.minWindSpeedForecast[index]) + "mph at " + "");


            //TODO: MINUV_FORECAST AND MINUV_TIME
            String uvMax = extractHourFromTimestamp((long)weatherData.maxUVTime[index]);
            //String uvMin = String.valueOf(extractHourFromTimestamp((long)weatherData.minUVTime[index]));

            uvIndex.setStyle("-fx-font: 12 system;");
            uvIndex.setWrapText(true);
            uvIndex.setText("Max: " + Math.round(weatherData.maxUVForecast[index]) +" at " + uvMax);

            String aqiMax = weatherData.maxAQITime[index];
            String aqiMin = weatherData.minAQITime[index];
            String pollen = weatherData.pollen[index];

            if(aqiMax == null){
                airQuality.setStyle("-fx-font: 12 system;");
                airQuality.setWrapText(true);
                airQuality.setText("API Call Limit Exceeded");

                pollenCount.setStyle("-fx-font: 12 system;");
                pollenCount.setWrapText(true);
                pollenCount.setText("API Call Limit Exceeded");
            }
            else{
                airQuality.setStyle("-fx-font: 12 system;");
                airQuality.setWrapText(true);
                aqiMax = extractHourFromString(aqiMax);
                aqiMin = extractHourFromString(aqiMin);
                airQuality.setText("Max: " + Math.round(weatherData.maxAQIForecast[index]) + " at " + aqiMax +
                                   "Min: " + Math.round(weatherData.minAQIForecast[index]) + " at " + aqiMin);

                pollenCount.setStyle("-fx-font: 12 system;");
                pollenCount.setWrapText(true);
                pollenCount.setText(pollen);
            }



        }


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

    @FXML
    protected void toggleRealFeel() {
        isFeelTemp = !isFeelTemp;
        updateWeatherData(weatherData);
    }

    @FXML void toggleNightMode() {
        isNightMode = !isNightMode;
        updateWeatherData(weatherData);
    }

    public static String extractHourFromTimestamp(long timestamp){
        Date date = new Date(timestamp*1000);
        int hour = date.toInstant().atZone(ZoneId.systemDefault()).getHour();
        String result = hour <= 12 ? String.valueOf(hour) + " am" : String.valueOf(hour-12) + " pm";
        return result;
    }

    private String extractHourFromString(String date){
        String time = date.split("T")[1];
        int hour = Integer.parseInt(time.substring(0,2));
        String result = hour + " h";
        return result;
    }
}

