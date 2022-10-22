package server;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * An interface that is used to be stored in an arraylist to
 * run each method once again for every new joined users.
 *
 * Every method that expect to run again for new users should implement this interface.
 *
 * It is preferred to use anonymous class in-line in the arraylist to use this method.
 */
public interface MethodRunner extends Remote, Serializable {
    public void run(User u) throws RemoteException;
}
