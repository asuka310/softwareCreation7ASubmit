package sample;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class NewStageTest2Controller {
    public TextField userID_fieldID;
    public PasswordField pass_fieldID;
    public Button sendButtonID;
    public Button AuthButtonID;
    public Label status_labelID;

    public TextField getUserID_field() {
        return userID_fieldID;
    }

    public PasswordField getPass_field() {
        return pass_fieldID;
    }

    public Button getSendButton() {
        return sendButtonID;
    }

    public Button getAuthButton() {
        return AuthButtonID;
    }

    public Label getStatus_label() {
        return status_labelID;
    }

}
