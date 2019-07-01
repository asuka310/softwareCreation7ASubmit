package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class RegisterSubjectViewController {
    public Label time;
    public Label Lecture;
    public Label Place;
    public Label Professor;
    public Button Register;
    public Button close;
    private RegisterSubjectView rsv;

    @FXML
    void initialize(){

    }

    public Label getTime(){ return time; }

    public Label getlecture(){return Lecture;}

    public Label getplace(){return Place;}

    public  Label getprofessor(){return Professor;}

    public void setRegisterSubjectView(RegisterSubjectView rsvt){rsv=rsvt;}

    public void changedetail(){
        RegisterSubjectDetailView rsdv =new RegisterSubjectDetailView(this);
        rsdv.show();
    }
    public void closewindow(){
        rsv.hide();
    }

}
