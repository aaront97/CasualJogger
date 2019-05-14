package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {

    @FXML
    Label NotifictionLabel;

    @FXML
    protected void ClickMeHandler(Event event) {
        System.out.println("Test");
//        Window owner = bigTextArea.getScene().getWindow();
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("No more cookies :(");
//        alert.setContentText("Unfortunately, we have ran out of cookies. Apologies.");
//        alert.initOwner(owner);
//        alert.show();
    }
}
