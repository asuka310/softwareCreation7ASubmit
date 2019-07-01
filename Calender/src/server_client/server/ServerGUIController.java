package server_client.server;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Map;

public class ServerGUIController {
    public TableColumn host_name_tableID;
    public TableColumn ip_tableID;
    public TableView connecting_user_table;
    public TableColumn user_tableID;
    public TableColumn passwordID;
    public TableView user_name_password_table;
    public TableView file_data_table;
    public TableColumn file_name_tableID;
    public TableColumn file_path_tableID;
    public TableColumn file_size_tableID;
    public TableColumn login_statusID;
    public TableColumn login_user_nameID;

    @FXML
    void initialize() {
        host_name_tableID.setCellValueFactory(new PropertyValueFactory<UserData, String>("host_name_table"));
        ip_tableID.setCellValueFactory(new PropertyValueFactory<UserData, String>("ip_table"));
        login_user_nameID.setCellValueFactory(new PropertyValueFactory<UserData, String>("login_user_name_table"));
        login_statusID.setCellValueFactory(new PropertyValueFactory<UserData, String>("login_status_table"));
        user_tableID.setCellValueFactory(new PropertyValueFactory<PasswordData, String>("user_name_table"));
        passwordID.setCellValueFactory(new PropertyValueFactory<PasswordData, String>("password_table"));
        file_name_tableID.setCellValueFactory(new PropertyValueFactory<FileData, String>("file_name_table"));
        file_path_tableID.setCellValueFactory(new PropertyValueFactory<FileData, String>("file_path_table"));
        file_size_tableID.setCellValueFactory(new PropertyValueFactory<FileData, String>("file_size_table"));
    }

    public void reloadConnectingUserTable(){
        connecting_user_table.getItems().clear();
        for(UserData ud : ServerDatabase.getInstance().getConnectingUserMap().values())
            connecting_user_table.getItems().add(ud);
        System.out.println("reload ConnectingUserTable");
        System.out.println(connecting_user_table);
    }

    public void reloadUserNamePasswordTable(){
        user_name_password_table.getItems().clear();
        for(Map.Entry<String, String> entry : ServerDatabase.getInstance().getPasswordMap().entrySet())
            user_name_password_table.getItems().add(new PasswordData(entry.getKey(), entry.getValue()));
        System.out.println("reload UserNamePasswordTable");
        System.out.println(user_name_password_table);
    }

    public void reloadFileDataTable(){
        file_data_table.getItems().clear();
        for(FileData fd : ServerDatabase.getInstance().getFileDataMap().values())
            file_data_table.getItems().add(fd);
        System.out.println("reload FileDataTable");
        System.out.println(file_data_table);
    }


}
