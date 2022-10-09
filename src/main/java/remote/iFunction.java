package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface iFunction extends Remote {
    String calculate(String message) throws RemoteException;
}
