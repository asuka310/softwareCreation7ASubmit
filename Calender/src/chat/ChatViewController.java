package chat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Calendar;
import java.util.List;

public class ChatViewController {
    public Label name_labelID;
    public TextArea text_areaID;
    public VBox message_vboxID;
    public Button send_button;
    public ScrollPane scroll_ID;
    private List<ChatMessage> chat;
    private String userName;

    @FXML
    void initialize(){
        message_vboxID.heightProperty().addListener((ChangeListener) (observable, oldvalue, newValue) -> scroll_ID.setHvalue((Double)newValue ));
    }

    public Label getName_label() {
        return name_labelID;
    }

    public VBox getMessageVBox() {
        return message_vboxID;
    }

    public TextArea getText_area() {
        return text_areaID;
    }

    public void setChat(List<ChatMessage> chat) {
        this.chat = chat;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Button getSend_button() {
        return send_button;
    }

    public ScrollPane getScroll() {
        return scroll_ID;
    }

}
