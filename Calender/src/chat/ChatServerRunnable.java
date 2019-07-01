package chat;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class ChatServerRunnable implements Runnable {
    private Socket s;
    private String friendName;
    private String username;
    private ChatView cv;
    ChatMessage cm = null;
    public ChatServerRunnable(Socket socket, String fName, String username, ChatView chatView) {
        this.s = socket;
        this.friendName = fName;
        this.username = username;
        this.cv = chatView;
    }

    public void run() {
        // スレッドIDを出力する
        System.out.println("Connection accepted: " + s);
        System.out.println("Chat Thread : " + Thread.currentThread().getId());
        int count = 0;

        try {
            while(count < 100) {
                try {
                    while (true) {
                        ChatMessage c = (ChatMessage) this.getObject(s);
                        if (c != null) {
                            cv.receiveMessage(c);
                            System.out.println(c);
                        }
                        this.sendObject(s, cm);
                        if (cm != null)
                            cm = null;
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    count++;
                } finally {
                    if(count >= 100) {
                        System.out.println("session closing...");
                        s.close();
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setCm(ChatMessage cm) {
        this.cm = cm;
    }

    //ソケットとデータをもらい、送信するメソッド data はSerialize化してある
    public void sendObject(Socket s, Object data) throws IOException {
        System.out.println("socket = " + s);

        ObjectOutputStream ois = new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream())); // 出力用のストリーム バッファ付き

        //while(!in.readLine().equals("CS")); //送信待ち
        ois.writeObject(data); // データの送信
        ois.flush();
        System.out.println("送信内容 : " + data);

        //ois.writeObject("END"); // データ送信の終了を通知
        //ois.flush();

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

}
