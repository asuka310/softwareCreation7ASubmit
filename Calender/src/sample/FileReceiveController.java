package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import server_client.client.ClientSocketData;
import server_client.server.FileData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class FileReceiveController {
    public TableView file_name_size_tableID;
    public TableColumn file_name_table2ID;
    public TableColumn file_size_table2ID;

    private Stage stage;

    @FXML
    void initialize() {
        file_name_table2ID.setCellValueFactory(new PropertyValueFactory<FileData, String>("file_name_table"));
        file_size_table2ID.setCellValueFactory(new PropertyValueFactory<FileData, Integer>("file_size_table"));

        new Thread(this::reloadFileNameSizeTable).start(); // スレッドで処理

    }

    public void reloadFileNameSizeTable(){
        try {
            ArrayList<String> l = new ArrayList<String>();
            l.add("getFileData");
            ClientSocketData.getInstance().sendObject(l);// 要求の送信

            HashMap<String, FileData> fileDataMap = (HashMap<String, FileData>)ClientSocketData.getInstance().getObject(); // データの取得
            file_name_size_tableID.getItems().clear();
            for (FileData fd : fileDataMap.values())
                file_name_size_tableID.getItems().add(fd);
            System.out.println("reload FileNameSizeTable");
            System.out.println(file_name_size_tableID);
        }catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
        }
    }

    public void saveSelectedFile(ActionEvent actionEvent) {
        FileData fd = (FileData) file_name_size_tableID.getSelectionModel().getSelectedItem();
        System.out.println(fd.getTitle());

        try {
            ArrayList<String> l = new ArrayList<String>();
            l.add("downloadFile");
            l.add(fd.getTitle());
            ClientSocketData.getInstance().sendObject(l);// 要求の送信

            byte[] file_data = (byte[])ClientSocketData.getInstance().getObject(); // データの受信

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");
            fileChooser.setInitialFileName(fd.getTitle());
            File file = fileChooser.showSaveDialog( stage );
            if(file != null) {
                System.out.println("save at : " + file.getPath());
                Files.write(file.toPath(), file_data); // 保存
            }
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void closeWindow(ActionEvent actionEvent) {
        Scene scene = ((Node) actionEvent.getSource()).getScene();
        Window window = scene.getWindow();
        window.hide();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
