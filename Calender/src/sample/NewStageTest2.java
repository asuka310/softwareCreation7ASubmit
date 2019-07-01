package sample;
import chat.ChatServerThread;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import server_client.client.ClientSocketData;

import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class NewStageTest2 extends Stage{
    GridPane pane;
    TextField name;
    PasswordField pass;
    Button output;
    Button clear;
    Label msg;
    Button match;
    Button send;
    Button Auth;
    Controller ct;
    NewStageTest2Controller ns2ct;
    //CheckMyProfileController cmpct;

    public  NewStageTest2(Stage oya, Controller cont) {
        super();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("newstage2_layout.fxml"));
            Parent subWindow = fxmlLoader.load();
            setTitle("Enter Form");
            ct = cont;
            ns2ct = (NewStageTest2Controller) fxmlLoader.getController();
            this.resizableProperty().setValue(false);
            name = ns2ct.getUserID_field();

            pass = ns2ct.getPass_field();
            send = ns2ct.getSendButton();
            send.setOnMouseClicked(event -> SendUser());

            Auth = ns2ct.getAuthButton();
            Auth.setOnMouseClicked(event -> Authenticate());

            msg = ns2ct.getStatus_label();

            Scene scene = new Scene(subWindow, 320, 200);
            msg.requestFocus();
            setScene(scene);

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void SendUser(){
        try {
            String pw_hash = new String(Hashing(pass.getText()));
            // サーバーへの送信
            ArrayList<String> l = new ArrayList<String>();
            l.add("registerData");
            l.add(String.valueOf(name.getText()));
            l.add(pw_hash);

            ClientSocketData.getInstance().sendObject(l);//送信

            ArrayList<String> respond = (ArrayList<String>)ClientSocketData.getInstance().getObject();// サーバの応答
            if(respond.get(0).equals("OK")) {
                msg.setText("Username and password is registered!");
                name.clear();
                pass.clear();
            }
            else // respond.equals("NG")
                msg.setText("Failed register (may registered username)");

        }catch (Exception e){
            e.printStackTrace();
            msg.setTextFill(Color.RED);
            msg.setText("Cannot send Userinfo.");
        }
    }

    public void Authenticate(){
        if(ClientSocketData.getInstance().isLogin()){
            Alert alrt = new Alert(Alert.AlertType.INFORMATION); //アラートを作成
            alrt.getDialogPane().lookupButton(ButtonType.OK);
            alrt.setHeaderText("すでにログインされています");
            alrt.showAndWait(); //表示
            return;
        }

        try {
            String userName = String.valueOf(name.getText());
            String pw_hash = new String(Hashing(pass.getText()));
            // サーバーへの送信
            ArrayList<String> l = new ArrayList<String>();

            l.add("authenticateData");
            l.add(userName);
            l.add(pw_hash);
            l.add(InetAddress.getLocalHost().getHostName());

            ClientSocketData.getInstance().sendObject(l); // 送信

            ArrayList<String> respond = (ArrayList<String>)ClientSocketData.getInstance().getObject();// サーバの応答
            if(respond.get(0).equals("OK")) {
                msg.setTextFill(Color.BLUE);
                msg.setText("Userdata has matched on server!");
                ct.getDisplayname().setText("ようこそ、 " + userName + " さん！");
                ClientSocketData.getInstance().setUserName(userName);
                ClientSocketData.getInstance().setLogin(true);
                ChatServerThread cst = new ChatServerThread(userName);
                ClientSocketData.getInstance().setCst(cst);
                cst.start(); // チャットサーバの起動
                name.clear();
                pass.clear();

                this.hide(); // 画面を閉じる

                // プロフィールの受信
                System.out.println("プロフィールの受信を開始");
                ArrayList<String> l2 = new ArrayList<String>();
                l2.add("getUserProfile");
                l2.add(userName);
                ClientSocketData.getInstance().sendObject(l2);
                Profile p = (Profile) ClientSocketData.getInstance().getObject();
                ClientSocketData.getInstance().setProfile(p);
                System.out.println("プロフィールの受信を完了");

            } else if(respond.get(0).equals("NON")) {
                msg.setTextFill(Color.BLUE);
                msg.setText("No such a Username");
            } else if(respond.get(0).equals("ALREADY")){
                msg.setTextFill(Color.BLUE);
                msg.setText("Already login!");
            } else { // respond.get(0).equals("NG")
                msg.setTextFill(Color.PURPLE);
                msg.setText("Userdata has not matched on server");
            }

        }catch (Exception e){
            e.printStackTrace();
            msg.setTextFill(Color.RED);
            msg.setText("Cannot send Userinfo.");
        }
    }

    public String Hashing(String pw){
        Charset cs = StandardCharsets.UTF_8;
        String algorithm = "SHA-512";
        try {
            byte[] bytes = MessageDigest.getInstance(algorithm).digest(pw.getBytes(cs));
            String pwh = new String(bytes);
            return pwh;
        }catch (NoSuchAlgorithmException nsae) {
            msg.setTextFill(Color.PURPLE);
            msg.setText("No Algorithm (System Fatal Error" + nsae);
        }
        return "";
    }
}
