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
import server_client.client.ClientSocketData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DayPlanDetailViewController {
    public Label dateLabelID;
    public Label timeLabelID;
    public Label titleLabelID;
    public Label placeLabelID;
    public Label detailLabelID;
    //public ListView day_info_list;
    private MyDateInfo mdi;
    private MyEvents me;
    public Button delete;
    private DayPlanDetailView dpdv;


    @FXML
    void initialize() {
    }

    void setMyDateInfo(MyDateInfo myinfo){
        this.mdi = myinfo;
    }

    void setMyevent(MyEvents myevent){
        this.me = myevent;
        loadEvents();
    }

    public void setDpdv(DayPlanDetailView dpdv) {
        this.dpdv = dpdv;
    }

    void loadEvents(){
        titleLabelID.setText(me.getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年 MM月 dd日 EEEEEEE");
        String key = sdf.format(me.getS_time().getTime());
        dateLabelID.setText(key);
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        String key2 = sdf2.format(me.getS_time().getTime());
        SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
        String key3 = sdf3.format(me.getE_time().getTime());
        timeLabelID.setText(key2 + " ~ " + key3);

        placeLabelID.setText(me.getPlace());
        detailLabelID.setText(me.getDetail());

    }


    public void closeWindow(ActionEvent actionEvent) {
        Scene scene = ((Node) actionEvent.getSource()).getScene();
        Window window = scene.getWindow();
        window.hide();
    }

    private void deleteclicked(){
        Alert alt = new Alert(Alert.AlertType.WARNING);
        alt.getButtonTypes().add(ButtonType.CANCEL);
        alt.setHeaderText("消しますよ！？");
        alt.setContentText("削除する予定 : " + me.title);
        Optional opt = alt.showAndWait();
        if(opt.get() == ButtonType.OK){
            mdi.deleteEvents(me);
            Alert alt2 = new Alert(Alert.AlertType.INFORMATION);
            alt2.setHeaderText("削除しました");
            alt2.showAndWait();
            dpdv.hide(); // 画面を閉じる

            // サーバーへの送信
            ArrayList<String> l = new ArrayList<String>();
            l.add("removeEvent");
            ClientSocketData.getInstance().sendObject(l);
            System.out.println("サーバー側のイベントを削除");
            ClientSocketData.getInstance().sendObject(me);

            return;
        }
        //CheckDelete cd = new CheckDelete(mdi, me);
        //cd.show();
    }

    public void checkdelete(){
        delete.setOnMouseClicked((MouseEvent event )->{ deleteclicked();});
    }

}
