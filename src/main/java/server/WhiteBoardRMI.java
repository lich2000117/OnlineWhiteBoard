package server;

import remote.iClient;
import remote.iServer;

import java.net.MalformedURLException;
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
        User usr;
        // if first enter the room, make it manager
        if (userList.size()==0) {
            usr = new User(name, newClient, User.STATUS.MANAGER);}
        else {
            usr = new User(name, newClient); }
        userList.add(usr);
        System.out.println("Successfully add User to server, user Status: " + usr.status);
        return true;
    }

    @Override
    public Void broadCastChat(String t) throws RemoteException {
        // notify every user to update their chatbox
        for (User u:userList){
            if (checkApproved(u)) {
                //u.client.addMessage(t);
            }
        }
        return null;
    }

    @Override
    public void broadDrawRectangle() throws RemoteException {
        for (User u:userList){
            u.client.local_drawRectangle();
        }
    }

    // check if user is authenticated
    private boolean checkApproved(User u){
        if (u.status!= User.STATUS.WAITING){
            return true;
        }
        return false;
    }

    // check if it's manager
    private boolean checkManager(User u){
        if (u.status == User.STATUS.MANAGER){
            return true;
        }
        return false;
    }
}
