package remote;

import client.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * RMI Server interface.
 *
 * Every methods of server that wants to be controlled (called remotely) from other peers,
 * needs to implement this.
 *
 * Author: Chenghao Li
 */
public interface iServer extends Remote {
    Boolean addUser(String name, String rmiURL) throws RemoteException;
    Void broadCastChat(String t) throws RemoteException;
}
