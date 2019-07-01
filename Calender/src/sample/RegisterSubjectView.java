package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;

public class RegisterSubjectView extends Stage {
    RegisterSubjectView(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register_subject_view.fxml"));
            Parent registersubjectWindow = fxmlLoader.load();
            RegisterSubjectViewController rsvc=fxmlLoader.getController();
            rsvc.setRegisterSubjectView(this);
            Scene scene = new Scene(registersubjectWindow, 600, 400);
            setScene(scene);
            setTitle("Register");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
