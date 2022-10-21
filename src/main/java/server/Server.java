package server;

import client.Client;
import remote.iServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String args[]) {
        String self_IP = "192.168.0.32";
        String self_Port = String.valueOf(2005);
        if (args.length==2){
            self_IP = args[0];
            self_Port = args[1];
        }
        for (String s: args){
            System.out.println(s);
        }
        // register self RMI for clients, self restart.
        while (true) {
            try {
                System.setProperty("java.rmi.server.hostname",self_IP);
                iServer whiteBoardRMI = new WhiteBoardRMI();
                Registry registry = LocateRegistry.createRegistry(Integer.parseInt(self_Port));
                registry.bind("whiteboardrmi", whiteBoardRMI);
                System.out.println("Server listing");
                System.out.println();
                break;
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println(e.getMessage());
                System.out.println("RMI Server encountered some issues. Please Try Again.");
            }
        }
    }
}
