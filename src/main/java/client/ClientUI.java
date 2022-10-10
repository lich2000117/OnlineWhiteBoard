package client;

import ComponentGUI.WhiteBoardComponent;
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

    public void drawShape(WhiteBoardComponent.shapeMode mode, int x1, int y1, int x2, int y2, float brushSize, boolean filling, int rgb){
        this.frame.getWhiteBoard().drawShape(mode, x1, y1, x2, y2, brushSize, filling, rgb);
        System.out.println("Draw on Board");
    }

    public void sendChatMessage(String username, String message){
        this.frame.getChatPanel().addMessage(username, message);
    }

    public void addMessageToChat(String m){

    }
}
