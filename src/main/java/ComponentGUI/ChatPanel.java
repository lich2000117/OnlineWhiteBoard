package ComponentGUI;

import client.ClientRMI;
import client.LocalDrawBoard;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;

public class ChatPanel extends JPanel {
    private ClientRMI clientRMI;
    private JLabel TopRightLabel;
    private JButton SwitchToUser;
    private JTextArea chatbox;
    private JTextField inputMsg;
    private JButton sendMsgButton;
    private LocalDrawBoard mainFrame;
    private JPanel inputTextPanel;
    private JScrollPane scrollChat;


    public ChatPanel(ClientRMI clientRMI, LocalDrawBoard mainFrame){
        this.mainFrame = mainFrame;
        this.clientRMI = clientRMI;
        this.setPreferredSize(new Dimension(200, 500));

        this.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));

        TopRightLabel = new JLabel(clientRMI.getUsername() + ", Welcome!", SwingConstants.CENTER);
        this.add(TopRightLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        SwitchToUser = new JButton("Show Users");
        this.add(SwitchToUser, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));


        chatbox = new JTextArea();
        chatbox.setWrapStyleWord(true);
        chatbox.setLineWrap(true);

        chatbox.setEditable(false);

        scrollChat = new JScrollPane(chatbox, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollChat.setAutoscrolls(true);
        this.add(scrollChat, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));


        inputTextPanel = new JPanel();
        inputTextPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        this.add(inputTextPanel, new GridConstraints(3, 0, 1, 1,GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        // add a button at the top to switch to user
        SwitchToUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.SwitchToUserPanel();
            }
        });

        inputMsg = new JTextField();
        inputMsg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        inputTextPanel.add(inputMsg, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));


        sendMsgButton = new JButton();
        sendMsgButton.setText("SEND");
        inputTextPanel.add(sendMsgButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.SIZEPOLICY_CAN_SHRINK, GridConstraints.SIZEPOLICY_CAN_SHRINK, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));


        sendMsgButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                sendMessage();
            }
        });
    }

    public void sendMessage(){

        try {
            clientRMI.request_sendChatMessage(clientRMI.getUsername(), inputMsg.getText());
        } catch (RemoteException e) {
            clientRMI.kickClientUI("Lost Connection to Server");
        }
        inputMsg.setText("");
    }

    public void addMessage(String clientName, String msg){
        chatbox.setText(chatbox.getText() + "\n" + clientName + ": "+ msg+"\n");
    }
}
