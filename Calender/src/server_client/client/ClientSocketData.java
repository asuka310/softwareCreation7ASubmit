package server_client.client;

import chat.ChatServerThread;
import sample.Profile;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ClientSocketData {
    private Socket clientSocket = new Socket();
    private ServerSocket sSocket = null;

    protected String serverIP = "localhost"; /*TODO 適宜変更*/
    private int PORT = 3000; // ポート番号を設定する

    private ChatServerThread cst;

    private ServerSocket ChatSocket3001 = null;
    private ServerSocket serv3002 = null;

    private String userName;

    private boolean login = false;
    private boolean firstSetuped = false;

    private Profile profile = new Profile();

    private static ClientSocketData csd = new ClientSocketData();


    private ClientSocketData(){
        System.out.println("ClientSocketDataを作成しました");
    }

    public static ClientSocketData getInstance(){
        return csd;
    }

    public void setup(){
        try {
            InetAddress addr = InetAddress.getByName(serverIP); //IPアドレスの変換
            System.out.println("addr = " + addr);
            clientSocket = new Socket(addr, PORT); //ソケットの生成
            System.out.println("socket setup OK");

            //クライアント情報のサーバへの通知
            ArrayList<String> l = new ArrayList<String>();
            l.add("clientHostAndIpData");
            l.add(InetAddress.getLocalHost().getHostName());
            l.add(clientSocket.getLocalAddress().getHostAddress());/*TODO あとで考え直す*/

            this.sendObject(l);

            firstSetuped = true;
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    //ソケットとデータをもらい、送信するメソッド data はSerialize化してある
    public void sendObject(Object data) {
        try {
            System.out.println("socket = " + clientSocket);

            ObjectOutputStream ois = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream())); // 出力用のストリーム バッファ付き

            //while(!in.readLine().equals("CS")); //送信待ち
            ois.writeObject(data); // データの送信
            ois.flush();
            System.out.println("送信内容 : " + data);
        }catch (IOException e){
            e.printStackTrace();
            this.setup();
        }
    }

    //ソケットをもらい、受信するメソッド
    public Object getObject() throws IOException, ClassNotFoundException {
        Object receive = null;
        try {
            System.out.println("socket = " + clientSocket);

            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream())); // 出力用のストリーム バッファ付き

            receive = ois.readObject(); // データの受信

            System.out.println("受信内容 : " + receive);
        } catch (IOException e){
            e.printStackTrace();
            this.setup();
        }

        return receive; // TODO nullのときの処理
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ChatServerThread getCst() {
        return cst;
    }

    public void setCst(ChatServerThread cst) {
        this.cst = cst;
    }

    public void setChatSocket3001(ServerSocket chatSocket3001) {
        ChatSocket3001 = chatSocket3001;
    }

    public ServerSocket getChatSocket3001() {
        return ChatSocket3001;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public boolean isLogin() {
        return login;
    }

    public void setFirstSetuped(boolean firstSetuped) {
        this.firstSetuped = firstSetuped;
    }

    public boolean isFirstSetuped() {
        return firstSetuped;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setsSocket(ServerSocket sSocket) {
        this.sSocket = sSocket;
    }

    public ServerSocket getsSocket() {
        return sSocket;
    }

    public void setServ3002(ServerSocket serv3002) {
        this.serv3002 = serv3002;
    }

    public ServerSocket getServ3002() {
        return serv3002;
    }

}
