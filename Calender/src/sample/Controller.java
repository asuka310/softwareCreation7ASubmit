package sample;

import chat.ChatSelector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import org.controlsfx.control.PrefixSelectionComboBox;
import org.controlsfx.control.textfield.TextFields;
import server_client.client.ClientSocketData;

import java.util.Calendar;


public class Controller {

    public GridPane calendar_gridID;
    public PrefixSelectionComboBox<String> yearselect;
    public ComboBox<Integer> monthselect;
    public Button warptime;
    public Button login_buttonID;
    public Button back_month_buttonID;
    public Button next_month_buttonID;
    public Button send_file_buttonID;
    public Button receive_buttonID;
    public Button chat_buttonID;
    public Button timetable_buttonID;
    public Button profile_buttonID;
    public Label displayname;
    public Button go_to_today_buttonID;

    @FXML
    void initialize() {
        for(int i=1;i<=9999;i++){
            yearselect.getItems().add(String.valueOf(i));
        }
        TextFields.bindAutoCompletion(yearselect.getEditor(), yearselect.getItems());
        for(int i=1;i<=12;i++){
            monthselect.getItems().add(i);
        }
    }

    public GridPane getCalendar_grid() {
        return calendar_gridID;
    }

    public ComboBox<String> getYearselect() {
        return yearselect;
    }

    public ComboBox<Integer> getMonthselect() {
        return monthselect;
    }

    public Button getWarptime() {
        return warptime;
    }

    public Button getLogin_button() {
        return login_buttonID;
    }

    public Button getBack_month_button() {
        return back_month_buttonID;
    }

    public Button getNext_month_button() {
        return next_month_buttonID;
    }

    public Button getSend_file_button() {
        return send_file_buttonID;
    }

    public Button getReceive_button() {
        return receive_buttonID;
    }

    public void startChat(ActionEvent actionEvent) {
        if(!ClientSocketData.getInstance().isLogin()){
            Alert alrt = new Alert(Alert.AlertType.WARNING); //アラートを作成
            alrt.setHeaderText(null);
            alrt.setContentText("まず、ログインしてください。");
            alrt.showAndWait(); //表示
            return;
        }
        ChatSelector cs = new ChatSelector();
        //cs.show(); ChatSelector内で行う
    }

    public Button getTimetable_button() {
        return timetable_buttonID;
    }

    public Button getProfile_button() {
        return profile_buttonID;
    }

    public Label getDisplayname(){
        return  displayname;
    }

    public Button getGoToTodayButton() {
        return go_to_today_buttonID;
    }

}
