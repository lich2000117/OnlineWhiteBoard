package client;

import remote.iClient;
import remote.iServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

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
    private ClientUI clientUI;

    protected ClientRMI(iServer whiteboard) throws RemoteException {
        super();
        this.whiteboard = whiteboard;
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


    @Override
    public boolean request_drawRectangle() throws RemoteException {
        // 1. ask whiteboard to draw rectangle
        whiteboard.broadDrawRectangle();
        System.out.println("Sent draw to Server WhiteBoard ");
        return true;
    }

    @Override
    public boolean local_drawRectangle() throws RemoteException {
        // 2. whiteboard call this function to draw local rectangle
        clientUI.drawRectangle();
        System.out.println("Called by Server to draw local ");
        return true;
    }

    @Override
    public boolean request_AddChatMessage(String message) throws RemoteException {
        //whiteboard.broadCastChat()
        return true;
    }

    @Override
    public void drawLine(String m) throws RemoteException {
        System.out.println(m);
    }

}
