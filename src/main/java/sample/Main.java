package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.api.DataQuery;
import sample.api.WeatherData;

import java.time.ZoneId;
import java.util.Date;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        // Assume starting city is Cambridge
        WeatherData weatherData = DataQuery.queryData("Cambridge");
        System.out.println(weatherData);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample.fxml"));

        //System.out.println(Controller.extractHourFromTimestamp((long)weatherData.maxWindSpeedForecast[1]));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(loader.load(), 310, 540));

        Controller controller = loader.<Controller>getController();
        controller.updateWeatherData(weatherData);
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}
