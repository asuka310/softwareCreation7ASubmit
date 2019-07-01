package server_client.server;

import sample.MyEvents;
import sample.Profile;
import server_client.client.ClientSocketData;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;

public class ServerWorkerRunnable implements Runnable {
    private Socket s;
    private String clientName = null;
    //private InputStream is;
    //private OutputStream os;

    //private BufferedReader in;
    //private PrintWriter out;

    public ServerWorkerRunnable(Socket socket) {
        this.s = socket;
    }

    public void run() {
        // スレッドIDを出力する
        System.out.println("Connection accepted: " + s);
        System.out.println("Service Thread : " + Thread.currentThread().getId());
        try {
            try {
                while (true) {
                    //クライアントからの要求を受け取る
                    System.out.println("getting Client's request");

                    List<String> requestFromClient = new ArrayList<String>();

                    requestFromClient = (ArrayList<String>) getObject(s); // 要求の受信

                    System.out.println("request from client : " + requestFromClient + "\n");

                    String user;
                    String pass;
                    String hostname;
                    List<String> response = new ArrayList<String>();

                    switch (requestFromClient.get(0)) {
                        case "clientHostAndIpData":
                            // requestFromClient = {"clientHostAndIpData", "hostname", "ipAddress"}
                            ServerDatabase.getInstance().registerConnectingUser(requestFromClient.get(1), requestFromClient.get(2));
                            this.clientName = requestFromClient.get(1);
                            System.out.println("add connecting user :: host : " + requestFromClient.get(1) + " ipAddress : " + requestFromClient.get(2));
                            break;

                        case "registerData": // ユーザ登録
                            // requestFromClient = {"registerData", "username", "password"}

                            user = String.valueOf(requestFromClient.get(1));
                            pass = String.valueOf(requestFromClient.get(2));
                            if (ServerDatabase.getInstance().isContainsUserForRegister(user)) {
                                // ユーザ名が登録済みであることをユーザに示す
                                response.add("NG");
                                sendObject(s, response);
                                //out.println("NG");
                                System.out.println("failed to register :: user : " + user + " pass : " + pass);
                            } else {
                                ServerDatabase.getInstance().registerUserPassword(user, pass);
                                response.add("OK");
                                sendObject(s, response);
                                System.out.println("registered :: user : " + user + " pass : " + pass);
                                //out.println("OK");
                            }
                            break;

                        case "authenticateData": // ユーザ認証
                            // requestFromClient = {"authenticateData", "username", "password", "hostname"}

                            user = String.valueOf(requestFromClient.get(1));
                            pass = String.valueOf(requestFromClient.get(2));
                            hostname = String.valueOf(requestFromClient.get(3));
                            if (!ServerDatabase.getInstance().isContainsUserForRegister(user)) {
                                // ユーザ名が未登録であることをユーザに示す
                                //out.println("NON");
                                response.add("NON");
                                sendObject(s, response);
                                System.out.println("no such a username :: user : " + user + " pass : " + pass);
                            } else if (ServerDatabase.getInstance().checkPassword(user, pass)) {
                                if(ServerDatabase.getInstance().getLoginUserMap().containsValue(user)){
                                    response.add("ALREADY");
                                    sendObject(s, response);
                                    System.out.println("already login :: user : " + user + " pass : " + pass);
                                }else {
                                    //out.println("OK");
                                    response.add("OK");
                                    sendObject(s, response);
                                    ServerDatabase.getInstance().addLoginUser(user, hostname); // ログイン中のユーザ登録
                                    System.out.println("match!! :: user : " + user + " pass : " + pass);
                                }
                            } else {
                                //out.println("NG");
                                response.add("NG");
                                sendObject(s, response);
                                System.out.println("Userdata has not matched on server :: user : " + user + " pass : " + pass);
                            }
                            break;

                        case "registerFile":
                            // requestFromClient = {"registerFile", "name", "size"}
                            System.out.println("データの受信を開始");
                            byte[] data = (byte[]) getObject(s); // データの受信
                            System.out.println("データの受信を完了");
                            ServerDatabase.getInstance().registerFileData(data, requestFromClient.get(1), requestFromClient.get(2)); // データの登録
                            break;

                        case "getFileData":
                            // requestFromClient = {"getFileData"}
                            System.out.println("データの送信を開始");
                            sendObject(s, ServerDatabase.getInstance().getFileDataMap());
                            System.out.println("データの送信を完了");
                            break;

                        case "downloadFile":
                            // requestFromClient = {"downloadFile", "fileTitle"}
                            String filePath_string = ServerDatabase.getInstance().getFileDataMap().get(requestFromClient.get(1)).getPath();
                            File f = new File(filePath_string); //TODO ファイルがない場合の処理
                            System.out.println("file path : " + filePath_string);

                            byte[] file_data = Files.readAllBytes(f.toPath());
                            System.out.println("データの送信を開始");
                            sendObject(s, file_data);
                            System.out.println("データの送信を完了");
                            break;

                        case "registerEvent":
                            // requestFromClient = {"registerEvent"}
                            System.out.println("データの受信を開始");
                            MyEvents event = (MyEvents) getObject(s); // データの受信
                            System.out.println("データの受信を完了");
                            ServerDatabase.getInstance().addAllEventMap(event);
                            break;

                        case "getEventOfMonth":
                            // requestFromClient = {"getEventOfMonth", year, month}
                            int year = Integer.parseInt(requestFromClient.get(1));
                            int month = Integer.parseInt(requestFromClient.get(2));
                            System.out.println("prepare data from year : " + year + "  month:" + (month - 1) + " to " + (month + 1));
                            HashMap<String, ArrayList<MyEvents>> sendEvents = ServerDatabase.getInstance().getEventOfMonth(year, month);
                            System.out.println("データの送信を開始");
                            sendObject(s, sendEvents);
                            System.out.println("データの送信を完了");
                            break;

                        case "getChatData":
                            // requestFromClient = {"getChatData", "hostname"}
                            System.out.println("チャット可能なユーザのリストを送信開始");
                            hostname = String.valueOf(requestFromClient.get(1));
                            sendObject(s, ServerDatabase.getInstance().getAvailableUserList(hostname));
                            System.out.println("データの送信を完了");
                            break;

                        case "registerUserProfile":
                            // requestFromClient = {"registerUserProfile", "username"}
                            user = requestFromClient.get(1);
                            System.out.println("プロフィールの受信開始");
                            Profile profile = (Profile) getObject(s);
                            System.out.println("プロフィールの受信完了");
                            //サーバへの登録
                            ServerDatabase.getInstance().setUserProfileMap(user, profile);
                            System.out.println("サーバへのプロフィールの保存完了");

                            break;

                        case "getUserProfile":
                            // requestFromClient = {"getUserProfile", "username"}
                            user = requestFromClient.get(1);
                            System.out.println("プロフィールの送信開始");
                            sendObject(s, ServerDatabase.getInstance().getUserProfile(user));
                            System.out.println("プロフィールの送信完了");
                            break;

                        case "logoutUser":
                            // requestFromClient = {"logoutUser", "hostname", "username"}
                            hostname = requestFromClient.get(1);
                            user = requestFromClient.get(2);
                            ServerDatabase.getInstance().removeLoginUser(user, hostname);
                            System.out.println("ログアウト : " + hostname + " " + user);
                            System.out.println(ServerDatabase.getInstance().getLoginUserMap().entrySet());
                            break;

                        case "removeEvent":
                            // requestFromClient = {"removeEvent"}
                            System.out.println("データの受信を開始");
                            MyEvents event2 = (MyEvents) getObject(s); // データの受信
                            System.out.println("データの受信を完了");
                            ServerDatabase.getInstance().removeAllEventMap(event2);
                            break;

                        default:
                            System.out.println("no such a request :: " + requestFromClient.get(0));
                    }


                }
            } catch (IOException e) {
                System.out.println(e);
                System.out.println("error 03");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                System.out.println("session closing...");
                //接続中クライアントから除去
                ServerDatabase.getInstance().removeConnectingUser(this.clientName);

                s.close();
                //ClientSocketData.setup();
            }
        } catch (IOException e) {
            System.out.println("error 04");
            System.out.println(e);
        }
    }
    //ソケットとデータをもらい、送信するメソッド data はSerialize化してある
    private void sendObject(Socket s, Object data) throws IOException {
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