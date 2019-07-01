package server_client.server;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PasswordData {
    private StringProperty user_name_table; // table用
    private StringProperty password_table; // table用

    PasswordData(String user, String pass){
        this.user_name_table = new SimpleStringProperty(user);
        this.password_table = new SimpleStringProperty(pass);
    }

    // table用
    public StringProperty user_name_tableProperty() {
        return user_name_table;
    }

    // table用
    public StringProperty password_tableProperty() {
        return password_table;
    }
}
