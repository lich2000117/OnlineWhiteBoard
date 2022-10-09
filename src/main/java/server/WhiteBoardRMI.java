package server;

import client.Client;
import remote.iClient;
import remote.iServer;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * A WhiteBoard RMI class, that implements all methods needed for clients to interact with server.
 * This class also stores STATE of the server (users, drawings etc).
 * Author: Chenghao Li
 */
public class WhiteBoardRMI extends UnicastRemoteObject implements iServer {

    private ArrayList<User> userList = new ArrayList<User>();
    protected WhiteBoardRMI() throws RemoteException {
        super();
    }


    @Override
    public Boolean addUser(String name, String lookUpURL) throws RemoteException {
        iClient newClient;
        try {
            newClient = (iClient)Naming.lookup(lookUpURL);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        userList.add(new User(name, newClient));
        return true;
    }

    @Override
    public Void broadCastChat(String t) throws RemoteException {
        // notify every user to update their chatbox
        for (User u:userList){
            u.client.addMessage(t);
        }
        return null;
    }
}
