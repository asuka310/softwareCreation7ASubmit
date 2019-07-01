package server_client.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class myServerMultiThread extends Thread {
    public myServerMultiThread() throws IOException{

    }

    public void run(){
        ExecutorService exec = Executors.newCachedThreadPool(); // スレッドプール

        try {
            System.out.println("Server IP : " + InetAddress.getLocalHost().getHostAddress());
        }catch (UnknownHostException e){
            e.printStackTrace();
        }

        while(true) { // もし例外で終了しても再起動
            try {
                int PORT = 3000; // ポート番号を設定する
                ServerSocket s = new ServerSocket(PORT); // ソケットを作成する

                while (true) {
                    Socket socket = s.accept(); // コネクション設定要求を待つ

                    exec.submit(new ServerWorkerRunnable(socket)); // 要求があればスレッドに処理を任せる

                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}


