package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DayPlanView extends Stage {
    //MyDateInfo mdi;

    DayPlanView(MyDateInfo mydateinfo){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("day_plan_view.fxml"));
            Parent subWindow = fxmlLoader.load();

            DayPlanViewController dpvc = fxmlLoader.getController();
            dpvc.setMydatainfo(mydateinfo); // mydatainfoの受け渡し

            dpvc.roadDayData(); // リストの追加

            Scene scene = new Scene(subWindow, 360, 420);
            setScene(scene);
            setTitle("Day Plan");
            //mdi = mydateinfo;
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
