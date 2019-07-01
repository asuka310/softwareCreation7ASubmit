package chat;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatView extends Stage {
    ChatViewController cv;
    List<ChatMessage> chat;
    final int PORT = 3001;
    Socket talk;
    String hostUserName;

    ChatView(String friend, String hostUserName, Socket listen, Socket talk) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat_view_layout.fxml"));
            Parent subWindow = fxmlLoader.load();

            this.talk = talk;
            this.hostUserName = hostUserName;

            cv = fxmlLoader.getController();

            cv.getName_label().setText(friend);

            //TODO 読み込み完了まで一時的な表示


            // ファイルがあれば、読み込み
            chat = new ArrayList<ChatMessage>();

            // なければ、作成


            // TODO List<ChatMessage>の並び替え

            VBox vb = cv.getMessageVBox();

            for (ChatMessage cm : chat) {
                if (cm.getSender().equals(friend)) {
                    FriendReply fr = new FriendReply(cm.getSender(), cm.getText());
                    vb.getChildren().add(fr);
                } else {
                    HostReply hr = new HostReply(cm.getText());
                    vb.getChildren().add(hr);
                }
            }

            cv.getScroll().setVvalue(1.0);//

            cv.setChat(chat);
            cv.setUserName(hostUserName);

            cv.getSend_button().setOnMouseClicked((MouseEvent event) -> {
                sendMessage(event);
            });

            Scene scene = new Scene(subWindow, 230, 400);
            setScene(scene);
            setTitle("chat");

            // listen
            new Thread(() -> {
                try {
                    try {
                        while (true) {
                            ChatMessage cm = (ChatMessage) getObject(listen);
                            System.out.println("receive : " + cm);
                            Platform.runLater(() -> receiveMessage(cm));
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        listen.close();
                        talk.close();
                        Platform.runLater(() -> closeWindow());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();


        } catch (IOException e) {
            e.printStackTrace();
            // 失敗の処理
        }
    }

    public void receiveMessage(ChatMessage c) {
        FriendReply fr = new FriendReply(c.getSender(), c.getText());
        cv.getMessageVBox().getChildren().add(fr);
        cv.getScroll().setVvalue(1.0);//
        // TODO 保存
    }

    private void sendMessage(MouseEvent event) {
        if (cv.getText_area().getText().equals("")) // 未入力
            return;


        String s = cv.getText_area().getText();
        ChatMessage cm = new ChatMessage(s, hostUserName, Calendar.getInstance());
        sendObject(talk, cm);
        System.out.println("送信内容 : " + cm);

        chat.add(cm);
        cv.getMessageVBox().getChildren().add(new HostReply(s));
        cv.getScroll().setVvalue(1.0);//
        cv.getText_area().clear();


        // TODO chatの保存

    }

    //ソケットをもらい、受信するメソッド
    private Object getObject(Socket s) throws IOException, ClassNotFoundException {
        System.out.println("socket = " + s);

        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(s.getInputStream())); // 出力用のストリーム バッファ付き

        //while(!in.readLine().equals("CS")); //送信待ち
        Object receive = ois.readObject(); // データの受信

        System.out.println("受信内容 : " + receive);

        return receive;
    }

    //ソケットとデータをもらい、送信するメソッド data はSerialize化してある
    public void sendObject(Socket talk, Object data) {
        try {
            ObjectOutputStream ois = new ObjectOutputStream(new BufferedOutputStream(talk.getOutputStream())); // 出力用のストリーム バッファ付き
            //while(!in.readLine().equals("CS")); //送信待ち
            ois.writeObject(data); // データの送信
            ois.flush();
            System.out.println("送信内容 : " + data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeWindow() {
        this.hide();
    }
}
