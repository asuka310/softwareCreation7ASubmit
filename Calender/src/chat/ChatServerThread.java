package chat;

import javafx.application.Platform;
import server_client.client.ClientSocketData;
import server_client.server.ServerWorkerRunnable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServerThread extends Thread {
    private String username;
    private int PORT = 3001;

    public ChatServerThread(String username) {
        this.username = username;
        if(ClientSocketData.getInstance().getsSocket() == null)
            this.setupServerSocket();
    }

    private void setupServerSocket(){
        try {
            ServerSocket s;
            s = new ServerSocket(PORT);
            ClientSocketData.getInstance().setsSocket(s);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            System.out.println("チャットサーバの起動");

            ServerSocket s = ClientSocketData.getInstance().getsSocket();

            ClientSocketData.getInstance().setChatSocket3001(s);

            Socket socket = ClientSocketData.getInstance().getsSocket().accept(); // コネクション設定要求を待つ

            //データを受け取る
            System.out.println("socket = " + s);
            String friend;
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream())); // 出力用のストリーム バッファ付き
            try {
                // l = {"friend name", "ipadd"}
                ArrayList<String> l = (ArrayList<String>) ois.readObject(); // データの受信
                friend = l.get(0);
                String ipadd = l.get(1);

                // GUI の起動
                Platform.runLater(() -> launchGUI(friend, socket, username, ipadd));


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void launchGUI(String friend, Socket socket, String username, String ipadd){
        try {
            int PORT2 = 3002;
            InetAddress addr = InetAddress.getByName(ipadd); //IPアドレスの変換
            Socket talk = new Socket(addr, PORT2);
            ChatView cv = new ChatView(friend, username, socket, talk);
            cv.showingProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue == true && newValue == false) {
                    new ChatServerThread(username).start(); // 画面が閉じられたら再起動
                }
            });
            cv.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
