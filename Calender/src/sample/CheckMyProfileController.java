package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import server_client.client.ClientSocketData;

public class CheckMyProfileController {

    public Label username;
    public Label nickname;
    public Label gender;
    public ListView ablecourse;
    public ListView disablecourse;

    @FXML
    void initialize() {
        if(ClientSocketData.getInstance().getUserName()!=null){
            username.setText(ClientSocketData.getInstance().getUserName() + " さんのプロフィール");
        }
        if(ClientSocketData.getInstance().getProfile().isValid()){
            Profile p = ClientSocketData.getInstance().getProfile();
            nickname.setText(p.getNickname());
            gender.setText(p.getGender());
            ObservableList<String> able = FXCollections.observableArrayList(p.getAble_list());
            ObservableList<String> disable = FXCollections.observableArrayList(p.getDisable_list());
            ablecourse.getItems().clear(); // 前のを削除
            ablecourse.setItems(able); // 新しいものに入れ替え
            disablecourse.getItems().clear(); // 前のを削除
            disablecourse.setItems(disable); // 新しいものに入れ替え
        }
    }

    public Label getNickName(){
        return nickname;
    }

    public Label getGender(){
        return gender;
    }

    public ListView getAblecourse(){
        return ablecourse;
    }

    public ListView getDisablecourse(){
        return disablecourse;
    }

    public void ChangeProfile(){
        if(!ClientSocketData.getInstance().isLogin()){
            Alert alrt = new Alert(Alert.AlertType.WARNING); //アラートを作成
            alrt.setHeaderText(null);
            alrt.setContentText("まず、ログインしてください。");
            alrt.showAndWait(); //表示
            return;
        }
        ProfileView pfv = new ProfileView(this);
        pfv.show();
    }

}
