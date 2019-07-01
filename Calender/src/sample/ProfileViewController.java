package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
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

import java.util.ArrayList;
import java.util.List;

public class ProfileViewController {
    public TextField nickname;
    public ComboBox gender;
    public String[] coursenames = new String[]{"言語処理系", "プログラミングB", "情報通信ネットワーク", "ソフトウェア工学", "OSA", "生命情報処理とICT", "人工知能A", "ディジタル信号処理"};
    @FXML
    public CheckBox a0, a1, a2, a3, a4, a5, a6, a7;
    public CheckBox[] ablecheck;
    //public ArrayList<CheckBox> ablecheck;
    @FXML
    public CheckBox d0, d1, d2, d3, d4, d5, d6, d7;
    public CheckBox[] disablecheck;
    //public ArrayList<CheckBox> disablecheck;
    private CheckMyProfileController cmpc;

    @FXML
    void initialize() {
        ablecheck = new CheckBox[]{a0, a1, a2, a3, a4, a5, a6, a7};
        disablecheck = new CheckBox[]{d0, d1, d2, d3, d4, d5, d6, d7};
        gendercomboxChange();
        System.out.println("ore");
    }

    public void setCheckMyProfileController(CheckMyProfileController cmpct){
        cmpc = cmpct;
    }

    void gendercomboxChange() {
        gender.setStyle("-fx-font: 15pt 'Meiryo';");
        gender.setCellFactory(
                new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        final ListCell<String> cell = new ListCell<String>() {
                            {
                                super.setPrefWidth(100);
                                super.setAlignment(Pos.CENTER);
                            }

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item);
                                    if (item.equals("女")) {
                                        setTextFill(Color.RED);
                                        setFont(Font.font(20));
                                    } else if (item.equals("男")) {
                                        setTextFill(Color.BLUE);
                                        setFont(Font.font(20));
                                    }
                                } else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                });
        }

        public void renewalProfile(ActionEvent actionEvent){
            ArrayList<String> able_list = new ArrayList<String>();
            ArrayList<String> disable_list = new ArrayList<String>();

            cmpc.getNickName().setText(nickname.getText());
            cmpc.getGender().setText(gender.getValue().toString());
            for(int i=0;i<coursenames.length;i++){
                if(ablecheck[i].isSelected()==true){
                    able_list.add(coursenames[i]);
                }
                if(disablecheck[i].isSelected()==true){
                    disable_list.add(coursenames[i]);
                }
            }

            ObservableList<String> able = FXCollections.observableArrayList(able_list);
            ObservableList<String> disable = FXCollections.observableArrayList(disable_list);
            cmpc.getAblecourse().getItems().clear(); // 前のを削除
            cmpc.getAblecourse().setItems(able); // 新しいものに入れ替え
            cmpc.getDisablecourse().getItems().clear(); // 前のを削除
            cmpc.getDisablecourse().setItems(disable); // 新しいものに入れ替え
            Scene scene = ((Node) actionEvent.getSource()).getScene();
            Window window = scene.getWindow();
            window.hide();

            // ログイン中であればサーバへ送信
            if(ClientSocketData.getInstance().isLogin()) {
                System.out.println("サーバへのプロフィールの送信を開始");
                ArrayList<String> l = new ArrayList<String>();
                l.add("registerUserProfile");
                l.add(ClientSocketData.getInstance().getUserName());
                ClientSocketData.getInstance().sendObject(l);
                Profile p = new Profile(ClientSocketData.getInstance().getUserName(), nickname.getText(), gender.getTypeSelector(), able_list, disable_list);
                ClientSocketData.getInstance().sendObject(p);
                System.out.println("サーバへのプロフィールの送信を完了");
            }
        }

}
