package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import server_client.client.ClientSocketData;

import java.util.Optional;
/*
* カレンダーもどき一応形だけを残す
* fxmlで作ったオブジェクトを別のクラスでどう扱うかがわからない
* よってこの型はシーンビルダーを使っていない
*
*
*/

public class Main extends Application {
    Stage stage;

    GridPane grid;
    CalenderGraphics cg;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("client_gui_layout.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Calendar");
        primaryStage.setScene(new Scene(root, 950, 760));

        Controller ct = fxmlLoader.getController();

        cg = new CalenderGraphics(ct, primaryStage);

        stage = primaryStage;
        primaryStage.setTitle("Calendar");

        // ip addressの入力要求
        TextInputDialog iptDlg  = new TextInputDialog();
        iptDlg.setTitle("IP アドレスの入力");
        iptDlg.setHeaderText(ClientSocketData.getInstance().getServerIP());
        iptDlg.setContentText(null);
        iptDlg.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
        iptDlg.getEditor().setText(ClientSocketData.getInstance().getServerIP());
        Optional<String> result = iptDlg.showAndWait();
        result.ifPresent(value -> { //値があった場合
            ClientSocketData.getInstance().setServerIP(value);
        });



        //ステージの作成
        setGrid();
        primaryStage.show();

        ClientSocketData.getInstance().setup();
        ct.getDisplayname().setText("ようこそ、 ゲスト さん！");
        cg.updateButton(); // 予定の読み込み

    }

    public void setGrid(){
        grid = cg.getGrid_BackOrNextOneMonth(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
