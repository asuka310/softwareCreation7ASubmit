package server_client.server;

import javafx.fxml.FXMLLoader;

public class ServerGUIBase {
    private static FXMLLoader loader;

    private static ServerGUIBase serverGUIBase = new ServerGUIBase();

    public static ServerGUIBase getInstance(){
        return serverGUIBase;
    }


    public void setLoader(FXMLLoader loader) {
        ServerGUIBase.loader = loader;
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public ServerGUIController getController(){
        return getLoader().getController();
    }
}
