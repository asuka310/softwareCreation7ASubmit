package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CalendarCall extends AnchorPane {
    @FXML
    private Button day_buttonID;
    @FXML
    private Label day_labelID;
    @FXML
    private Rectangle color_baseID;
    @FXML
    private Label event1_labelID;
    @FXML
    private Label event2_labelID;
    @FXML
    private Label event3_labelID;

    private static final Logger logger = Logger.getLogger(CalendarCall.class.getName());

    public CalendarCall(){
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("calender_cell.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try{
            fxmlLoader.load();
        }catch(IOException e){
            throw new RuntimeException(e);
        }

        //day_listID.setStyle("-fx-font-size: 1.5em ;");

    }

    public Button getDay_button() {
        return day_buttonID;
    }

    public Label getDay_label() {
        return day_labelID;
    }

    public Rectangle getColor_base() {
        return color_baseID;
    }

    public void roadDayList(List<MyEvents> mes){
        int size = mes.size();
        event1_labelID.setText("");
        event2_labelID.setText("");
        event3_labelID.setText("");

        if(size >= 1)
            event1_labelID.setText(mes.get(0).title);
        if(size >= 2)
            event2_labelID.setText(mes.get(1).title);
        if(size == 3)
            event3_labelID.setText(mes.get(2).title);
        if(size > 3)
            event3_labelID.setText("...");
    }
}
