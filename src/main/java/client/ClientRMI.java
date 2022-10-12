package client;

import ComponentGUI.LocalDrawBoardComponent;
import remote.iClient;
import remote.iServer;
import server.UserSTATUS;

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
    private String username;
    public UserSTATUS userStatus;
    private ClientUI clientUI;

    public ClientRMI(iServer whiteboard, String username) throws RemoteException {
        super();
        this.whiteboard = whiteboard;
        this.username = username;
        clientUI = new ClientUI(whiteboard, this);
    }

    // return true if success
    public boolean addMeToWhiteBoardServer(String userName, String RMI_URL){
        try {
            this.userStatus = whiteboard.handle_addUser(userName, RMI_URL);
            if (this.userStatus==UserSTATUS.ERROR){
                return false;
            };
        }
        catch (Exception e){
            System.out.println("Cannot Connect to Whiteboard Server.");
            e.printStackTrace();
            return false;
        }
        try {
            whiteboard.handle_broadCastChat("[System", "User: "+ userName+", has joined!]");
        }
        catch (Exception e){
            //ignore;
        }
        return true;
    }

    public boolean request_KickUser(String targetuserName){
        try {
            if (!whiteboard.kickUser(this.username, targetuserName)){
                // kick fail trying to kick yourself as a manager
                clientUI.displayAlert("Cannot Kick Manager!", false);
                return false;
            }
        }
        catch (Exception e){
//            System.out.println("Cannot Kick User.");
//            clientUI.displayAlert("Kick unknown Failed!", false);
//            e.printStackTrace();
//            return false;
        }
        try {
            whiteboard.handle_broadCastChat("[System", "User: " + targetuserName + ", was Kicked Out!]");
        }
        catch (Exception e){
            //ignore;
        }
        return true;
    }

    @Override
    public boolean local_beenKicked(String msg) throws RemoteException {
        this.clientUI.kickClient(msg);
        return true;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean local_managerApproveUser(String username) throws RemoteException {
        return clientUI.displayUserJoinRequest(username);
    }

    @Override
    public boolean request_drawShape(LocalDrawBoardComponent.shapeMode mode, int x1, int y1, int x2, int y2, float brushSize, boolean filling, int rgb) throws RemoteException {
        // 1. ask whiteboard to draw rectangle
        whiteboard.broadDrawShape(mode, x1, y1, x2, y2, brushSize, filling, rgb);
        System.out.println("Sent draw to Server WhiteBoard ");
        return true;
    }

    @Override
    public boolean local_drawShape(LocalDrawBoardComponent.shapeMode mode, int x1, int y1, int x2, int y2, float brushSize, boolean filling, int rgb) throws RemoteException {
        // 2. whiteboard call this function to draw local rectangle
        clientUI.drawShape(mode, x1, y1, x2, y2, brushSize, filling, rgb);
        System.out.println("Called by Server to draw local ");
        return true;
    }


    @Override
    public boolean request_drawPolygon(int[] lstX, int[] lstY, float brushSize, boolean filling, int rgb) throws RemoteException {
        // 1. ask whiteboard to draw rectangle
        whiteboard.broadDrawPolygon(lstX, lstY, brushSize, filling, rgb);
        System.out.println("Sent draw to Server WhiteBoard ");
        return true;
    }

    @Override
    public boolean local_drawPolygon(int[] lstX, int[] lstY, float brushSize, boolean filling, int rgb) throws RemoteException {
        // 2. whiteboard call this function to draw local rectangle
        clientUI.drawPolygon(lstX, lstY, brushSize, filling, rgb);
        System.out.println("Called by Server to draw local ");
        return true;
    }

    @Override
    public boolean request_drawFree(int[] lstX, int[] lstY, float brushSize, boolean filling, int rgb) throws RemoteException {
        // 1. ask whiteboard to draw rectangle
        whiteboard.broadDrawFree(lstX, lstY, brushSize, filling, rgb);
        System.out.println("Sent draw to Server WhiteBoard ");
        return true;
    }

    @Override
    public boolean local_drawFree(int[] lstX, int[] lstY, float brushSize, boolean filling, int rgb) throws RemoteException {
        // 2. whiteboard call this function to draw local rectangle
        clientUI.drawFree(lstX, lstY, brushSize, filling, rgb);
        System.out.println("Called by Server to draw local ");
        return true;
    }

    @Override
    public boolean request_drawText(String text, int x, int y) throws RemoteException {
        // 1. ask whiteboard to draw rectangle
        whiteboard.broadDrawText(text, x, y);
        System.out.println("Sent text to Server WhiteBoard ");
        return true;
    }

    @Override
    public boolean local_drawText(String text, int x, int y) throws RemoteException {
        // 2. whiteboard call this function to draw local rectangle
        clientUI.drawText(text, x , y);
        System.out.println("Called by Server to text local ");
        return true;
    }

    @Override
    public boolean request_sendChatMessage(String username, String message) throws RemoteException {
        whiteboard.handle_broadCastChat(username, message);
        return true;
    }

    @Override
    public boolean local_sendChatMessage(String username, String message) throws RemoteException {
        clientUI.sendChatMessage(username, message);
        return true;
    }

    @Override
    public boolean local_updateUserList(ArrayList<String> userList) throws RemoteException {
        clientUI.updateUserList(userList);
        return true;
    }
}
