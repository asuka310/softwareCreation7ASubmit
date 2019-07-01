package server_client.server;

import sample.MyEvents;
import sample.Profile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class ServerDatabase {
    private Map<String, UserData> connectingUserMap = new HashMap<String, UserData>();
    private Map<String, String> passwordMap = new HashMap<String, String>();
    private Map<String, FileData> fileDataMap = new HashMap<String, FileData>();
    private Map<String, String> loginUserMap = new HashMap<String, String>();
    private Map<String, ArrayList<MyEvents>> allEventMap = new HashMap<String, ArrayList<MyEvents>>(); // キーは(年)-(月)-(日)
    private Map<String, Profile> userProfileMap = new HashMap<String, Profile>();

    private static ServerDatabase sd = new ServerDatabase();

    private ServerDatabase(){
        System.out.println("ServerDatabaseのインスタンスを作成");
    }

    public static ServerDatabase getInstance(){
        return sd;
    }

    public void setup(){
        loadPasswordMap();
        loadFileDataMap();
        loadUserProfileMap();
        loadAllEventMap();
        ServerGUIBase.getController().reloadUserNamePasswordTable();
        ServerGUIBase.getController().reloadFileDataTable();
        // TODO eventリストの読み込み
    }

    public void registerConnectingUser(String host_name, String ip){
        connectingUserMap.put(host_name, new UserData(host_name, ip)); // データの登録
        ServerGUIBase.getController().reloadConnectingUserTable();//接続中ユーザのテーブルの読み直し
        System.out.println("register connecting user :: " + host_name + " : " + ip);
    }

    public void removeConnectingUser(String host){
        UserData ud = connectingUserMap.remove(host);
        if(loginUserMap.containsKey(host))
            loginUserMap.remove(host);
        ServerGUIBase.getController().reloadConnectingUserTable();//接続中ユーザのテーブルの読み直し
        System.out.println("lost connection with user :: " + ud.host_name + " : " + ud.ip);
    }

    public void registerUserPassword(String user, String pass){
        passwordMap.put(user, pass); // パスワードの登録
        savePasswordMap();
        ServerGUIBase.getController().reloadUserNamePasswordTable();
        System.out.println(passwordMap);
    }

    public void registerFileData(byte[] data, String title, String size){
        String filePath_string = "./src/server_client/server/contains/save_data/files/" + title;
        Path filePath = Paths.get(filePath_string);
        try {
            if (Files.exists(filePath)) { // ファイルが存在したとき
                String suffix = title.substring(0, title.lastIndexOf("."));
                String pathBase = "./src/server_client/server/contains/save_data/files/" + suffix;
                String prefix = title.substring(title.lastIndexOf("."), title.length()); // .を含む
                int index = 1;
                while(Files.exists(Paths.get(pathBase + "_" + index + prefix)))
                    index++;
                filePath = Paths.get(pathBase + "_" + index + prefix);
                title = suffix + "_" + index + prefix;
            }
            Files.write(filePath, data);
            fileDataMap.put(title, new FileData(title, filePath_string, Integer.parseInt(size)));
            System.out.println("データ登録完了 :: title " + title + " : path " + filePath.toString() + " : size " + size);
            saveFileDataMap(); // マップの保存
            ServerGUIBase.getController().reloadFileDataTable();
        }catch(IOException e){
            e.printStackTrace(); //TODO 例外発生時はユーザに通知
        }
    }

    public boolean checkPassword(String user, String pass){
        return passwordMap.containsKey(user) && passwordMap.get(user).equals(pass); //パスワードの一致を確認
    }

    public boolean isContainsUserForRegister(String user){
        return passwordMap.containsKey(user);
    }

    public boolean isConnectingUser(String user){
        return connectingUserMap.containsKey(user);
    }

    public Map<String, UserData> getConnectingUserMap() {
        return connectingUserMap;
    }

    public Map<String, String> getPasswordMap() {
        return passwordMap;
    }

    public Map<String, FileData> getFileDataMap() {
        return fileDataMap;
    }

    private void savePasswordMap(){
        String filePath_string = "./src/server_client/server/contains/save_data/passwordMap/passwordMap.ser";
        Path filePath = Paths.get(filePath_string);

        ObjectOutputStream os = null;
        try {
            if (Files.exists(filePath)) { // ファイルが存在したとき
                String createTime = Files.getAttribute(filePath, "creationTime").toString();
                createTime = createTime.replace(":", "-").replace(".", "-");

                String filePath_old_string = "./src/server_client/server/contains/save_data/passwordMap/old/passwordMap-" + createTime + ".ser";
                Path filePathOld = Paths.get(filePath_old_string);

                Files.move(filePath, filePathOld); // アーカイブ化
            }

            os = new ObjectOutputStream(new FileOutputStream(Files.createFile(filePath).toFile()));
            os.writeObject(passwordMap); // 書き込み

            System.out.println("save :: passwordMap");
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }


    }

    private void loadPasswordMap(){
        String filePath_string = "./src/server_client/server/contains/save_data/passwordMap/passwordMap.ser";

        Path filePath = Paths.get(filePath_string);

        ObjectInputStream ois = null;
        try{
            if (Files.exists(filePath)) { // ファイルが存在したとき
                ois = new ObjectInputStream(new FileInputStream(filePath.toFile()));

                passwordMap = (HashMap<String, String>) ois.readObject(); // 読み込み
                System.out.println("load :: passwordMap");
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void saveFileDataMap(){
        String filePath_string = "./src/server_client/server/contains/save_data/fileDataMap/fileDataMap.ser";
        Path filePath = Paths.get(filePath_string);

        ObjectOutputStream os = null;
        try {
            if (Files.exists(filePath)) { // ファイルが存在したとき
                String createTime = Files.getAttribute(filePath, "creationTime").toString();
                createTime = createTime.replace(":", "-").replace(".", "-");

                String filePath_old_string = "./src/server_client/server/contains/save_data/fileDataMap/old/fileDataMap-" + createTime + ".ser";
                Path filePathOld = Paths.get(filePath_old_string);

                Files.move(filePath, filePathOld); // アーカイブ化
            }

            os = new ObjectOutputStream(new FileOutputStream(Files.createFile(filePath).toFile()));
            os.writeObject(fileDataMap); // 書き込み

            System.out.println("save :: fileDataMap");
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }


    }

    private void loadFileDataMap(){
        String filePath_string = "./src/server_client/server/contains/save_data/fileDataMap/fileDataMap.ser";

        Path filePath = Paths.get(filePath_string);

        ObjectInputStream ois = null;
        try{
            if (Files.exists(filePath)) { // ファイルが存在したとき
                ois = new ObjectInputStream(new FileInputStream(filePath.toFile()));

                fileDataMap = (HashMap<String, FileData>) ois.readObject(); // 読み込み
                System.out.println("load :: fileDataMap");
                System.out.println(fileDataMap);
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void saveAllEventMap(){
        String filePath_string = "./src/server_client/server/contains/save_data/allEventMap/allEventMap.ser";
        Path filePath = Paths.get(filePath_string);

        ObjectOutputStream os = null;
        try {
            if (Files.exists(filePath)) { // ファイルが存在したとき
                String createTime = Files.getAttribute(filePath, "creationTime").toString();
                createTime = createTime.replace(":", "-").replace(".", "-");

                String filePath_old_string = "./src/server_client/server/contains/save_data/allEventMap/old/allEventMap-" + createTime + ".ser";
                Path filePathOld = Paths.get(filePath_old_string);

                Files.move(filePath, filePathOld); // アーカイブ化
            }

            os = new ObjectOutputStream(new FileOutputStream(Files.createFile(filePath).toFile()));
            os.writeObject(allEventMap); // 書き込み

            System.out.println("save :: allEventMap");
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }


    }

    private void loadAllEventMap(){
        String filePath_string = "./src/server_client/server/contains/save_data/allEventMap/allEventMap.ser";

        Path filePath = Paths.get(filePath_string);

        ObjectInputStream ois = null;
        try{
            if (Files.exists(filePath)) { // ファイルが存在したとき
                ois = new ObjectInputStream(new FileInputStream(filePath.toFile()));

                allEventMap = (HashMap<String, ArrayList<MyEvents>>) ois.readObject(); // 読み込み
                System.out.println("load :: allEventMap");
                System.out.println(allEventMap);
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    // TODO イベントリストの保存と読み込み用の関数の作成

    public void addLoginUser(String user, String hostname){
        connectingUserMap.get(hostname).setLoginUser(user, true);
        ServerGUIBase.getController().reloadConnectingUserTable();
        loginUserMap.put(hostname, user);
    }

    public void removeLoginUser(String user, String hostname){
        connectingUserMap.get(hostname).setLoginUser("Non", false);
        ServerGUIBase.getController().reloadConnectingUserTable();
        loginUserMap.remove(hostname, user);
    }

    public ArrayList<ArrayList<String>> getAvailableUserList(String hostname){
        ArrayList<ArrayList<String>> l = new ArrayList<ArrayList<String>>();
        ArrayList<String> data = new ArrayList<String>();

        for(Map.Entry<String, String> entry : loginUserMap.entrySet()){
            if(entry.getKey().equals(hostname)) // 自分自身はいらない
                continue;

            data.add(entry.getValue()); // username
            data.add(connectingUserMap.get(entry.getKey()).ip); // ip address
            System.out.println("add : " + data);
        }
        l.add(data);
        System.out.println("AvailableUserList : " + l);

        return l;
    }

    public Map<String, String> getLoginUserMap() {
        return loginUserMap;
    }

    public Map<String, ArrayList<MyEvents>> getAllEventMap() {
        return allEventMap;
    }

    public void addAllEventMap(MyEvents e) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String key = sdf.format(e.getS_time().getTime()); // 開始時間を基準に挿入する日を決定
        System.out.println(allEventMap.get(key));
        this.allEventMap.computeIfAbsent(key, k -> new ArrayList<MyEvents>());
        this.allEventMap.get(key).add(e); // TODO 日時順にソート
        System.out.println("add event : " + key + " " + e);
        saveAllEventMap();
    }

    public void removeAllEventMap(MyEvents e){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String key = sdf.format(e.getS_time().getTime()); // 開始時間を基準に挿入する日を決定
        System.out.println(allEventMap.get(key));
        if(this.allEventMap.containsKey(key)){
            for(MyEvents eve : allEventMap.get(key)) {
                if(eve.getTitle().equals(e.getTitle()) && eve.getS_time().equals(e.getS_time()) && eve.getE_time().equals(e.getE_time()) && eve.getPlace().equals(e.getPlace()) && eve.getDetail().equals(e.getDetail())) {
                    this.allEventMap.get(key).remove(eve); // そのイベントがあれば削除
                    System.out.println("remove event : " + key + " " + eve);
                }
            }
        }
        saveAllEventMap();
    }

    public HashMap<String, ArrayList<MyEvents>> getEventOfMonth(int yearIn, int monthIn){
        HashMap<String, ArrayList<MyEvents>> m = new HashMap<String, ArrayList<MyEvents>>();
        for(int month = monthIn - 1; month <= monthIn + 1; month++){ // 前後一ヶ月分
            for(int day = 1; day <= 31; day++) { // 面倒なので日にちは毎月31日とした
                String key = String.format("%04d", yearIn) + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
                if(allEventMap.containsKey(key)){
                    m.put(key, allEventMap.get(key));
                    System.out.println("append " + key + "" + allEventMap.get(key));
                }
            }
        }

        return m;
    }

    public void setUserProfileMap(String user, Profile profile) {
        this.userProfileMap.put(user, profile);
        saveUserProfileMap();
    }

    public Profile getUserProfile(String user){
        if(this.userProfileMap.containsKey(user))
            return this.userProfileMap.get(user);
        else
            return new Profile();
    }

    private void saveUserProfileMap(){
        String filePath_string = "./src/server_client/server/contains/save_data/UserProfileMap/UserProfileMap.ser";
        Path filePath = Paths.get(filePath_string);

        ObjectOutputStream os = null;
        try {
            if (Files.exists(filePath)) { // ファイルが存在したとき
                String createTime = Files.getAttribute(filePath, "creationTime").toString();
                createTime = createTime.replace(":", "-").replace(".", "-");

                String filePath_old_string = "./src/server_client/server/contains/save_data/UserProfileMap/old/UserProfileMap-" + createTime + ".ser";
                Path filePathOld = Paths.get(filePath_old_string);

                Files.move(filePath, filePathOld); // アーカイブ化
            }

            os = new ObjectOutputStream(new FileOutputStream(Files.createFile(filePath).toFile()));
            os.writeObject(userProfileMap); // 書き込み

            System.out.println("save :: UserProfileMap");
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }


    }

    private void loadUserProfileMap(){
        String filePath_string = "./src/server_client/server/contains/save_data/UserProfileMap/UserProfileMap.ser";

        Path filePath = Paths.get(filePath_string);

        ObjectInputStream ois = null;
        try{
            if (Files.exists(filePath)) { // ファイルが存在したとき
                ois = new ObjectInputStream(new FileInputStream(filePath.toFile()));

                userProfileMap = (HashMap<String, Profile>) ois.readObject(); // 読み込み
                System.out.println("load :: UserProfileMap");
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}

