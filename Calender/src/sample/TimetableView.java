package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class TimetableView extends Stage{
    TimetableView(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("timetable_view.fxml"));
            Parent timetableWindow = fxmlLoader.load();
            TimetableViewController ttvc=fxmlLoader.getController();

            Scene scene = new Scene(timetableWindow, 530, 450);
            setScene(scene);
            setTitle("Timetable");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
