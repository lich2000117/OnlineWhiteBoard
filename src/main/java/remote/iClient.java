package remote;

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
    boolean addMessage(String message) throws RemoteException;
}
