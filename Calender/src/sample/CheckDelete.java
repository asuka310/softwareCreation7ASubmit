package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CheckDelete extends Stage{
    CheckDelete(MyDateInfo mdi, MyEvents me){
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("check_delete.fxml"));
            Parent subWindow = fxmlLoader.load();

            CheckDeleteController cdc = fxmlLoader.getController();
            cdc.setMyDateInfo(mdi);
            cdc.setMyevent(me);
            cdc.deletetitle.setText(me.title);

            Scene scene = new Scene(subWindow, 300, 160);
            setScene(scene);
            setTitle("Check Delete");

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
