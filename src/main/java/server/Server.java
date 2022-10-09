package server;

import remote.iFunction;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String args[]) {
        try {
            iFunction remoteCalculator = new Calculator();
            int port = 2000;
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("calculator", remoteCalculator);
            System.out.println("Server listing on Port: "+ port);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
