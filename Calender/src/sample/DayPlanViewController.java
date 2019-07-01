package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DayPlanViewController {
    public ListView title_listID;
    public Label day_labelID;

    private MyDateInfo mdi;


    @FXML
    void initialize() {
    }


    void setMydatainfo(MyDateInfo mydatainfo){
        this.mdi = mydatainfo;
        title_listID.setOnMouseClicked((MouseEvent event )->{ listCellClicked( event );});

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日"); //フォーマッター
        day_labelID.setText(sdf.format(mdi.getDay().getTime()));
    }

    void roadDayData(){
        List<String> t_list = new ArrayList<String>();

        for(MyEvents me : mdi.getEvents())
            t_list.add(me.title);

        ObservableList<String> lm = FXCollections.observableArrayList(t_list);

        title_listID.getItems().clear(); // 前のを削除
        title_listID.setItems(lm); // 新しいものに入れ替え

    }


    public void addPlan(ActionEvent actionEvent) {
        EditPlaning plan = new EditPlaning(mdi);
        // 閉じたら再読み込み
        plan.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue == true && newValue == false) {
                roadDayData();
            }
        });
        plan.show();
    }

    private void listCellClicked(MouseEvent event){ //予定のリストから一つを選択した時
        String s = (String)title_listID.getSelectionModel().getSelectedItem();
        if(s == null)
            return;

        int index = title_listID.getSelectionModel().getSelectedIndex();

        System.out.println(index);
        System.out.println(s);

        DayPlanDetailView dpdv = new DayPlanDetailView(mdi, mdi.getEvents().get(index));
        dpdv.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue == true && newValue == false) {
                roadDayData(); // 予定リストの読み込み
                System.out.println("reload day list");
            }
        });
        dpdv.show();
    }

    public void closeWindow(ActionEvent actionEvent) {
        Scene scene = ((Node) actionEvent.getSource()).getScene();
        Window window = scene.getWindow();
        window.hide();
    }
}
