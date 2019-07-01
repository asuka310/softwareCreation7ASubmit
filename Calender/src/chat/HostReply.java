package chat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import sample.CalendarCall;

import java.io.IOException;
import java.util.logging.Logger;

public class HostReply extends AnchorPane {
    @FXML
    private Label host_messageID;

    private static final Logger logger = Logger.getLogger(CalendarCall.class.getName());

    public HostReply(String hostMessage){
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("host_reply.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try{
            fxmlLoader.load();
        }catch(IOException e){
            throw new RuntimeException(e);
        }

        host_messageID.setText(hostMessage);
    }

}
