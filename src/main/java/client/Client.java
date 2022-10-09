package client;

import remote.iClient;
import remote.iServer;

import javax.swing.*;
import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements Serializable{
    private String NamingServerIP;
    private String NamingServerPort;
    private String remoteMethodName;
    private String thisRMIName;
    private ClientRMI clientRMI;
    private String name = "New User";

    protected Client(String NamingServerIP, String NamingServerPort) throws RemoteException {
        this.NamingServerIP = NamingServerIP;
        this.NamingServerPort = NamingServerPort;
        this.remoteMethodName = "whiteboardrmi";
        this.thisRMIName = "client";
        this.clientRMI = new ClientRMI();
    }

    public static void main(String [] args) {

        // Connect to server RMI
        String NamingServerIP = "localhost";
        String NamingServerPort = "2000";
        Client client;
        try {
            client = new Client(NamingServerIP, NamingServerPort);
        }
        catch (RemoteException e){
            e.printStackTrace();
            return;
        }
        client.connectToServer();



    }

    public void connectToServer(){
        try{
            // connect to WhiteBoard RMI from Naming Server
            iServer whiteboardRMI = (iServer) Naming.lookup("rmi://" + this.NamingServerIP + ":" + this.NamingServerPort + "/" + this.remoteMethodName);
            System.out.println("connected to RMI!");


            // register self RMI apis to naming server
            iClient chatBox;
            try {
                chatBox = (iClient) new ClientRMI();
                Naming.rebind("rmi://" + this.NamingServerIP + ":" + this.NamingServerPort + "/" + this.thisRMIName, chatBox);
                System.out.println("Client RMI registered to server!");
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // tell server to add me to its user list.
            String thisURL = "rmi://" + this.NamingServerIP + ":" + this.NamingServerPort + "/" + this.thisRMIName;
            whiteboardRMI.addUser(name, thisURL);
            System.out.println("User Added");
            whiteboardRMI.broadCastChat("NEW CHAT!");
        }
        catch (RemoteException | NotBoundException e) {
            // TODO Message should print to GUI
            System.out.println("Cannot Connect to RMI");
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
