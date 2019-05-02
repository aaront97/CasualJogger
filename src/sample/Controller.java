package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class Controller {

    @FXML
    private TextArea bigTextArea;

    @FXML
    protected void ClickMeHandler(Event event) {
        bigTextArea.setText("You have clicked me! Here's a cookie as a reward :)");
    }
}
