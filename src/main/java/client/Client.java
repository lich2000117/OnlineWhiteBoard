package client;

import remote.iClient;
import remote.iServer;

import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements Serializable{
    private String Host_NamingServer;
    private String hostPort;
    private String self_port;
    private String remoteMethodName;
    private String thisRMIName;
    private ClientChatBox clientChatBox;
    private String name = "New User";

    protected Client(String IP, String port) throws RemoteException {
        this.Host_NamingServer = IP;
        this.hostPort = port;
        this.remoteMethodName = "whiteboardrmi";
        this.thisRMIName = "client";
        this.clientChatBox = new ClientChatBox();
    }

    public static void main(String [] args) {

        // Connect to server RMI
        String serverIP = "localhost";
        String port = "2000";
        Client client;
        try {
            client = new Client(serverIP, port);
        }
        catch (RemoteException e){
            e.printStackTrace();
            return;
        }
        client.connectToServer();

        // register self RMI for server
        iClient chatBox;
        try {
            chatBox = (iClient) new ClientChatBox();
            int self_port = 2005;
            Registry registry = LocateRegistry.createRegistry(self_port);
            registry.bind("chat", chatBox);
            System.out.println("Client RMI server listing on Port: "+ port);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public void connectToServer(){
        try{
            iServer whiteboardRMI = (iServer) Naming.lookup("rmi://" + this.Host_NamingServer + ":" + this.hostPort + "/" + this.remoteMethodName);

            System.out.println("connected to RMI!");

            // tell server to add me to its user list.
            String thisURL = "rmi://" + this.Host_NamingServer + ":" + this.hostPort + "/" + this.thisRMIName;
            whiteboardRMI.addUser(name, thisURL);
            System.out.println("User Added");
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
