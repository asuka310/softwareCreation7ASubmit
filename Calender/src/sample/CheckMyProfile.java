package sample;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CheckMyProfile extends Stage{
    CheckMyProfile(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("check_my_profile.fxml"));
            Parent myprofileWindow = fxmlLoader.load();
            CheckMyProfileController cmpfc =fxmlLoader.getController();
            Scene scene = new Scene(myprofileWindow, 650, 440);
            setScene(scene);
            setTitle("MyProfile");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}