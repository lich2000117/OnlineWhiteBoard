package server;

import ComponentGUI.LocalDrawBoardComponent;
import remote.iClient;
import remote.iServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A WhiteBoard RMI class, that implements all methods needed for clients to interact with server.
 * This class also stores STATE of the server (users, drawings etc).
 * Author: Chenghao Li
 */
public class WhiteBoardRMI extends UnicastRemoteObject implements iServer {

    public static List<User> userList = Collections.synchronizedList(new ArrayList<User>());
    // to store a set of username that currently in use (including user in waiting room), remove name from it if user get approved or kicked
    public static Set<String> waitingList = Collections.synchronizedSet(new HashSet<>());

    private static User managerUser;

    // To store a list of history methods for new users to run.
    private ArrayList<MethodRunner> history_methods = new ArrayList<>();

    protected WhiteBoardRMI() throws RemoteException {
        super();
    }

    // return 0 if approved, return 1 if username already taken, return 2 if manager does not approve
    @Override
    public int CheckUserNameWith_Server(String name) throws RemoteException {
        // check if username already exists

        // check uf userName is unique, or is already selected if it's not unique, DO NOT add RMI since RMI uses its username.
        // 1. not in existing joined userList
        // 2. not in other joining people's waiting user list
        if ((userList.stream().filter(user -> Objects.equals(user.name, name)).collect(Collectors.toList()).size()==0)
                && (!waitingList.contains(name))){
            // add current username into HashSet to store it as the user is currently selecting this username
            waitingList.add(name);
            // ask permission from manager
            if (managerUser!=null) {
                // ask manager
                if (!managerUser.client.local_managerApproveUser(name)){
                    // if manager approve user join
                    System.out.println("Manager does not approve");
                    waitingList.remove(name);
                    this.handle_broadCastChat("[System", "User: "+ name+", is NOT approved to join!]");
                    return 2;
                }
            }
            System.out.println("User can join now");
            return 0;
        }
        System.out.println("UserName already exists");
        return 1;
    }




    @Override
    public UserSTATUS handle_addUser(String name, String lookUpURL) throws RemoteException {

        // try to establish RMI connection.
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
            usr = new User(name, newClient, UserSTATUS.MANAGER.MANAGER);
            managerUser = usr;
        }
        else {
            usr = new User(name, newClient);
            // if not manager, load and catchup with current progress
            for (MethodRunner mr : history_methods) {
                mr.run(usr);
            }
        }

        //add user to array list and update userlist panel for every client
        AddUser_Notify(usr);
        return usr.status;
    }




    @Override
    public void handle_broadCastChat(String username, String t) throws RemoteException {
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
    public void broadDrawShape(LocalDrawBoardComponent.shapeMode mode, int x1, int y1, int x2, int y2, float brushSize, boolean filling, int rgb) throws RemoteException {
        for (User u:userList){
            u.client.local_drawShape(mode, x1, y1, x2, y2, brushSize, filling, rgb);
        }
        // add all executed methods into an arraylist of history
        history_methods.add(new MethodRunner() {
            @Override
            public void run(User u) throws RemoteException {
                u.client.local_drawShape(mode, x1, y1, x2, y2, brushSize, filling, rgb);
            }
        });
    }

    @Override
    public void broadDrawPolygon(int[] lstX, int[] lstY, float brushSize, boolean filling, int rgb) throws RemoteException {
        for (User u:userList){
            u.client.local_drawPolygon(lstX, lstY, brushSize, filling, rgb);
        }
        // add all executed methods into an arraylist of history
        history_methods.add(new MethodRunner() {
            @Override
            public void run(User u) throws RemoteException {
                u.client.local_drawPolygon(lstX, lstY, brushSize, filling, rgb);
            }
        });
    }

    @Override
    public void broadDrawFree(int[] lstX, int[] lstY, float brushSize, boolean filling, int rgb) throws RemoteException {
        for (User u:userList){
            u.client.local_drawFree(lstX, lstY, brushSize, filling, rgb);
        }
        // add all executed methods into an arraylist of history
        history_methods.add(new MethodRunner() {
            @Override
            public void run(User u) throws RemoteException {
                u.client.local_drawFree(lstX, lstY, brushSize, filling, rgb);
            }
        });
    }

    @Override
    public void broadDrawText(String text, int x, int y, String name, int style, int size, int rgb) throws RemoteException{
        for (User u:userList){
            u.client.local_drawText(text, x, y, name, style, size, rgb);
        }
        // add all executed methods into an arraylist of history
        history_methods.add(new MethodRunner() {
            @Override
            public void run(User u) throws RemoteException {
                u.client.local_drawText(text, x, y, name, style ,size , rgb);
            }
        });
    }

    // check if user is approved in the chat
    private boolean checkApproved(User u){
        if (u.status!= UserSTATUS.WAITING){
            return true;
        }
        return false;
    }

    @Override
    public boolean kickUser(String subject, String target) throws RemoteException {
        User user = checkKickble(subject, target);
        if (user!=null){
            System.out.println("User found to be Kick");
            RemoveUser_Notify(user);
            return true;
        }
        return false;
    }


    // check if it's manager that require to kick someone is not a manager.
    public User checkKickble(String subject, String target){
        // query the arraylist of user to get that user information.
        User targ, subj;
        try {
            targ = userList.stream().filter(user -> Objects.equals(user.name, target)).collect(Collectors.toList()).get(0);
            subj = userList.stream().filter(user -> Objects.equals(user.name, subject)).collect(Collectors.toList()).get(0);

        }
        catch (IndexOutOfBoundsException e){
            // user is already removed, cannot kick
            return null;
        }

        // can only kick non Manager user, can only kick from the request of manager.
        if ((targ.status != UserSTATUS.MANAGER) && (subj.status == UserSTATUS.MANAGER)){
            return targ;
        }
        return null;
    }


    /**
     * To method two call whenever want to add or remove a user and notify all clients and everyplace
     * that may have this user information.
     * @param usr
     * @throws RemoteException
     */
    private static void AddUser_Notify(User usr) throws RemoteException {
        waitingList.remove(usr.name); //remove username as it move from waiting list to user list
        userList.add(usr);
        ArrayList<String> names = userList.stream()
                .map(user -> user.name)
                .collect(Collectors.toCollection(ArrayList::new));
        for (User u:userList){
            u.client.local_updateUserList(names);
        }
        System.out.println("Successfully add User to server, user Status: " + usr.status);
    }
    private static void RemoveUser_Notify(User usr) throws RemoteException {
        waitingList.remove(usr.name);
        userList.remove(usr);
        ArrayList<String> names = userList.stream()
                .map(user -> user.name)
                .collect(Collectors.toCollection(ArrayList::new));
        for (User u:userList){
            u.client.local_updateUserList(names);
        }
        // ask usr that client to quit.
        usr.client.local_beenKicked("You are Kicked by Manager.");
        System.out.println("Successfully remove User from server");
    }
}
