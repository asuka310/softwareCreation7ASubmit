package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DayPlanDetailView extends Stage {

    DayPlanDetailView(MyDateInfo myinfo, MyEvents myevent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("day_plan_detail_view.fxml"));
            Parent subWindow = fxmlLoader.load();

            DayPlanDetailViewController dpdvc = fxmlLoader.getController();
            dpdvc.setMyDateInfo(myinfo);
            dpdvc.setMyevent(myevent); // mydatainfoの受け渡し
            dpdvc.setDpdv(this);


            Scene scene = new Scene(subWindow, 360, 400);
            setScene(scene);
            setTitle("Day Plan Detail");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
