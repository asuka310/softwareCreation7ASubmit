package chat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import sample.CalendarCall;

import java.io.IOException;
import java.util.logging.Logger;

public class FriendReply extends AnchorPane {
    @FXML
    private Label friend_nameID;
    @FXML
    private Label friend_messageID;
    @FXML
    private Label f_name_initialID;

    private static final Logger logger = Logger.getLogger(CalendarCall.class.getName());

    public FriendReply(String friendName, String friendMessage){
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("friend_reply.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try{
            fxmlLoader.load();
        }catch(IOException e){
            throw new RuntimeException(e);
        }

        f_name_initialID.setText(String.valueOf(friendName.charAt(0)));
        friend_nameID.setText(friendName);
        friend_messageID.setText(friendMessage);
    }

}
