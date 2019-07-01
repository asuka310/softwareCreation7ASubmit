package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import server_client.client.ClientSocketData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegisterSubjectDetailViewController {
    public ComboBox starttimehour;
    public ComboBox starttimeminute;
    public ComboBox finishtimehour;
    public ComboBox finishtimeminute;
    public TextField lecture;
    public TextField place;
    public TextField professor;
    public Button change;
    private RegisterSubjectViewController rsvc;
    @FXML
    void initialize(){
        setscheduletime();
        //change.setOnMouseClicked(event->Register_Clicled());
    }

    void setscheduletime(){
        for(int time=0;time<24;time++)starttimehour.getItems().add(time);
        for(int minute=0;minute<60;minute++)starttimeminute.getItems().add(minute);
        for(int time=0;time<24;time++)finishtimehour.getItems().add(time);
        for(int minute=0;minute<60;minute++)finishtimeminute.getItems().add(minute);
    }

    public void setRegisterSubejectViewController(RegisterSubjectViewController rsvct){
        rsvc=rsvct;
    }

    public void renewalTimetable(ActionEvent actionEvent){

        rsvc.getTime().setText(
                starttimehour.getValue().toString()+":"+starttimeminute.getValue().toString()+"～"+
                finishtimehour.getValue().toString()+":"+finishtimeminute.getValue().toString());
        rsvc.getlecture().setText(lecture.getText());
        rsvc.getplace().setText(place.getText());
        rsvc.getprofessor().setText(professor.getText());

        Scene scene = ((Node) actionEvent.getSource()).getScene();
        Window window = scene.getWindow();
        window.hide();
    }

}
