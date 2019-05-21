package sample;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import sample.api.APIReadException;
import sample.api.DataQuery;
import sample.api.LocationNotFoundException;
import sample.api.LocationOutOfReachException;
import sample.api.WeatherData;

public class Controller {

    private WeatherData weatherData;
    private boolean isNightMode = false;
    private boolean isFeelTemp = false;

    // 0 -> Today, 1 -> Tomorrow, 2 -> The day after tomorrow
    private int currentlyDisplayedDay = 0;

    private Image toggledImage = new Image(getClass().getClassLoader().getResource("images/toggled.png").toString());
    private Image notToggledImage = new Image(getClass().getClassLoader().getResource("images/notToggled.png").toString());
    private Image ddSun = new Image(getClass().getClassLoader().getResource("images/ddSun.png").toString());
    private Image ddMoon = new Image(getClass().getClassLoader().getResource("images/ddMoon.png").toString());
    private Image ddCross = new Image(getClass().getClassLoader().getResource("images/ddCross.png").toString());

    @FXML
    Label windLabel;

    @FXML
    TextField cityName;

    @FXML
    ToggleButton lowerToggle;

    @FXML
    Button refreshButton;

    @FXML
    Label mainTempLabel;

    @FXML
    Canvas rainCanvas;

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
    Label dawnDuskLeftTime;

    @FXML
    Label dawnDuskRightTime;

    @FXML
    Line dawnDuskNowLine;

    @FXML
    Label dawnDuskNowLabel;

    @FXML
    ImageView dawnDuskCentreGraphic;

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
    ScrollPane scrollPane;

    @FXML
    Label WeatherNotificationLabel;

    @FXML
    protected void ClickMeHandler(Event event) {
        System.out.println(lowerToggle.isSelected());
    }

    @FXML
    protected void refreshHandler(Event event){
        try{
            weatherData = DataQuery.queryData(cityName.getText() + ", United Kingdom");
        }
        catch(LocationNotFoundException e){
            Alert locationAlert = new Alert(Alert.AlertType.ERROR);
            locationAlert.setTitle("Location Not Found");
            locationAlert.setContentText("Sorry, we can't find weather data for " +
                    e.getMessage() + ". Please enter a valid city name in the UK. ");
            locationAlert.setHeaderText(null);
            locationAlert.showAndWait();
            return;
        }
        catch(LocationOutOfReachException e){
            Alert locationOutOfReachAlert = new Alert(Alert.AlertType.ERROR);
            locationOutOfReachAlert.setTitle("Location Out of Reach");
            locationOutOfReachAlert.setContentText("Sorry, our application does not currently handle locations outside the UK. " +
                    "Please enter a location within the UK.");
            locationOutOfReachAlert.setHeaderText(null);
            locationOutOfReachAlert.showAndWait();
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
        drawScene(weatherData);
    }

    public void drawScene(WeatherData weatherData) {
        this.weatherData = weatherData;
        mainTempLabel.setText(Math.round(isFeelTemp ? weatherData.currentApparentTemperature
                : weatherData.currentTemperature) + " " + "\u00B0C");

        // Setting the toggles
        if (isNightMode) {
            toggleNightMode.setImage(toggledImage);
            scrollPane.getScene().getStylesheets().clear();
            scrollPane.getScene().getStylesheets().add(
                    getClass().getClassLoader().getResource("stylesheets/medina_dark.css").toString());
        } else {            toggleNightMode.setImage(toggledImage);
        toggleNightMode.setImage(notToggledImage);
        scrollPane.getScene().getStylesheets().clear();
        scrollPane.getScene().getStylesheets().add(
                getClass().getClassLoader().getResource("stylesheets/lightMode.css").toString());        }
        if (isFeelTemp) {
            toggleRealFeel.setImage(toggledImage);
        } else {
            toggleRealFeel.setImage(notToggledImage);
        }


        // Drawing the rainmeter

        final int outer_r = 65, inner_r = 50;
        final int centre_x = 108, centre_y = 82;
        GraphicsContext gc = rainCanvas.getGraphicsContext2D();

        for (int t = 0; t < 60; ++t) {
            gc.setFill(precipColor(weatherData.immediatePrecipitationForecast[t]));
            gc.fillArc(centre_x - outer_r, centre_y - outer_r,
                    outer_r * 2, outer_r * 2, 90 - (t + 1) * 6, 6, ArcType.ROUND);
        }
        
        gc.setFill(isNightMode ? Color.rgb(50, 50, 50) : Color.rgb(244, 244, 244));
        gc.fillOval(centre_x - inner_r, centre_y - inner_r, inner_r * 2, inner_r * 2);

        gc.setFill(isNightMode? Color.rgb(244, 244, 244) : Color.rgb(50, 50, 50));
        gc.setFont(new Font(14));
        gc.fillText("Now", centre_x - 14, centre_y - outer_r - 7);
        gc.fillText("15", centre_x + outer_r + 5, centre_y + 5);
        gc.fillText("30", centre_x - 8, centre_y + outer_r + 17);
        gc.fillText("45", centre_x - outer_r - 21, centre_y + 5);
        
        gc.fillText("Precipitation", centre_x - inner_r + 12, centre_y - 5);
        gc.fillText("(next hour)", centre_x - inner_r + 18, centre_y + 15);


        // Setting the notifications text

        String display = "";
        if (Math.round(weatherData.currentTemperature) > 35) {
            display += "⚠ High temperature of " + Math.round(weatherData.currentTemperature) + "°C!\n";
        } else if (Math.round(weatherData.currentTemperature) < 5) {
            display += "⚠ Low temperature of " + Math.round(weatherData.currentTemperature) + "°C!\n";
        }
        if (Math.round(weatherData.currentWindSpeed) > 20) {
            display += "⚠ Strong winds of " + Math.round(weatherData.currentWindSpeed) + " mph!\n";
        }
        if (Math.round(weatherData.currentUV) > 3) {
            display += "⚠ High UV Index of " + Math.round(weatherData.currentUV) + "!\n";
        }
        if (Math.round(weatherData.currentAQI) > 6) {
            display += "⚠ Very Low Air Quality!\n";
        }
        if (weatherData.currentPollen != null && weatherData.currentPollen.equals("Very High")) {
            display += "⚠ Very High Pollen count!\n";
        }


        if (display.equals("")) {
            display = "Great weather, nothing to display.\nJog on!";
        }
        WeatherNotificationLabel.setText(display);




        // Setting the label above the graph and the summary to displayed day
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 2);
        String dayAfterTomorrow = c.get(Calendar.DAY_OF_MONTH) + " " +
                c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK);
        String[] possibleGraphTexts = {"Today", "Tomorrow", dayAfterTomorrow};
        String[] possibleSummaryTexts = {"Now", "Tomorrow's summary", dayAfterTomorrow + "'s summary"};
        chartDayLabel.setText(possibleGraphTexts[currentlyDisplayedDay] + "'s temperature and precipitation");
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
            GridPane.setMargin(nowLine, new Insets(15, 0, 0, margin));
            GridPane.setMargin(nowLabel, new Insets(-7, 0, 0, margin - 17));
            DecimalFormat decimalFormat = new DecimalFormat("00");
            nowLabel.setText(decimalFormat.format(localTime.getHour()) + ":" + decimalFormat.format(localTime.getMinute()));

        }

        //Dawn Dusk variables
        int[] dawnTime = weatherData.sunriseTimes;
        int[] duskTime = weatherData.sunsetTimes;

        double ddNowLineLength = 100.0;
        double ddNowLineInset = 20.0;
        dawnDuskNowLine.setStrokeWidth(2.0);
        double ddProportion;
        dawnDuskLeft.setText("Sunrise");
        dawnDuskRight.setText("Sunset");

        if(currentlyDisplayedDay == 0){
            windBearing.setVisible(true);
            windBearing.setTranslateX(-15);
            windBearing.setRotate(weatherData.currentWindBearing);

            windSpeed.setTranslateX(-10);
            windSpeed.setStyle("-fx-font: 20 system;");
            windSpeed.setText("   " + Math.round(weatherData.currentWindSpeed) + " mph");

            uvIndex.setStyle("-fx-font: 20 system;");
            uvIndex.setText(weatherData.currentUV + "");

            boolean apiCallLimitExceeded = weatherData.currentAQI == 0 ? true : false;

            if(apiCallLimitExceeded){
                airQuality.setStyle("-fx-font: 11 system;");
                airQuality.setText("API Call Limit\n Exceeded");
                pollenCount.setStyle("-fx-font: 11 system;");
                pollenCount.setText("API Call Limit\nExceeded");
            }
            else{
                airQuality.setText(Math.round(weatherData.currentAQI) + "");

                pollenCount.setText(weatherData.currentPollen);
            }

            //Dawn Dusk Logic for today
            dawnDuskNowLabel.setOpacity(1.0);
            dawnDuskNowLine.setOpacity(1.0);
            dawnDuskCentreGraphic.setImage(ddSun);
            dawnDuskCentreGraphic.setEffect(new GaussianBlur(3.0));

            long ddNowTimeUnix = (new Date()).getTime()/1000L;
            Date ddDawnTime = new Date(dawnTime[currentlyDisplayedDay]*1000L);
            Date ddDuskTime = new Date(duskTime[currentlyDisplayedDay]*1000L);
            if (ddNowTimeUnix > dawnTime[currentlyDisplayedDay] &&
                    ddNowTimeUnix < duskTime[currentlyDisplayedDay]){ //ie. in daytime

                ddProportion = (ddNowTimeUnix - dawnTime[currentlyDisplayedDay])
                        / (double) (duskTime[currentlyDisplayedDay] - dawnTime[currentlyDisplayedDay]);

                dawnDuskNowLine.setEndX(ddNowLineLength * Math.sin((ddProportion - 0.5)*Math.PI));
                dawnDuskNowLine.setStartX(ddNowLineInset * Math.sin((ddProportion - 0.5)*Math.PI));
                dawnDuskNowLine.setEndY(ddNowLineLength * -Math.cos((ddProportion - 0.5)*Math.PI));
                dawnDuskNowLine.setStartY(ddNowLineInset * -Math.cos((ddProportion - 0.5)*Math.PI));

                dawnDuskCentreGraphic.setImage(ddSun);
                dawnDuskCentreGraphic.setFitHeight(53.0);
                dawnDuskCentreGraphic.setFitWidth(53.0);
                dawnDuskCentreGraphic.setX(0.0);
                dawnDuskCentreGraphic.setY(0.0);

            } else {
                dawnDuskCentreGraphic.setImage(ddMoon);
                dawnDuskCentreGraphic.setFitHeight(100.0);
                dawnDuskCentreGraphic.setFitWidth(100.0);
                dawnDuskCentreGraphic.setY(-50.0);
                dawnDuskCentreGraphic.setX(-24.0);
                dawnDuskNowLine.setOpacity(0.0);
            }

            dawnDuskLeftTime.setText((new SimpleDateFormat("HH:mm")).format(ddDawnTime));
            dawnDuskRightTime.setText((new SimpleDateFormat(("HH:mm")).format(ddDuskTime)));


            // Calculate margin by from time
            LocalDateTime localTime = LocalDateTime.now();
            float past = localTime.getHour() * 60 + localTime.getMinute();
            float proportion = past / (24*60);
            int margin = Math.round(50 + 232 * proportion);
            DecimalFormat decimalFormat = new DecimalFormat("00");

            dawnDuskNowLabel.setText("Now\n" + decimalFormat.format(localTime.getHour()) + ":"
                    + decimalFormat.format(localTime.getMinute()));


        } else {
            int index = currentlyDisplayedDay - 1;
            windBearing.setVisible(false);
            windBearing.setTranslateX(-1000);


            windSpeed.setStyle("-fx-font: 11 system;");
            windSpeed.setWrapText(true);

            SimpleDateFormat format = new SimpleDateFormat("h a");     // (1-12) am/pm
            windSpeed.setText("Max: " + Math.round(weatherData.maxWindSpeedForecast[index]) + " mph at "
                    + format.format(new Date((long)weatherData.maxWindSpeedTime[index] * 1000)) + " \n" +
                    "Min: " + Math.round(weatherData.minWindSpeedForecast[index]) + " mph at "
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
                airQuality.setText("API Call Limit\nExceeded");

                pollenCount.setStyle("-fx-font: 11 system;");
                pollenCount.setWrapText(true);
                pollenCount.setText("API Call Limit\nExceeded");
            }
            else{
                airQuality.setStyle("-fx-font: 11 system;");
                airQuality.setWrapText(true);
                System.out.println("Aqi Max: " + aqiMax);
                SimpleDateFormat parsingFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                try {
                    airQuality.setText("Max: " + Math.round(weatherData.maxAQIForecast[index]) + " at "
                            + format.format(parsingFormat.parse(aqiMax)) + "\n" +
                            "Min: " + Math.round(weatherData.minAQIForecast[index]) + " at "
                            + format.format(parsingFormat.parse(aqiMin)));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                pollenCount.setStyle("-fx-font: 11 system;");
                pollenCount.setWrapText(true);
                pollenCount.setText(pollen);
            }

            //Dawn Dusk Logic for other days
            dawnDuskNowLine.setOpacity(0.0);
            Date ddDawnTime = new Date(dawnTime[currentlyDisplayedDay]*1000L);
            Date ddDuskTime = new Date(duskTime[currentlyDisplayedDay]*1000L);
            dawnDuskNowLabel.setOpacity(0.0);
            dawnDuskCentreGraphic.setImage(ddCross);
            dawnDuskCentreGraphic.setFitHeight(25.0);
            dawnDuskCentreGraphic.setFitWidth(25.0);
            dawnDuskCentreGraphic.setX(13.0);
            dawnDuskCentreGraphic.setY(11.0);

            dawnDuskLeftTime.setText((new SimpleDateFormat("HH:mm")).format(ddDawnTime));
            dawnDuskRightTime.setText((new SimpleDateFormat(("HH:mm")).format(ddDuskTime)));
        }
    }



    @FXML
    protected void todayButtonClicked() {
        if (currentlyDisplayedDay != 0) {
            currentlyDisplayedDay = 0;
            tomorrowButton.setSelected(false);
            dayAfterTomorrowButton.setSelected(false);
            drawScene(weatherData);
        }
        todayButton.setSelected(true);
    }

    @FXML
    protected void tomorrowButtonClicked() {
        if (currentlyDisplayedDay != 1) {
            currentlyDisplayedDay = 1;
            todayButton.setSelected(false);
            dayAfterTomorrowButton.setSelected(false);
            drawScene(weatherData);
        }
        tomorrowButton.setSelected(true);
    }

    @FXML
    protected void dayAfterTomorrowButtonClicked() {
        if (currentlyDisplayedDay != 2) {
            currentlyDisplayedDay = 2;
            todayButton.setSelected(false);
            tomorrowButton.setSelected(false);
            drawScene(weatherData);
        }
        dayAfterTomorrowButton.setSelected(true);
    }

    @FXML
    protected void toggleRealFeel() {
        isFeelTemp = !isFeelTemp;
        drawScene(weatherData);
    }

    @FXML
    void toggleNightMode() {
        isNightMode = !isNightMode;
        drawScene(weatherData);
    }

    private static String extractHourFromString(String date){
        String time = date.split("T")[1];
        int hour = Integer.parseInt(time.substring(0,2));
        String result = hour + " h";
        return result;
    }

    private static Color precipColor(double probability) {
        return Color.color(1 - probability, 1 - probability, 1 - probability);
    }
}

