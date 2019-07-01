package sample;
import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import server_client.client.ClientSocketData;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class EditPlaning extends Stage{

    final ComboBox<Integer> timesh, timeeh;   //コンボボックスの宣言
    final ComboBox<String> timesm, timeem;
    TextField title, place;
    TextArea content;
    Label msg;
    MyDateInfo mdi;

    public EditPlaning(MyDateInfo mydateinfo){
        setTitle("Plan Content");
        //StackPane root = new StackPane();
        GridPane pane = new GridPane();
        pane.setStyle("-fx-background-color: white;");
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setVgap(5);
        pane.setHgap(5);

        Scene scene = new Scene(pane, 650, 300);
        this.setScene(scene);
        this.show();

        this.mdi = mydateinfo;

        Label titlel = new Label("タイトル*:");
        title = new TextField();
        title.getStylesheets().add(EditPlaning.class.getResource("../cssFolder/textfieldDesign01.css").toExternalForm());
        GridPane.setConstraints(title, 0, 1);
        pane.getChildren().addAll(titlel, title);

        timesh = new ComboBox<Integer>(FXCollections.observableArrayList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23));
        timesm = new ComboBox<String>(FXCollections.observableArrayList("0","5","10","15","20","25","30","35","40","45","50","55"));
        timesh.getStylesheets().add(EditPlaning.class.getResource("../cssFolder/comboboxDesign01.css").toExternalForm());
        timesh.setPrefWidth(150);
        timesm.setEditable(true);
        timesm.getStylesheets().add(EditPlaning.class.getResource("../cssFolder/comboboxDesign02.css").toExternalForm());
        timesm.setPrefWidth(150);

        Label time_sl = new Label("開始時刻*:");
        GridPane.setConstraints(time_sl, 0, 2);
        GridPane.setConstraints(timesh, 1, 2);
        Label timeshl = new Label(":");
        GridPane.setConstraints(timeshl, 2, 2);
        GridPane.setConstraints(timesm, 3, 2);
        pane.getChildren().addAll(time_sl, timesh, timeshl, timesm);

        timeeh = new ComboBox<Integer>(FXCollections.observableArrayList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23));
        timeem = new ComboBox<String>(FXCollections.observableArrayList("0","5","10","15","20","25","30","35","40","45","50","55"));
        timeeh.getStylesheets().add(EditPlaning.class.getResource("../cssFolder/comboboxDesign01.css").toExternalForm());
        timeeh.setPrefWidth(150);
        timeem.setEditable(true);
        timeem.getStylesheets().add(EditPlaning.class.getResource("../cssFolder/comboboxDesign02.css").toExternalForm());
        timeem.setPrefWidth(150);


        Label time_el = new Label("終了時刻*:");
        GridPane.setConstraints(time_el, 0, 3);
        GridPane.setConstraints(timeeh, 1, 3);
        Label timeehl = new Label(":");
        GridPane.setConstraints(timeehl, 2, 3);
        GridPane.setConstraints(timeem, 3, 3);
        pane.getChildren().addAll(time_el, timeeh, timeehl, timeem);

        Label placel = new Label("場所:");
        place = new TextField();
        place.getStylesheets().add(EditPlaning.class.getResource("../cssFolder/textfieldDesign01.css").toExternalForm());
        GridPane.setConstraints(placel, 0, 4);
        GridPane.setConstraints(place, 0, 5);
        pane.getChildren().addAll(placel, place);

        Label contentl = new Label("詳細内容:");
        content = new TextArea();
        content.getStylesheets().add(EditPlaning.class.getResource("../cssFolder/textareaDesign01.css").toExternalForm());
        GridPane.setConstraints(contentl, 0, 6);
        GridPane.setConstraints(content, 0, 7);
        content.setWrapText(true);    //入力が右端に行ったら改行されるように設定
        pane.getChildren().addAll(contentl, content);

        Button regist = new Button("登録");
        regist.getStylesheets().add(EditPlaning.class.getResource("../cssFolder/buttonDesign02.css").toExternalForm());
        regist.setPrefHeight(30);
        regist.setMinHeight(30);
        GridPane.setConstraints(regist, 1, 8);
        pane.getChildren().add(regist);
        regist.setOnMouseClicked(event -> Register());

        msg = new Label("*:必須");
        msg.setTextFill(Color.RED);
        GridPane.setConstraints(msg, 0, 8);
        pane.getChildren().add(msg);

//        Region region = (Region)tarea.lookup(".content");   //背景色を設定
//        region.setStyle("-fx-background-color: #87CEFA;");

    }

    public void Register(){

        String sendtitle = title.getText(); //「タイトル」を格納
        int sh,  eh, sm , em; //左から開始時刻('s'tart)の時('h'our)、分('m'inute)、終了時刻('e'nd)の時、分
        boolean shNotOk = timesh.getSelectionModel().isEmpty(); //開始時刻・終了時刻が入力されているかを表す
        boolean smNotOk = timesm.getValue() == null || timesm.getValue().isEmpty();
        boolean ehNotOk = timeeh.getSelectionModel().isEmpty();
        boolean emNotOk = timeem.getValue() == null || timeem.getValue().isEmpty();

        String sendplace, sendcont;
        if(sendtitle.isEmpty()){    //タイトルが未入力の場合
            msg.setText("タイトルを入力してください。");  //エラーメッセージ
        }else{
            if(shNotOk || smNotOk || ehNotOk || emNotOk){   //開始時刻・終了時刻が全て埋められていない場合
                msg.setText("開始時刻と終了時刻を全て入力してください。");   //エラーメッセージ
            }else{
                try{
                    sm = Integer.parseInt(timesm.getValue());   //開始時刻・終了時刻(分)の格納
                    em = Integer.parseInt(timeem.getValue());
                    if(sm < 0 || 60 <= sm || em < 0 || 60 <= em){   //分の値が範囲外の場合
                        msg.setTextFill(Color.RED);
                        msg.setText("時刻の入力が正しくありません。（0～59の値を入力してください。）");   //エラーメッセージ
                    }else{
                        sh = timesh.getValue(); //開始時刻・終了時刻(時)の格納
                        eh = timeeh.getValue();
                        sendplace = place.getText();    //「場所」を格納
                        sendcont = content.getText();   //「詳細内容」を格納

                        Calendar s_time = (Calendar) mdi.getDay().clone(); // 日時比較用
                        Calendar e_time = (Calendar) mdi.getDay().clone(); // 日時比較用

                        s_time.set(Calendar.HOUR, sh); // 開始時間の設定
                        s_time.set(Calendar.MINUTE, sm); // 開始時間の設定
                        e_time.set(Calendar.HOUR, eh); // 終了時間の設定
                        e_time.set(Calendar.MINUTE, em); // 終了時間の設定

//                    System.out.println("開始時刻 : " + s_time.getTime());
//                    System.out.println("終了時刻 : " + e_time.getTime());
//                    System.out.println("s_time is after e_time : " + e_time.after(s_time));

                        if(e_time.before(s_time)){  //開始時刻と終了時刻の順番が逆の時
                            msg.setTextFill(Color.RED);
                            msg.setText("時刻の入力が正しくありません。（開始時刻と終了時刻の順が逆になっています。）");
                        }else {
                            MyEvents e = new MyEvents(sendtitle, s_time, e_time, sendplace, sendcont);
                            System.out.println("イベントの作成 :\n" + e);
                            System.out.println("サーバへ送信開始");
                            ArrayList<String> l = new ArrayList<String>();
                            l.add("registerEvent");
                            ClientSocketData.getInstance().sendObject(l);
                            ClientSocketData.getInstance().sendObject(e);// サーバへ送信
                            System.out.println("サーバへ送信完了");
                            mdi.getEvents().add(e); // イベントの追加
                            msg.setTextFill(Color.BLACK);
                            msg.setText("登録が完了しました。");
                            this.hide();
                        }
                    }
                }catch(NumberFormatException e){
                    msg.setText("時刻の入力が正しくありません。（整数値で入力してください。）");   //エラーメッセージ
                }
            }
        }
    }
}
