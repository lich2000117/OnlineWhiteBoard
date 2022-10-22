package ComponentGUI;

import client.ClientRMI;
import client.LocalDrawBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * set a box layout panel to store a list of users as a list of buttons, click to kick
 *
 * Author: Chenghao Li
 */

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import server.User;
import server.UserSTATUS;

import java.awt.event.*;
import java.util.ArrayList;

public class UserPanel extends JPanel {
    private ClientRMI clientRMI;
    private JLabel NUserLabel;
    private JButton SwitchToChat;
    private JList userList;
    private JTextField inputMsg;
    private JButton kickUserButton;
    private LocalDrawBoard mainFrame;
    private JScrollPane scrollUser;
    private JPanel inputTextPanel;

    public static ArrayList<User> userArrayList;


    public UserPanel(ClientRMI clientRMI, LocalDrawBoard mainFrame){
        this.mainFrame = mainFrame;
        this.clientRMI = clientRMI;
        this.setPreferredSize(new Dimension(200, 500));

        this.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));

        NUserLabel = new JLabel("Current User: 0", SwingConstants.CENTER);
        this.add(NUserLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        SwitchToChat = new JButton("Show Chat");
        this.add(SwitchToChat, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        userList = new JList();



        scrollUser = new JScrollPane(userList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollUser.setAutoscrolls(true);
        this.add(scrollUser, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));


        inputTextPanel = new JPanel();
        inputTextPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        this.add(inputTextPanel, new GridConstraints(3, 0, 1, 1,GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        // add a button at the top to switch to user
        SwitchToChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.SwitchToChatPanel();
            }
        });
    }

    private void KickUserFunction() {
        // method used to kick user and display kick button
        kickUserButton = new JButton();
        kickUserButton.setText("Kick");
        inputTextPanel.add(kickUserButton, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.SIZEPOLICY_CAN_SHRINK, GridConstraints.SIZEPOLICY_CAN_SHRINK, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        // Kick user
        kickUserButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String userName = (String) userList.getSelectedValue();
                if (userName!=""){
                    clientRMI.request_KickUser(userName);
                }
            }
        });
    }

    public void updateUserList(ArrayList<String> names){
        System.out.println("````````````````User List```````````````````");
        System.out.println(names);
        System.out.println("Online Users: ");
        userList.setListData(names.toArray());
        // Check if it's manager now, update GUI.
        if (clientRMI.userStatus == UserSTATUS.MANAGER) {
            System.out.println("Update Manager GUI");
            // only manager can have kick function
            KickUserFunction();
        }
        NUserLabel.setText("Online Users: "+names.size());
        this.revalidate();
    }
}
