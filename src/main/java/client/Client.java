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

/**
 * This class Client,
 * 1. set up connection to RMI server
 * 2. set up stub and get stub from whiteboard on RMI server.
 */

public class Client extends UnicastRemoteObject implements Serializable{
    private String NamingServerIP;
    private String NamingServerPort;
    private String remoteMethodName; // we want to get all methods from whiteboardrmi server.
    private String thisRMIName = "client";
    public String userName; //static, one client can have only one username
    public String SELF_RMI_ADDRESS;  // RMI address of this client instance, so we can add this client to RMI server's object list.

    protected Client(String userName, String NamingServerIP, String NamingServerPort) throws RemoteException {
        this.userName = userName;
        this.NamingServerIP = NamingServerIP;
        this.NamingServerPort = NamingServerPort;
        this.remoteMethodName = "whiteboardrmi";
        this.thisRMIName = userName;
    }

    public static void main(String [] args) {

        // 1. Connect to server RMI
        String NamingServerIP = "localhost";
        String NamingServerPort = "2000";
        Client client;
        String userName = args[0];
        try {
            client = new Client(userName, NamingServerIP, NamingServerPort);
        }
        catch (RemoteException e){
            e.printStackTrace();
            return;
        }
        // 2. set up RMI registry and // 3. set up RMI UI
        client.setUpRMIServer();

    }

    public void setUpRMIServer(){
        try{
            // 1. connect to WhiteBoard RMI from Naming Server
            iServer whiteboardRMI = (iServer) Naming.lookup("rmi://" + this.NamingServerIP + ":" + this.NamingServerPort + "/" + this.remoteMethodName);
            System.out.println("connected to whiteboard RMI!");

            // 2. register self RMI apis to naming server and // 3. set up RMI UI
            ClientRMI clientRMI = (ClientRMI) new ClientRMI(whiteboardRMI, userName); // 3. set up RMI UI and run gui
            SELF_RMI_ADDRESS = "rmi://" + this.NamingServerIP + ":" + this.NamingServerPort + "/" + this.thisRMIName;
            Naming.rebind(SELF_RMI_ADDRESS, clientRMI);
            // 4. add current Client to Whiteboard RMI server so whiteboard has access to call method defined in RMI.
            if (! clientRMI.addMeToWhiteBoardServer(userName, SELF_RMI_ADDRESS)){ return; }
            System.out.println("Client RMI registered to server!");
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



