package client;

import remote.iClient;
import remote.iServer;

public class ClientUI {
    private MyFrame frame;
    private iServer whiteboard;
    private iClient clientRMI;  // use this RMI interface to interact with remote server.

    public ClientUI(iServer whiteboard, ClientRMI clientRMI) {
        this.clientRMI = clientRMI;
        this.frame = new MyFrame(clientRMI);
    }

    public void drawRectangle(){
        this.frame.drawRectangle_Board();
        System.out.println("Draw on Board");
    }

    public void addMessageToChat(String m){

    }
}
