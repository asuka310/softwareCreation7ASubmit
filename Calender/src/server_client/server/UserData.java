package server_client.server;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserData {
    String host_name;
    String ip;
    String login_user_name = "Non";
    boolean login = false;

    //private StringProperty host_name_table; // table用
    //private StringProperty ip_table; // table用

    UserData(String host_name, String ip){
        this.host_name = host_name;
        this.ip = ip;

        //this.host_name_table = new SimpleStringProperty(host_name);
        //this.ip_table = new SimpleStringProperty(ip);
    }

    public void setLoginUser(String user, boolean login) {
        this.login_user_name = user;
        this.login = login;
    }

    // table用
    public StringProperty host_name_tableProperty() {
        return new SimpleStringProperty(host_name);
    }

    // table用
    public StringProperty ip_tableProperty() {
        return new SimpleStringProperty(ip);
    }

    public StringProperty login_user_name_tableProperty() {
        return new SimpleStringProperty(login_user_name);
    }

    // table用
    public StringProperty login_status_tableProperty() {
        return new SimpleStringProperty(String.valueOf(login));
    }

}
