package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.api.DataQuery;
import sample.api.WeatherData;

/**
 * Mostly boilerplate code for launching a JavaFX application.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        // Assume starting city is Cambridge
        WeatherData weatherData = DataQuery.queryData("London");
        System.out.println(weatherData);

        // Loading FXML file with UI components
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample.fxml"));

        // Creating window and inflating FXML
        primaryStage.setTitle("CasualJogger");
        Scene scene = new Scene(loader.load(), 315, 540);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("stylesheets/medina_dark.css").toString());
        primaryStage.setScene(scene);

        // Draw scene on startup with queried data
        Controller controller = loader.<Controller>getController();
        controller.drawScene(weatherData);

        // Show window
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
