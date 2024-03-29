package client;

import ComponentGUI.JoinDialogue;
import remote.iServer;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.net.BindException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.ServerNotActiveException;
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
    private String whiteboard_NamingServerIP;
    private String local_NamingIP;
    private int local_NamingPort;
    private int NamingServerPort;
    private String remoteMethodName; // we want to get all methods from whiteboardrmi server.
    private String thisRMIName = "client";
    public String userName; //static, one client can have only one username
    public String SELF_RMI_ADDRESS;  // RMI address of this client instance, so we can add this client to RMI server's object list.

    protected Client(String whiteboard_NamingServerIP, int NamingServerPort, String local_NamingIP, int local_NamingPort) throws RemoteException {
        this.whiteboard_NamingServerIP = whiteboard_NamingServerIP;
        this.NamingServerPort = NamingServerPort;
        this.local_NamingIP = local_NamingIP;
        this.remoteMethodName = "whiteboardrmi";
        this.local_NamingPort = local_NamingPort;
    }

    public static void main(String [] args) {
        String whiteboard_NamingServerIP;
        int NamingServerPort;
        String local_NamingIP;
        int local_NamingPort;
        // 1. Connect to server RMI
        for (String s: args){
            System.out.println(s);
        }

        if (args.length!=4){
            System.out.println("Please Provide full length arguments. WhiteBoard IP +(port) + Local IP +(port)");
            return;
        }
        else {
            whiteboard_NamingServerIP = args[0];
            NamingServerPort = Integer.parseInt(args[1]);
            local_NamingIP = args[2];
            local_NamingPort = Integer.parseInt(args[3]);
        }
        System.out.println("Connecting to Whiteboard: " + whiteboard_NamingServerIP + ":" + NamingServerPort);
        System.out.println("From Local Client: " + local_NamingIP + ":" + local_NamingPort);
        ConnectAtStart(whiteboard_NamingServerIP, NamingServerPort, local_NamingIP, local_NamingPort);
    }

    /**
     * Method to connect user to RMI server and set up remote, set up whiteboard, client etc.
     * @param NamingServerIP
     */
    private static void ConnectAtStart(String NamingServerIP, int NamingServerPort, String local_NamingIP, int local_NamingPort) {
        Client client;
        // display connection dialog
        JOptionPane.showMessageDialog(null,"Welcome to Online SharedWhiteBoard!");
        while (true) {
            try {
                client = new Client(NamingServerIP, NamingServerPort, local_NamingIP, local_NamingPort);
                // 2. set up RMI registry and // 3. set up RMI UI
                client.SetUp();
                break;
            }
            catch (ExportException e) {
                // if fail to connect, ask user to retry.
                int res = JOptionPane.showConfirmDialog(null, "Local Address been used, try different ports.", "Duplicate Port", JOptionPane.OK_OPTION);
                System.exit(0);
                return;
            }
            catch(Exception e) {
                //e.printStackTrace();
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
        System.out.println("----------------------------------------------------------");
        // 1.1 display user dialogue check if user accept this userName
        askForUserNamePopUp(whiteboardRMI);

        // 2. register self RMI apis to naming server
        System.setProperty("java.rmi.server.hostname",this.local_NamingIP);  //set public IP for others to connect.
        ClientRMI clientRMI = (ClientRMI) new ClientRMI(whiteboardRMI, userName);
        // 3. set up RMI UI and run gui
        // bind registry
        Registry registry = LocateRegistry.createRegistry(this.local_NamingPort);
        registry.rebind(this.thisRMIName, clientRMI);
        System.out.println("This RMI name: " + thisRMIName);
        //System.out.println(registry);
        // 4. add current Client to Whiteboard RMI server so whiteboard has access to call method defined in RMI.
        if (clientRMI.addMeToWhiteBoardServer(userName, this.local_NamingIP, this.local_NamingPort)){
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
            this.thisRMIName = userName;
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
        System.out.println("Connecting to" + whiteboard_NamingServerIP + " : " + NamingServerPort );
        iServer whiteboardRMI = (iServer) Naming.lookup("rmi://" + this.whiteboard_NamingServerIP + ":" + this.NamingServerPort + "/" + this.remoteMethodName);
        System.out.println("connected to whiteboard RMI!");
        System.out.println(whiteboardRMI);
        return whiteboardRMI;
    }

}



