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
    UserSTATUS handle_addUser(String name, String rmiURL) throws RemoteException;
    void handle_broadCastChat(String u, String t) throws RemoteException;
    void broadDrawShape(LocalDrawBoardComponent.shapeMode mode, int x1, int y1, int x2, int y2) throws RemoteException;
    boolean kickUser(String userName) throws RemoteException;
}
