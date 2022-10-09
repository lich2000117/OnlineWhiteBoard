package client;

import remote.iClient;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * This class stores State, Methods of local client,
 * implements iClient to make it accessible to the server.
 *
 * Author:Chenghao Li
 */

public class ClientRMI extends UnicastRemoteObject implements iClient {
    ArrayList<String> messages = new ArrayList<String>();
    private MainFrame chatFrame;
    protected ClientRMI() throws RemoteException {
        super();
        // gui for text boxes
        chatFrame = new MainFrame("ChatBox");
        chatFrame.setVisible(true);
    }

    @Override
    public boolean addMessage(String message) throws RemoteException {
        messages.add(message);
        DisplayChat();
        return true;
    }

    @Override
    public void drawLine(String m) throws RemoteException {
        System.out.println(m);
    }

    public void DisplayChat() {
        // TODO this is the method that display all chats in GUI
        System.out.println(messages.stream().toArray());
    }
}
