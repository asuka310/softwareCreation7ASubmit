package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FileReceive  extends Stage {
    FileReceive(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("file_receive.fxml"));
            Parent subWindow = fxmlLoader.load();

            FileReceiveController frc = fxmlLoader.getController();
            frc.setStage(this); // ステージの記憶

            //GridPane gpane = new GridPane();
            Scene scene = new Scene(subWindow, 850, 500);
            setScene(scene);
            setTitle("File Receive");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
