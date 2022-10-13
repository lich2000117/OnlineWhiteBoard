package server;

import client.Client;
import remote.iServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String args[]) {
        int port = 2000;
        if (args.length==1){
            port = Integer.parseInt(args[0]);
        }
        // register self RMI for clients, self restart.
        while (true) {
            try {
                iServer whiteBoardRMI = new WhiteBoardRMI();
                Registry registry = LocateRegistry.createRegistry(port);
                registry.bind("whiteboardrmi", whiteBoardRMI);
                System.out.println("Server listing on Port: " + port);
                break;
            } catch (AlreadyBoundException e) {
                System.out.println("RMI Server address already in use.");
            } catch (Exception e) {
                System.out.println("RMI Server encountered some issues.");
            }
        }
    }
}
