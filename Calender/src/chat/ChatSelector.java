package chat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import server_client.client.ClientSocketData;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ChatSelector extends Stage {
    ArrayList<ArrayList<String>> userList;
    ChatSelectorController csc;
    public ChatSelector(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat_selector_layout.fxml"));
            Parent subWindow = fxmlLoader.load();

            csc = fxmlLoader.getController();

            Scene scene = new Scene(subWindow, 600, 400);
            setScene(scene);
            setTitle("chat selector");

            //TODO データの読み込みが終わるまで一時的な画面を表示


            // user list の受信要求
            ArrayList<String> l = new ArrayList<String>();
            l.add("getChatData");
            l.add(InetAddress.getLocalHost().getHostName());
            ClientSocketData.getInstance().sendObject(l);

            userList = (ArrayList<ArrayList<String>>) ClientSocketData.getInstance().getObject();
            // [[user, ip], [user, ip], [user, ip], ...]

            if(userList.size() == 1 && userList.get(0).isEmpty()) { // 他にログイン中のユーザがいない
                Alert alrt = new Alert(Alert.AlertType.NONE, "チャット可能なユーザがいませんでした...", ButtonType.OK); //アラートを作成
                alrt.setHeaderText(null);
                alrt.showAndWait(); //表示
                return;
            }


            List<String> userListForListView = new ArrayList<String>();

            for(ArrayList<String> al : userList)
                userListForListView.add(al.get(0)); // ユーザー名をまとめる

            ObservableList<String> lm = FXCollections.observableArrayList(userListForListView);

            csc.getUser_selector_list().getItems().clear(); // 前のを削除
            csc.getUser_selector_list().setItems(lm); // 新しいものに入れ替え
            csc.getStart_chat_button().setOnMouseClicked((MouseEvent event )->{ chatUserSelected( event );});

            if(ClientSocketData.getInstance().getServ3002() == null)
                setupServerSocket();

            this.show();


        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            // 失敗の処理
        }
    }

    private void setupServerSocket(){
        try {
            int PORT2 = 3002;
            ClientSocketData.getInstance().setServ3002(new ServerSocket(PORT2));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void chatUserSelected(MouseEvent event){
        String s = (String)csc.getUser_selector_list().getSelectionModel().getSelectedItem();
        if(s == null)
            return;

        String ipTarget = null;

        for(ArrayList<String> al : userList) {
            if(al.get(0).equals(s)){
                ipTarget = al.get(1);
                System.out.println("Selected ip address : " + ipTarget);
                break;
            }
        }

        // チャットを開始
        if(ipTarget != null){
            try {
                int PORT = 3001; // ポート番号を設定する
                InetAddress addr = InetAddress.getByName(ipTarget); //IPアドレスの変換
                Socket socket = new Socket(addr, PORT);

                this.hide();

                ArrayList<String> l = new ArrayList<String>();
                l.add(ClientSocketData.getInstance().getUserName());
                l.add(socket.getLocalAddress().getHostAddress()); // ipアドレスの通知
                ObjectOutputStream ois = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream())); // 出力用のストリーム バッファ付き
                ois.writeObject(l); // データの送信
                ois.flush();
                System.out.println("送信内容 : " + l);

                try {
                    ClientSocketData.getInstance().getChatSocket3001().close();
                }catch (SocketException e){
                    e.printStackTrace();
                }


                Socket listen = ClientSocketData.getInstance().getServ3002().accept();
                System.out.println("ServerSocket accepted : " + listen);

                ChatView cv = new ChatView(s, ClientSocketData.getInstance().getUserName(), listen, socket);
                cv.showingProperty().addListener((observable, oldValue, newValue) -> {
                    if (oldValue == true && newValue == false) {
                        new ChatServerThread(ClientSocketData.getInstance().getUserName()).start(); // 画面が閉じられたら再起動
                    }
                });
                cv.show();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}
