package client;

import remote.iClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Chat related method at local client,
 * implements iClient to make it accessible to the server.
 *
 * Author:Chenghao Li
 */

public class ClientChatBox extends UnicastRemoteObject implements iClient {
    ArrayList<String> messages = new ArrayList<String>();

    protected ClientChatBox() throws RemoteException {
        super();
    }

    @Override
    public boolean addMessage(String message) throws RemoteException {
        messages.add(message);
        DisplayChat();
        return true;
    }

    public void DisplayChat() {
        // TODO this is the method that display all chats in GUI
        System.out.println(messages.stream().toArray());
    }
}
