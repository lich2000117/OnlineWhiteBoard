package server;

import client.Client;
import remote.iServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String args[]) {

        // register self RMI for clients
        try {
            iServer whiteBoardRMI = new WhiteBoardRMI();
            int port = 2000;
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("whiteboardrmi", whiteBoardRMI);
            System.out.println("Server listing on Port: "+ port);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
