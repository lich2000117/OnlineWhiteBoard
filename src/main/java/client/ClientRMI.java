package client;

import ComponentGUI.WhiteBoardComponent;
import remote.iClient;
import remote.iServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class does not store state, it only stores Methods of local client,
 *
 * 1. request_: implements iClient to make it accessible to the server.
 * 2. local_: Server calls method in this class to modify client side.
 *
 * Author:Chenghao Li, Dimitri
 */

public class ClientRMI extends UnicastRemoteObject implements iClient {
    private iServer whiteboard;
    private String username;
    private ClientUI clientUI;

    protected ClientRMI(iServer whiteboard, String username) throws RemoteException {
        super();
        this.whiteboard = whiteboard;
        this.username = username;
        clientUI = new ClientUI(whiteboard, this);
    }

    public boolean addMeToWhiteBoardServer(String userName, String RMI_URL){
        try {
            whiteboard.addUser(userName, RMI_URL);
        }
        catch (Exception e){
            System.out.println("Cannot Connect to Whiteboard Server.");
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public String getUsername() {
        return username;
    }

    @Override
    public boolean request_drawShape(WhiteBoardComponent.shapeMode mode, int x1, int y1, int x2, int y2) throws RemoteException {
        // 1. ask whiteboard to draw rectangle
        whiteboard.broadDrawShape(mode, x1, y1, x2, y2);
        System.out.println("Sent draw to Server WhiteBoard ");
        return true;
    }

    @Override
    public boolean local_drawShape(WhiteBoardComponent.shapeMode mode, int x1, int y1, int x2, int y2) throws RemoteException {
        // 2. whiteboard call this function to draw local rectangle
        clientUI.drawShape(mode, x1, y1, x2, y2);
        System.out.println("Called by Server to draw local ");
        return true;
    }

    @Override
    public boolean request_sendChatMessage(String username, String message) throws RemoteException {
        whiteboard.broadCastChat(username, message);
        return true;
    }

    @Override
    public boolean local_sendChatMessage(String username, String message) throws RemoteException {
        clientUI.sendChatMessage(username, message);

        return true;
    }


}
