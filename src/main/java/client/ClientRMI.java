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
        clientUI = new ClientUI(this);
    }

    /**
     * Add current local client to whiteboard RMI server (subscribe to whiteboard)
     * @param userName
     * @return
     */
    // return true if success
    public boolean addMeToWhiteBoardServer(String userName, String clientIP, int clientPort) throws RemoteException {
            this.userStatus = whiteboard.handle_addUser(userName, clientIP, clientPort);
            if (this.userStatus==UserSTATUS.ERROR){
                return false;
            }
        return true;
    }

    public boolean request_KickUser(String targetUserName){
        try {
            if (!whiteboard.kickUser(this.username, targetUserName)){
                // kick fail trying to kick yourself as a manager
                clientUI.displayAlert("Cannot Kick Manager!", false);
                return false;
            }
            clientUI.displayAlert("They GONE! CYA!", false);

        }
        catch (Exception e){
            clientUI.displayAlert("Server unstable, kick may not be successful.",false);
            return false;
        }
        return true;
    }

    /**
     * Inform the server this user is leaving.
     * @return
     */
    public boolean request_userLeave(){
        try {
            whiteboard.userLeave(this.username);
            System.out.println("Leave request sent to server");
            return true;
        } catch (Exception e) {
            // ignore, user quits anyway.
        }
        return false;
    }

    @Override
    public boolean local_beenKicked(String msg) throws RemoteException {
        /**
         * Need to start a new thread as server does not need to wait for local client to quit.
         * if no thread, this will block server's execution.
         */
        (new Thread(() -> kickClientUI(msg))).start();
        return true;
    }

    public void kickClientUI(String msg){
        clientUI.kickClient(msg);
    }

    public String getUsername() {
        return username;
    }

    @Override
    /**
     * Method for manager to approve user's join
     */
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
    public boolean request_drawText(String text, int x, int y, String name, int style, int size, int rgb) throws RemoteException {
        // 1. ask whiteboard to draw rectangle
        whiteboard.broadDrawText(text, x, y, name , style, size, rgb);
        System.out.println("Sent text to Server WhiteBoard ");
        return true;
    }

    @Override
    public boolean local_drawText(String text, int x, int y, String name, int style, int size, int rgb) throws RemoteException {
        // 2. whiteboard call this function to draw local rectangle
        clientUI.drawText(text, x , y, name, style, size, rgb);
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
