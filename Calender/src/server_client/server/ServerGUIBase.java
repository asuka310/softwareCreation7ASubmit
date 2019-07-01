package server_client.server;

import javafx.fxml.FXMLLoader;

public class ServerGUIBase {
    private static FXMLLoader loader;

    public static void setLoader(FXMLLoader loader) {
        ServerGUIBase.loader = loader;
    }

    public static FXMLLoader getLoader() {
        return loader;
    }

    public static ServerGUIController getController(){
        return getLoader().getController();
    }
}
