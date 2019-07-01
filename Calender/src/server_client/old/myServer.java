package server_client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class myServer {
    public static void main(String[] args)  throws IOException  {
        String name = "";
        String pass = "";
        int PORT = 3000; // ポート番号を設定する
        System.out.println(PORT);

        //String path = "./tw_data_marged_out_hirasawa.json";

        //String data = readAll(path);

        while(true) {
            ServerSocket s = new ServerSocket(PORT); // ソケットを作成する
            System.out.println("Started: " + s);
            try{
                Socket socket = s.accept(); // コネクション設定要求を待つ
                try {
                    System.out.println("Connection accepted: " + socket);

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // データ受信用のバッファの設定
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true); //送信バッファ設定
                    String flag = in.readLine();
                    StringBuilder at = new StringBuilder();
                    if(flag.equals("0")) {
                        File file = new File("testsend.txt");
                        FileWriter fw = new FileWriter(file);
                        FileReader fr = new FileReader(file);
                        BufferedWriter bw = new BufferedWriter(fw);
                        BufferedReader br = new BufferedReader(fr);
                        while(true) {
                            String str = in.readLine(); //データの受信
                            if (str.equals("END")) {
                                bw.newLine();
                                bw.close();
                                break;
                            }
                            bw.write(str);
                            System.out.println("Echoing : ");//データの送信
                            out.println("END");
                        }
                    }
                    if(flag.equals("1")){
                        File file = new File("testsend.txt");
                        FileWriter fw = new FileWriter(file,true);
                        FileReader fr = new FileReader(file);
                        BufferedWriter bw = new BufferedWriter(fw);
                        BufferedReader br = new BufferedReader(fr);
                        while(true) {
                            String str = in.readLine();
                            if (str.equals("END")) {
                                break;
                            }
                            at.append(str);
                            at.append(",");
                        }
                        String auth = new String(at);
                        System.out.println("Echoing : ");//データの送信
                        String match = br.readLine() + ",";
                        try {
                            if (match.equals(auth)) {
                                out.println("1");
                                br.close();
                                System.out.println("1");
                            } else if (!match.equals(auth)) {
                                out.println("0");
                                br.close();
                                System.out.println("0");
                            }
                        }catch (NullPointerException npe){
                            out.println("-1");
                            br.close();
                            System.out.println("-1");
                        }
                    }
                } finally {
                    System.out.println("closing...");
                    socket.close();
                }
            }finally {
                s.close();
            }
        }

    }


    public static String readAll(final String path) throws IOException {
        return Files.lines(Paths.get(path), Charset.forName("UTF-8"))
                .collect(Collectors.joining(System.getProperty("line.separator")));
    }
}

