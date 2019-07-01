package server_client.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class ServerGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("server_gui_layout.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Server GUI");
        primaryStage.setScene(new Scene(root, 835, 610));

        ServerGUIBase.setLoader(fxmlLoader); // ローダの記憶

        ServerDatabase.getInstance().setup(); // 保存済みのmap類の読み出し

        myServerMultiThread msmt = new myServerMultiThread();
        msmt.start(); // サーバーの待機開始

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION , "サーバーを終了しますか?" , ButtonType.YES , ButtonType.NO);
            ButtonType button  = alert.showAndWait().orElse( ButtonType.CANCEL );
            System.out.println(button.getText());
            //はいボタンが押されたら終了、いいえなら終了せずに戻る
            if(button.getText().equals("はい")){
                msmt.interrupt();
                System.exit(0);
            }else{
                event.consume();
            }

        });

        primaryStage.show();


    }



}
