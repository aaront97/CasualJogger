package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.api.DataQuery;
import sample.api.WeatherData;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        // TODO Only for debugging
        WeatherData weatherData = DataQuery.queryData("Cambridge");
        System.out.println(weatherData);
        //

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 310, 540));
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}
