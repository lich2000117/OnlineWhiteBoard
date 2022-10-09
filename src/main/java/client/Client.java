package client;

import remote.iFunction;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements Serializable{
    private String hostName;
    private String hostPort;
    private String remoteMethodName;

    protected Client(String IP, String port) throws RemoteException {
        this.hostName = IP;
        this.hostPort = port;
        this.remoteMethodName = "calculator";

    }

    public static void main(String [] args) {
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
        client.connectToRMI();
    }

    public void connectToRMI(){
        try{
            iFunction calculator = (iFunction) Naming.lookup("rmi://" + this.hostName + ":" + this.hostPort + "/" + this.remoteMethodName);

            System.out.println("connected to RMI!");
            String out = calculator.calculate("TestMessage");
            System.out.println("Received Message: " + out);
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
