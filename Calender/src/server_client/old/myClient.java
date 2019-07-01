package server_client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class myClient {
    public static void main(String[] args) throws IOException {
        int PORT = 3000; // ポート番号を設定する
        InetAddress addr = InetAddress.getByName("localhost"); //IPアドレスの変換
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, PORT); //ソケットの生成
        try{
            System.out.println("socket = " + socket);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //データ受信用のバッファを設定
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            out.println("command"); //データを送信 これがサーバにやってもらいたいもの
            out.println("END"); // データ送信の終了を通知
            while(true) {
                String str = in.readLine(); //データを受信 ここでサーバからの応答を待つ
                if(str.equals("END")) // データの受信終了を受信
                    break;
                System.out.println(str);
            }
        }finally {
            System.out.println("closing...");
            socket.close();
        }
    }
}
