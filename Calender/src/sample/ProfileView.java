package sample;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileView extends Stage{
    ProfileView(CheckMyProfileController cmpcont){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile_view.fxml"));
            Parent profileWindow = fxmlLoader.load();
            ProfileViewController pfvc =fxmlLoader.getController();
            pfvc.setCheckMyProfileController(cmpcont);
            Scene scene = new Scene(profileWindow, 750, 560);
            setScene(scene);
            setTitle("Profile");
            //show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
