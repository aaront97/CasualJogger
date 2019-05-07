package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Window;

public class Controller {

    @FXML
    private TextArea bigTextArea;

    @FXML
    protected void ClickMeHandler(Event event) {
        bigTextArea.setText("You have clicked me! Here's a cookie as a reward :) Blah blah " +
                ";sakfajkbrfk,wbfjwebfgajrs,hasmjrhgbUIRWGWlefh,kEWFH,WEFJ");
//        Window owner = bigTextArea.getScene().getWindow();
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("No more cookies :(");
//        alert.setContentText("Unfortunately, we have ran out of cookies. Apologies.");
//        alert.initOwner(owner);
//        alert.show();
    }
}
