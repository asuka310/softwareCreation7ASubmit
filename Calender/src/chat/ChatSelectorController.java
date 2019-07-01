package chat;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Window;

public class ChatSelectorController {
    public ListView user_selector_listID;
    public Button start_chat_buttonID;
    public Button close_buttonID;

    public ListView getUser_selector_list() {
        return user_selector_listID;
    }

    public void closeButton(ActionEvent actionEvent) {
        Scene scene = ((Node) actionEvent.getSource()).getScene();
        Window window = scene.getWindow();
        window.hide();
    }

    public Button getStart_chat_button() {
        return start_chat_buttonID;
    }
}
