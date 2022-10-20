package remote;

import ComponentGUI.LocalDrawBoardComponent;
import server.UserSTATUS;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * RMI Server interface.
 *
 * Every methods of server that wants to be controlled (called remotely) from other peers,
 * needs to implement this.
 *
 * Author: Chenghao Li
 *
 */
public interface iServer extends Remote {
    UserSTATUS handle_addUser(String name, String clientIP, int clientPort) throws RemoteException;
    int CheckUserNameWith_Server(String name) throws RemoteException; // return 0 if approved, return 1 if username already taken, return 2 if manager does not approve
    void handle_broadCastChat(String u, String t) throws RemoteException;
    void broadDrawShape(LocalDrawBoardComponent.shapeMode mode, int x1, int y1, int x2, int y2, float brushSize, boolean filling, int rgb) throws RemoteException;

    void broadDrawPolygon(int[] lstX, int[] lstY, float brushSize, boolean filling, int rgb) throws RemoteException;
    void broadDrawFree(int[] lstX, int[] lstY, float brushSize, boolean filling, int rgb) throws RemoteException;
    void broadDrawText(String text, int x, int y, String name, int style, int size, int rgb) throws RemoteException;
    boolean kickUser(String subjectUser, String targetUser) throws RemoteException;
    boolean userLeave(String userName) throws RemoteException;
}
