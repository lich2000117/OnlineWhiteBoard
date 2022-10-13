package client;

import ComponentGUI.JoinDialogue;
import remote.iServer;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class Client,
 * 1. set up connection to RMI server
 * 2. set up stub and get stub from whiteboard on RMI server.
 */

public class Client extends UnicastRemoteObject implements Serializable{
    private final int USER_APPROVED = 0;
    private final int USER_NAMETAKEN = 1;
    private final int USER_NOT_APPROVED = 2;
    private String NamingServerIP;
    private String NamingServerPort;
    private String remoteMethodName; // we want to get all methods from whiteboardrmi server.
    private String thisRMIName = "client";
    public String userName; //static, one client can have only one username
    public String SELF_RMI_ADDRESS;  // RMI address of this client instance, so we can add this client to RMI server's object list.

    protected Client(String NamingServerIP, String NamingServerPort) throws RemoteException {
        this.NamingServerIP = NamingServerIP;
        this.NamingServerPort = NamingServerPort;
        this.remoteMethodName = "whiteboardrmi";
        this.thisRMIName = userName;
    }

    public static void main(String [] args) {
        String NamingServerIP = "localhost";
        String NamingServerPort = "2000";
        // 1. Connect to server RMI
        if (args.length==1){
            NamingServerIP = args[0];
        }
        else if(args.length==2){
            NamingServerIP = args[0];
            NamingServerPort = args[1];
        }

        ConnectAtStart(NamingServerIP, NamingServerPort);
    }

    /**
     * Method to connect user to RMI server and set up remote, set up whiteboard, client etc.
     * @param NamingServerIP
     * @param NamingServerPort
     */
    private static void ConnectAtStart(String NamingServerIP, String NamingServerPort) {
        Client client;
        // display connection dialog
        JOptionPane.showMessageDialog(null,"Welcome to Online SharedWhiteBoard!");
        while (true) {
            try {
                client = new Client(NamingServerIP, NamingServerPort);
                // 2. set up RMI registry and // 3. set up RMI UI
                client.SetUp();
                break;
            } catch (Exception e) {
                // if fail to connect, ask user to retry.
                int res = JOptionPane.showConfirmDialog(null, "WhiteBoard is not available right now, Please retry.", "Connection Error", JOptionPane.YES_NO_OPTION);
                if (res==JOptionPane.NO_OPTION){
                    System.exit(0);
                    return;
                }
            }
        }
    }

    /**
     * Set up RMI component for client and whiteboard remote server.
     * @throws MalformedURLException
     * @throws NotBoundException
     * @throws RemoteException
     */
    public void SetUp() throws MalformedURLException, NotBoundException, RemoteException {
        iServer whiteboardRMI = ConnectToWhiteBoardRMI();

        // 1.1 display user dialogue check if user accept this userName
        askForUserNamePopUp(whiteboardRMI);

        // 2. register self RMI apis to naming server and // 3. set up RMI UI
        ClientRMI clientRMI = (ClientRMI) new ClientRMI(whiteboardRMI, userName); // 3. set up RMI UI and run gui
        SELF_RMI_ADDRESS = "rmi://" + this.NamingServerIP + ":" + this.NamingServerPort + "/" + this.thisRMIName;
        Naming.rebind(SELF_RMI_ADDRESS, clientRMI);
        // 4. add current Client to Whiteboard RMI server so whiteboard has access to call method defined in RMI.
        if (clientRMI.addMeToWhiteBoardServer(userName, SELF_RMI_ADDRESS)){
            System.out.println("Client RMI registered to server!");
        } else {
            System.out.println("Client RMI FAILED to register to server! Restart and Try Again");
            clientRMI.local_beenKicked("Connection Failed, Try Again.");
        }
    }

    /**
     * Method uses a while loop until server accept the username
     * DONE: manager should have a popup message to allow that user name too.
     * @param whiteboardRMI
     * @throws RemoteException
     */
    private void askForUserNamePopUp(iServer whiteboardRMI) throws RemoteException {
        String msg = "";
        JOptionPane pane;
        JDialog dialog;
        while(true) {
            JoinDialogue popUp = new JoinDialogue(msg);
            this.userName = popUp.getSelectedName();
            if (this.userName==null){
                // if user closes window without submitting anything.
                System.exit(0);
            }
            if (!CheckUserNameLegal(this.userName)){
                msg = "UserName is Not Legal, Only Chars and Numbers are accepted.";
                continue; // continue to make user input name
            }

            // let user wait using this screen
            pane = new JOptionPane("Please Wait for Approval", JOptionPane.INFORMATION_MESSAGE,
                    JOptionPane.PLAIN_MESSAGE, // NO Icon
                    null,
                    new Object[] {},  // No options
                    null);
            dialog = pane.createDialog("Connecting...");
            dialog.setModalityType(Dialog.ModalityType.MODELESS);
            dialog.setVisible(true);

            // check on serverside if it is unique name
            int res = whiteboardRMI.CheckUserNameWith_Server(this.userName);
            switch (res) {
                case USER_APPROVED:
                    dialog.setVisible(false);
                    pane.setVisible(false);
                    dialog.dispose();
                    return;
                case USER_NAMETAKEN:
                    msg = "UserName is already Used!";
                    break;
                case USER_NOT_APPROVED:
                    msg = "Manager Disapproved you.";
            }
            dialog.setVisible(false);
            pane.setVisible(false);
            dialog.dispose();
        }
    }

    /**
     * Check if user name is legal.
     * @param name
     * @return
     */
    private boolean CheckUserNameLegal(String name){
        return name.matches("[a-zA-Z0-9]+");
    }

    /**
     * Connect to WhiteBoard server RMI
     * @return
     * @throws NotBoundException
     * @throws MalformedURLException
     * @throws RemoteException
     */
    private iServer ConnectToWhiteBoardRMI() throws NotBoundException, MalformedURLException, RemoteException {
        // 1. connect to WhiteBoard RMI from Naming Server
        iServer whiteboardRMI = (iServer) Naming.lookup("rmi://" + this.NamingServerIP + ":" + this.NamingServerPort + "/" + this.remoteMethodName);
        System.out.println("connected to whiteboard RMI!");
        return whiteboardRMI;
    }

}



