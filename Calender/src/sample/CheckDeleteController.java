package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Window;

public class CheckDeleteController {
    public Label deletetitle, finishdelete;
    private MyEvents me;
    private MyDateInfo mdi;
    @FXML
    void initialize() {
    }

    public void setMyevent(MyEvents myevent){
        this.me = myevent;
    }

    public void setMyDateInfo(MyDateInfo mydate){
        this.mdi = mydate;
    }

    public void closeWindow(ActionEvent actionEvent) {
        Scene scene = ((Node) actionEvent.getSource()).getScene();
        Window window = scene.getWindow();
        window.hide();
    }

    public void deleteItem(){
        mdi.deleteEvents(me);
        finishdelete.setTextFill(Color.RED);
        finishdelete.setText("削除が完了しました。");
    }
}
