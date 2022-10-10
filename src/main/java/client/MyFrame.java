package client;

import ComponentGUI.ChatPanel;
import ComponentGUI.ImageButton;
import ComponentGUI.WhiteBoardComponent;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyFrame extends JFrame {
    public final int F_buttonSize = 50;

    private JPanel mainPanel;


    private JPanel leftPanel;
    private ImageButton[] leftButtonList;


    private JPanel bottomPanel;
    private ImageButton[] bottomButtonList;

    private ChatPanel chatPanel;
    private ClientRMI clientRMI;
    private WhiteBoardComponent whiteBoard;


    //Icons variable

    public MyFrame(ClientRMI clientRMI) {
        mainPanel = new JPanel();
        this.clientRMI = clientRMI;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        setupUI();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    private void loadIcons(){

    }
    private void setupUI() {
        mainPanel.setLayout(new GridLayoutManager(2, 3, new Insets(10, 10, 10, 10), -1, -1));


        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayoutManager(8, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(leftPanel, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));


        leftButtonList = new ImageButton[6];
        leftButtonList[0] = new ImageButton("images/defaultSquare.png", "images/selectedSquare.png", new Dimension(F_buttonSize, F_buttonSize));
        leftButtonList[1] = new ImageButton("images/defaultCircle.png", "images/selectedCircle.png", new Dimension(F_buttonSize, F_buttonSize));
        leftButtonList[2] = new ImageButton("images/defaultTriangle.png", "images/selectedTriangle.png", new Dimension(F_buttonSize, F_buttonSize));
        leftButtonList[3] = new ImageButton("images/defaultLine.png", "images/selectedLine.png", new Dimension(F_buttonSize, F_buttonSize));
        leftButtonList[4] = new ImageButton("images/defaultPolygon.png", "images/selectedPolygon.png", new Dimension(F_buttonSize, F_buttonSize));
        leftButtonList[5] = new ImageButton("images/defaultFree.png", "images/selectedFree.png", new Dimension(F_buttonSize, F_buttonSize));


        setUpLeftButtonListener();
        for(int i = 0; i < leftButtonList.length; i++) {
            int finalI = i;
            leftButtonList[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);

                    for(int j = 0; j < leftButtonList.length; j++){
                        leftButtonList[j].unselect();
                    }

                    leftButtonList[finalI].select();
                }
            });

            leftPanel.add(leftButtonList[i], new GridConstraints(i+1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.SIZEPOLICY_FIXED, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK, null, null, null, 0, false));
        }


        final Spacer spacer1 = new Spacer();
        leftPanel.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        leftPanel.add(spacer2, new GridConstraints(leftButtonList.length+1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));


        whiteBoard = new WhiteBoardComponent(clientRMI);
        whiteBoard.setBorder(new EmptyBorder(50, 50, 50, 50));
        whiteBoard.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(whiteBoard, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));


        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(bottomPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));


        bottomButtonList = new ImageButton[3];
        bottomButtonList[0] = new ImageButton("images/brushSize.png", "images/brushSize.png", new Dimension(F_buttonSize, F_buttonSize));
        bottomButtonList[1] = new ImageButton("images/neutralImage.png", "images/neutralImage.png", new Dimension(F_buttonSize, F_buttonSize));
        bottomButtonList[2] = new ImageButton("images/figureEmpty.png", "images/figureFilled.png", new Dimension(F_buttonSize, F_buttonSize));

        setUpBottomButtonListener();
        for(int i = 0; i < bottomButtonList.length; i++) {
            bottomPanel.add(bottomButtonList[i], new GridConstraints(0, i+1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.SIZEPOLICY_FIXED, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK, null, null, null, 0, false));
        }

        final Spacer spacer3 = new Spacer();
        bottomPanel.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        bottomPanel.add(spacer4, new GridConstraints(0, bottomButtonList.length+1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));


        chatPanel = new ChatPanel(clientRMI);
        mainPanel.add(chatPanel, new GridConstraints(0, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK |  GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK |GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    private void setUpLeftButtonListener(){
        leftButtonList[0].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                whiteBoard.setDrawMode(WhiteBoardComponent.drawMode.SHAPE);
                whiteBoard.setShapeMode(WhiteBoardComponent.shapeMode.RECTANGLE);
            }
        });

        leftButtonList[1].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                whiteBoard.setDrawMode(WhiteBoardComponent.drawMode.SHAPE);
                whiteBoard.setShapeMode(WhiteBoardComponent.shapeMode.ELLIPSE);
            }
        });

        leftButtonList[2].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                whiteBoard.setDrawMode(WhiteBoardComponent.drawMode.SHAPE);
                whiteBoard.setShapeMode(WhiteBoardComponent.shapeMode.TRIANGLE);
            }
        });

        leftButtonList[3].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                whiteBoard.setDrawMode(WhiteBoardComponent.drawMode.SHAPE);
                whiteBoard.setShapeMode(WhiteBoardComponent.shapeMode.LINE);
            }
        });

        leftButtonList[4].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                whiteBoard.setDrawMode(WhiteBoardComponent.drawMode.POLYGON);
            }
        });

        leftButtonList[5].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                whiteBoard.setDrawMode(WhiteBoardComponent.drawMode.FREE);
            }
        });
    }

    private void setUpBottomButtonListener() {
        bottomButtonList[0].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                //TODO
            }
        });

        bottomButtonList[1].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                //TODO
            }
        });

        bottomButtonList[2].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                //TODO
            }
        });
    }

    public ChatPanel getChatPanel(){
        return chatPanel;
    }

    public WhiteBoardComponent getWhiteBoard(){
        return whiteBoard;
    }

    public static void main(String[] args) {
        JFrame frame = new MyFrame(null);
        frame.setVisible(true);
    }
}







