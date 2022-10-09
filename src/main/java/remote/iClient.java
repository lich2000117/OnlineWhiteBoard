package remote;

import ComponentGUI.WhiteBoardComponent;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * RMI Client interface.
 *
 * Every Client that wants to be controlled (called methods) from server,
 * needs to implement this.
 *
 * Author: Chenghao Li
 */

public interface iClient extends Remote {

    // tell server a message wants to be added, let server tell all users.
    boolean request_drawShape(WhiteBoardComponent.shapeMode mode, int x1, int y1, int x2, int y2) throws RemoteException;
    boolean local_drawShape(WhiteBoardComponent.shapeMode mode, int x1, int y1, int x2, int y2) throws RemoteException;

    boolean request_sendChatMessage(String username, String message) throws RemoteException;
    boolean local_sendChatMessage(String username, String message) throws RemoteException;
}
