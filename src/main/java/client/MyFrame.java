package client;

import ComponentGUI.ChatPanel;
import ComponentGUI.WhiteBoardComponent;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;

public class MyFrame extends JFrame {

    private JPanel mainPanel;

    private JButton button1;
    private JButton button2;
    private JButton button3;


    private JPanel chatPanel;
    private JLabel NUserLabel;
    private JButton extendUserButton;
    private JList chatList;
    private JTextField inputMsg;
    private JButton sendMsgButton;

    private ClientRMI clientRMI;

    private WhiteBoardComponent boardComponent;

    public MyFrame(ClientRMI clientRMI){
        mainPanel  = new JPanel();
        this.clientRMI = clientRMI;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        setupUI();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                // ask RMI to send a request to server to draw.
                try {
                    clientRMI.request_drawRectangle();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        button1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public void drawRectangle_Board(){
        boardComponent.getGraphics().drawRect(50, 50, 100, 100);
    }


    private void setupUI(){
        mainPanel.setLayout(new GridLayoutManager(6, 3, new Insets(10, 10, 10, 10), -1, -1));
        button1 = new JButton();
        button1.setText("Button");
        mainPanel.add(button1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        button2 = new JButton();
        button2.setText("Button");
        mainPanel.add(button2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        button3 = new JButton();
        button3.setText("Button");
        mainPanel.add(button3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));

        boardComponent = new WhiteBoardComponent();
        boardComponent.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        mainPanel.add(boardComponent, new GridConstraints(0, 1, 6, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));


        chatPanel = new ChatPanel();
        mainPanel.add(chatPanel, new GridConstraints(0, 2, 6, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK |  GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK |GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

//    public static void main(String[] args) {
//        JFrame frame = new MyFrame();
//        frame.setVisible(true);
//    }
}







