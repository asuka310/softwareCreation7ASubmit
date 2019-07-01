package server_client.client;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class myClientForMultiThread {
    static int i = 0;
    int no;
    private InputStream is;
    private OutputStream os;

    private BufferedReader in;
    private PrintWriter out;

/*
    public myClientForMultiThread(){
        no = i++;
        ClientSocketData.setup();
        System.out.println("socket = " + ClientSocketData.getClientSocket());

        try {
            is = ClientSocketData.getClientSocket().getInputStream();
            os = ClientSocketData.getClientSocket().getOutputStream();


            in = new BufferedReader(new InputStreamReader(is));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)), true);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
*/

/*
    public static void main(String[] args) throws IOException {
        new myClientForMultiThread().requestForServer("AI時代のシステムに何ができるのか.pdf");
        //for(int i = 0; i < 10; i++) {
        //    new myClientForMultiThread().requestForServer("profile.txt");
        //    new myClientForMultiThread().requestForServer("3min_cooking.txt");
        //}
    }
*/

/*
    public void updateStream(){
        try {
            is = ClientSocketData.getInputStream();
            os = ClientSocketData.getOutputStream();


            in = new BufferedReader(new InputStreamReader(is));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)), true);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("error while getting Stream");
        }
    }
*/

/*
    private void requestForServer(String contains) throws IOException{
        Socket socket = ClientSocketData.getClientSocket();
        try{
            //System.out.println("socket = " + socket);

            //is = socket.getInputStream();
            //os = socket.getOutputStream();

            //in = new BufferedReader(new InputStreamReader(is));
            //out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)), true);

            List<String> l = new LinkedList<String>();
            l.add(contains);

            //リクエストの送信
            System.out.println("send request to Server\n");
            sendRequest(socket, l);


            //ヘッダを待つ
            System.out.println("waiting for header from Server");
            Map<String, Object> header = new HashMap<String, Object>();
            header = getHeader(socket);
            System.out.println("receive header from Server\n");

            //ヘッダ受信の終了を送信
            System.out.println("send header received message to Server");
            l.clear();
            l.add("OK");
            System.out.println(l);
            sendRequest(socket, l);
            System.out.println("finish sending header received message to Server\n");


            // データを受信
            System.out.println("getting data from Server");
            receiveData(socket, header);
            System.out.println("success receiving data from Server\n\n");

            out.println("CS");

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            System.out.println("finish...");
            //System.out.println("closing...");
            //socket.close();
        }
    }


    //サーバにリクエスト、OKを送る
    public void sendRequest(Socket s, List<String> data) throws IOException{
        //PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)), true); // 送信用のストリーム
        //System.out.println("sendReq"); //送信待ち
        String str;
        //System.out.println("str : " + str);
        //do {
        //    System.out.println("waiting CS"); //送信待ち
        //    str = in.readLine();
        //}while(!str.equals("CS"));
        for(String l: data) {
            out.println(l); // データの送信
            System.out.println("送信データ : " + l);
        }

        out.println("END"); // データ送信の終了を通知
    }

    // サーバからのヘッダを待つ
    // ここでデータがなかった場合とかの処理ができれば素敵
    // ここではmap, string の順番で送られてくることを想定
    // この想定が外せたら素敵
    private Map<String, Object> getHeader(Socket s) throws IOException{
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is));

        Map<String, Object> data = new HashMap<String, Object>();

        List<Object> l = new LinkedList<Object>();

        out.println("CS");
        try {
            data = (Map<String, Object>) ois.readObject(); // ヘッダの受信
            String str = (String) ois.readObject(); // 終了の受信
            System.out.println(data + str);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return data; // 受信に失敗したら空のMapが返される
    }

    // サーバからデータを受け取る
    private void receiveData(Socket s, Map<String, Object> header) throws IOException{
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is));

        System.out.println(header);
        byte[] bytes = new byte[Integer.parseInt(header.get("size").toString())];

        out.println("CS");
        try {
            bytes = (byte[]) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //このあと保存処理など
        System.out.println(bytes.length);

        String header_title = header.get("title").toString();
        int lastSeparatorIndex = header_title.lastIndexOf(".");
        String name = header_title.substring(0, lastSeparatorIndex);
        String extension = header_title.substring(lastSeparatorIndex);

        String path = "./src/server_client/" + name;

        if (new File(path + extension).exists()){
            int prefix = 1;
            while(new File(path + "_" + prefix + extension).exists())
                prefix++;
            path = path + "_" + prefix + extension;
        }

        BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(path));
        writer.write(bytes);
        writer.close();

    }

    //ソケットとデータをもらい、送信するメソッド data はSerialize化してある
    public void sendObject(Socket s, Object data) throws IOException{

        ObjectOutputStream ois = new ObjectOutputStream(new BufferedOutputStream(os)); // 出力用のストリーム バッファ付き

        //while(!in.readLine().equals("CS")); //送信待ち
        ois.writeObject(data); // データの送信
        ois.flush();
        System.out.println("送信内容 : " + data);

        //ois.writeObject("END"); // データ送信の終了を通知
        //ois.flush();

    }

    public String waitStringData() throws IOException{
        return in.readLine();
    }
*/
}
