package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterSubjectDetailView extends Stage {
    RegisterSubjectDetailView(RegisterSubjectViewController rsvc){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register_subject_detail_view.fxml"));
            Parent registerDetailWindow = fxmlLoader.load();
            RegisterSubjectDetailViewController rsdvc=fxmlLoader.getController();
            rsdvc.setRegisterSubejectViewController(rsvc);
            Scene scene = new Scene(registerDetailWindow, 550, 430);
            setTitle("RegisterSubjectDetail");
            setScene(scene);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
