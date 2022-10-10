package server;

import ComponentGUI.WhiteBoardComponent;
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

    // To store a list of history methods for new users to run.
    private ArrayList<MethodRunner> history_methods = new ArrayList<>();

    protected WhiteBoardRMI() throws RemoteException {
        super();
    }


    @Override
    public UserSTATUS addUser(String name, String lookUpURL) throws RemoteException {
        iClient newClient;
        try {
            newClient = (iClient)Naming.lookup(lookUpURL);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
            return UserSTATUS.ERROR;
        }
        User usr;
        // if first enter the room, make it manager
        if (userList.size()==0) {
            usr = new User(name, newClient, UserSTATUS.MANAGER.MANAGER);}
        else {
            usr = new User(name, newClient);

            // if not manager, load and catchup with current progress
            for (MethodRunner mr : history_methods) {
                mr.run(usr);
            }
        }

        userList.add(usr);
        System.out.println("Successfully add User to server, user Status: " + usr.status);
        return usr.status;
    }

    @Override
    public void broadCastChat(String username, String t) throws RemoteException {
        // notify every user to update their chatbox
        for (User u:userList){
            if (checkApproved(u)) {
                u.client.local_sendChatMessage(username, t);
            }
        }
        // add all executed methods into an arraylist of history
        history_methods.add(new MethodRunner() {
            @Override
            public void run(User u) throws RemoteException {
                u.client.local_sendChatMessage(username, t);
            }
        });
    }

    @Override
    public void broadDrawShape(WhiteBoardComponent.shapeMode mode, int x1, int y1, int x2, int y2) throws RemoteException {
        for (User u:userList){
            u.client.local_drawShape(mode, x1, y1, x2, y2);
        }
        // add all executed methods into an arraylist of history
        history_methods.add(new MethodRunner() {
            @Override
            public void run(User u) throws RemoteException {
                u.client.local_drawShape(mode, x1, y1, x2, y2);
            }
        });
    }

    // check if user is authenticated
    private boolean checkApproved(User u){
        if (u.status!= UserSTATUS.WAITING){
            return true;
        }
        return false;
    }

    // check if it's manager
    private boolean checkManager(User u){
        if (u.status == UserSTATUS.MANAGER){
            return true;
        }
        return false;
    }
}
