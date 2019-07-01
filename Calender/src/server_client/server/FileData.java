package server_client.server;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class FileData implements Serializable {
    String title;
    String path;
    int size;

    //private StringProperty file_name_table; // table用
    //private StringProperty file_path_table; // table用
    //private IntegerProperty file_size_table; // table用

    public FileData(){}

    public FileData(String title, String path, int size){
        this.title = title;
        this.path = path;
        this.size = size;

        //this.file_name_table = new SimpleStringProperty(title);
        //this.file_path_table = new SimpleStringProperty(path);
        //this.file_size_table = new SimpleIntegerProperty(size);
    }

    // table用
    public StringProperty file_name_tableProperty() {
        return new SimpleStringProperty(title);
    }

    // table用
    public StringProperty file_path_tableProperty() {
        return new SimpleStringProperty(path);
    }

    // table用
    public IntegerProperty file_size_tableProperty() {
        return new SimpleIntegerProperty(size);
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }
}
