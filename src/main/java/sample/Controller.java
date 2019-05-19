package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import sample.api.APIReadException;
import sample.api.DataQuery;
import sample.api.LocationNotFoundException;
import sample.api.WeatherData;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
    Label dawnDuskLeft;

    @FXML
    Label dawnDuskRight;

    @FXML
    Line dawnDuskNowLine;

    @FXML
    ImageView toggleRealFeel;

    @FXML
    ImageView toggleNightMode;

    @FXML
    ImageView windBearing;

    @FXML
    Label summaryLabel;

    @FXML
    Line nowLine;

    @FXML
    Label nowLabel;

    @FXML
    protected void ClickMeHandler(Event event) {
        System.out.println(lowerToggle.isSelected());
    }

    @FXML
    protected void refreshHandler(Event event){
        WeatherData newWeatherData;
        try{
           weatherData = DataQuery.queryData(cityName.getText());
        }
        catch(LocationNotFoundException e){
            Alert locationAlert = new Alert(Alert.AlertType.ERROR);
            locationAlert.setTitle("Location Not Found");
            locationAlert.setContentText("Sorry, we can't find weather data for " +
                    e.getMessage() + ". Please enter a valid location");
            locationAlert.setHeaderText(null);
            locationAlert.showAndWait();
            return;
        }
        catch(APIReadException e){
            Alert apiAlert = new Alert(Alert.AlertType.ERROR);
            apiAlert.setTitle("API Error");
            apiAlert.setContentText("Sorry, we encountered an error while getting our data. Please read the README file " +
                            "and enter a valid API key for each API provider" );
            apiAlert.setHeaderText(null);
            apiAlert.showAndWait();
            return;
        }
        updateWeatherData(weatherData);
    }

    public void updateWeatherData(WeatherData weatherData) {
        NotificationLabel.setText(weatherData.location);

        this.weatherData = weatherData;
        mainTempLabel.setText(Math.round(isFeelTemp ? weatherData.currentApparentTemperature
                                                    : weatherData.currentTemperature) + " " + "\u00B0C");

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

        // Setting the label above the graph and the summary to displayed day
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 2);
        String dayAfterTomorrow = c.get(Calendar.DAY_OF_MONTH) + " " +
                c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK);
        String[] possibleGraphTexts = {"Today", "Tomorrow", dayAfterTomorrow};
        String[] possibleSummaryTexts = {"Now", "Tomorrow", dayAfterTomorrow};
        chartDayLabel.setText(possibleGraphTexts[currentlyDisplayedDay]);
        summaryLabel.setText(possibleSummaryTexts[currentlyDisplayedDay]);

        // Populate temperature graph
        double[][] temperatureSource = isFeelTemp ? weatherData.apparentTemperatureForecast : weatherData.temperatureForecast;
        XYChart.Series<String, Double> lineSeries = new XYChart.Series<>();
        for (int i = 0; i < 24; i++) {
            String label = i+":00";
            lineSeries.getData().add(new XYChart.Data<>(label, temperatureSource[currentlyDisplayedDay][i]));
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

        // Draw now line for graphs
        if (currentlyDisplayedDay > 0) {
            nowLine.setVisible(false);
            nowLabel.setVisible(false);
        } else {
            nowLine.setVisible(true);
            nowLabel.setVisible(true);

            // Calculate margin by from time
            LocalDateTime localTime = LocalDateTime.now();
            float past = localTime.getHour() * 60 + localTime.getMinute();
            float proportion = past / (24*60);
            int margin = Math.round(50 + 232 * proportion);

            // Apply margin and set text
            GridPane.setMargin(nowLine, new Insets(10, 0, 0, margin));
            GridPane.setMargin(nowLabel, new Insets(-4, 0, 0, margin -25));
            DecimalFormat decimalFormat = new DecimalFormat("00");
            nowLabel.setText("Now - " + decimalFormat.format(localTime.getHour()) + ":" + decimalFormat.format(localTime.getMinute()));

        }

        //Populate dawn dusk timings

        //Dawn Dusk variables
        double dawnTime = 0.0;
        double duskTime = 1.0;
        double nowTime = 0.2;
        double ddNowLineLength = 80.0;
        double ddNowLineInset = 0.0;
        double ddProportion;


        if (nowTime > 0) { //ie. in daytime
            dawnDuskLeft.setText("dawn" + dawnTime);
            dawnDuskRight.setText("dusk" + duskTime);


            ddProportion = (nowTime - dawnTime) / (duskTime - dawnTime);

            ddProportion = 1.0;

            dawnDuskRight.setText(Double.toString(ddProportion));


            dawnDuskNowLine.setEndX(ddNowLineLength * Math.sin(ddProportion * Math.PI * 2));
            dawnDuskNowLine.setEndY(ddNowLineLength * Math.cos(ddProportion * Math.PI * 2));





        } else {
            dawnDuskLeft.setText("dusk" + duskTime);
            dawnDuskRight.setText("dawn" + dawnTime);
        }


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
               airQuality.setStyle("-fx-font: 11 system;");
               airQuality.setText("API Call Limit Exceeded");
               pollenCount.setStyle("-fx-font: 11 system;");
               pollenCount.setText("API Call Limit Exceeded");
            }
            else{
                airQuality.setText(Math.round(weatherData.currentAQI) + "");

                pollenCount.setText(weatherData.currentPollen);
            }

            //Dawn Dusk Logic for today
            dawnDuskNowLine.setOpacity(1.0);

            if (nowTime > 0) { //ie. in daytime
                dawnDuskLeft.setText("dawn@" + dawnTime);
                dawnDuskRight.setText("dusk@" + duskTime);
                ddProportion = (nowTime - dawnTime) / (duskTime - dawnTime);
            } else {
                dawnDuskLeft.setText("dusk@" + duskTime);
                dawnDuskRight.setText("dawn@" + dawnTime);
                ddProportion = (nowTime - duskTime) / (dawnTime - duskTime);
            }
            dawnDuskNowLine.setEndX(ddNowLineLength * Math.sin((ddProportion - 0.5)*Math.PI));
            dawnDuskNowLine.setEndY(ddNowLineLength * -Math.cos((ddProportion - 0.5)*Math.PI));


        } else {
            int index = currentlyDisplayedDay - 1;
            windBearing.setVisible(false);
            windBearing.setTranslateX(-1000);


            windSpeed.setStyle("-fx-font: 11 system;");
            windSpeed.setWrapText(true);

            SimpleDateFormat format = new SimpleDateFormat("h a");     // (1-12) am/pm
            windSpeed.setText("Max: " + Math.round(weatherData.maxWindSpeedForecast[index]) + "mph at "
                                    + format.format(new Date((long)weatherData.maxWindSpeedTime[index] * 1000)) + " \n" +
                              "Min: " + Math.round(weatherData.minWindSpeedForecast[index]) + "mph at "
                                    + format.format(new Date((long)weatherData.minWindSpeedTime[index] * 1000)));


            uvIndex.setStyle("-fx-font: 11 system;");
            uvIndex.setWrapText(true);
            uvIndex.setText("Max: " + Math.round(weatherData.maxUVForecast[index]) +" at "
                    + format.format(new Date((long)weatherData.maxUVTime[index] * 1000)));

            String aqiMax = weatherData.maxAQITime[index];
            String aqiMin = weatherData.minAQITime[index];
            String pollen = weatherData.pollen[index];

            if(aqiMax == null){
                airQuality.setStyle("-fx-font: 11 system;");
                airQuality.setWrapText(true);
                airQuality.setText("API Call Limit Exceeded");

                pollenCount.setStyle("-fx-font: 11 system;");
                pollenCount.setWrapText(true);
                pollenCount.setText("API Call Limit Exceeded");
            }
            else{
                airQuality.setStyle("-fx-font: 11 system;");
                airQuality.setWrapText(true);
                aqiMax = extractHourFromString(aqiMax);
                aqiMin = extractHourFromString(aqiMin);
                airQuality.setText("Max: " + Math.round(weatherData.maxAQIForecast[index]) + " at " + aqiMax +
                                   "Min: " + Math.round(weatherData.minAQIForecast[index]) + " at " + aqiMin);

                pollenCount.setStyle("-fx-font: 11 system;");
                pollenCount.setWrapText(true);
                pollenCount.setText(pollen);
            }


            //Dawn Dusk Logic for other days

            dawnDuskLeft.setText("dawn@" + dawnTime);
            dawnDuskRight.setText("dusk@" + duskTime);

            dawnDuskNowLine.setOpacity(0.0);





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

    @FXML
    void toggleNightMode() {
        isNightMode = !isNightMode;
        updateWeatherData(weatherData);
    }

    private String extractHourFromString(String date){
        String time = date.split("T")[1];
        int hour = Integer.parseInt(time.substring(0,2));
        String result = hour + " h";
        return result;
    }
}

