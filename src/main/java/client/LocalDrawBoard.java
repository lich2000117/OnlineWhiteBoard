package client;

import ComponentGUI.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Paths;

public class LocalDrawBoard extends JFrame {
    public final int F_buttonSize = 50;
    public final String userDirectory = Paths.get("")
            .toAbsolutePath()
            .toString() + "/src/";

    private JPanel mainPanel;


    private JPanel leftPanel;
    private ImageButton[] leftButtonList;


    private JPanel bottomPanel;
    private ImageButton[] bottomButtonList;

    private ChatPanel chatPanel;
    private ClientRMI clientRMI;
    private LocalDrawBoardComponent whiteBoard;
    private UserPanel userPanel;


    //Icons variable

    public LocalDrawBoard(ClientRMI clientRMI) {
        mainPanel = new JPanel();
        this.clientRMI = clientRMI;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        setupUI();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void setupUI() {
        mainPanel.setLayout(new GridLayoutManager(2, 3, new Insets(10, 10, 10, 10), -1, -1));


        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayoutManager(8, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(leftPanel, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));


        leftButtonList = new ImageButton[6];
        leftButtonList[0] = new ImageButton(userDirectory + "images/defaultSquare.png", userDirectory + "images/selectedSquare.png", new Dimension(F_buttonSize, F_buttonSize));
        leftButtonList[1] = new ImageButton(userDirectory + "images/defaultCircle.png", userDirectory + "images/selectedCircle.png", new Dimension(F_buttonSize, F_buttonSize));
        leftButtonList[2] = new ImageButton(userDirectory + "images/defaultTriangle.png", userDirectory + "images/selectedTriangle.png", new Dimension(F_buttonSize, F_buttonSize));
        leftButtonList[3] = new ImageButton(userDirectory + "images/defaultLine.png", userDirectory + "images/selectedLine.png", new Dimension(F_buttonSize, F_buttonSize));
        leftButtonList[4] = new ImageButton(userDirectory +"images/defaultPolygon.png", userDirectory + "images/selectedPolygon.png", new Dimension(F_buttonSize, F_buttonSize));
        leftButtonList[5] = new ImageButton(userDirectory + "images/defaultFree.png", userDirectory + "images/selectedFree.png", new Dimension(F_buttonSize, F_buttonSize));


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


        whiteBoard = new LocalDrawBoardComponent(clientRMI);
        whiteBoard.setBorder(new EmptyBorder(50, 50, 50, 50));
        whiteBoard.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(whiteBoard, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));


        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(bottomPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));


        bottomButtonList = new ImageButton[3];
        bottomButtonList[0] = new ImageButton(userDirectory + "images/brushSize.png", userDirectory + "images/brushSize.png", new Dimension(F_buttonSize, F_buttonSize));
        bottomButtonList[1] = new ImageButton(userDirectory + "images/figureEmpty.png", userDirectory + "images/figureFilled.png", new Dimension(F_buttonSize, F_buttonSize));
        bottomButtonList[2] = new ImageColoredButton(userDirectory + "images/neutralImage.png", userDirectory + "images/neutralImage.png", new Dimension(F_buttonSize, F_buttonSize));
        ((ImageColoredButton)bottomButtonList[2]).setColor(whiteBoard.getCurrentColor());


        setUpBottomButtonListener();
        for(int i = 0; i < bottomButtonList.length; i++) {
            bottomPanel.add(bottomButtonList[i], new GridConstraints(0, i+1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.SIZEPOLICY_FIXED, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK, null, null, null, 0, false));
        }

        final Spacer spacer3 = new Spacer();
        bottomPanel.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        bottomPanel.add(spacer4, new GridConstraints(0, bottomButtonList.length+1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));

        // display chat/user panel
        chatPanel = new ChatPanel(clientRMI, this);
        userPanel = new UserPanel(clientRMI, this);

        // at begining, display chatPanel only.
        mainPanel.add(chatPanel, new GridConstraints(0, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK |  GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK |GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));

    }

    private void setUpLeftButtonListener(){
        leftButtonList[0].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                whiteBoard.setDrawMode(LocalDrawBoardComponent.drawMode.SHAPE);
                whiteBoard.setShapeMode(LocalDrawBoardComponent.shapeMode.RECTANGLE);
            }
        });

        leftButtonList[1].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                whiteBoard.setDrawMode(LocalDrawBoardComponent.drawMode.SHAPE);
                whiteBoard.setShapeMode(LocalDrawBoardComponent.shapeMode.ELLIPSE);
            }
        });

        leftButtonList[2].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                whiteBoard.setDrawMode(LocalDrawBoardComponent.drawMode.SHAPE);
                whiteBoard.setShapeMode(LocalDrawBoardComponent.shapeMode.TRIANGLE);
            }
        });

        leftButtonList[3].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                whiteBoard.setDrawMode(LocalDrawBoardComponent.drawMode.SHAPE);
                whiteBoard.setShapeMode(LocalDrawBoardComponent.shapeMode.LINE);
            }
        });

        leftButtonList[4].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                whiteBoard.setDrawMode(LocalDrawBoardComponent.drawMode.POLYGON);
            }
        });

        leftButtonList[5].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                whiteBoard.setDrawMode(LocalDrawBoardComponent.drawMode.FREE);
            }
        });
    }

    private void setUpBottomButtonListener() {
        Frame frame = this;
        bottomButtonList[0].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                PickBrushSizeDialog dial = new PickBrushSizeDialog(frame, whiteBoard.getCurrentBrushSize());
                whiteBoard.setCurrentBrushSize(dial.run());
            }
        });

        bottomButtonList[1].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if(bottomButtonList[1].isSelected()){
                    whiteBoard.setCurrentFilling(false);
                    bottomButtonList[1].unselect();
                } else{
                    whiteBoard.setCurrentFilling(true);
                    bottomButtonList[1].select();
                }

            }
        });

        bottomButtonList[2].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                JColorChooser colorChooser = new JColorChooser(whiteBoard.getCurrentColor());
                ActionListener onOk = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Color c = colorChooser.getColor();
                        ((ImageColoredButton) bottomButtonList[2]).setColor(c);
                        whiteBoard.setCurrentColor(c);
                    }
                };
                JColorChooser.createDialog(frame, "Pick brush color", true, colorChooser, onOk, (ActionEvent ev)-> {}).show();
            }
        });
    }

    public ChatPanel getChatPanel(){
        return chatPanel;
    }

    public UserPanel getUserPanel(){
        return userPanel;
    }

    public LocalDrawBoardComponent getWhiteBoard(){
        return whiteBoard;
    }

    public void SwitchToChatPanel(){
        mainPanel.remove(userPanel);
        mainPanel.add(chatPanel, new GridConstraints(0, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK |  GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK |GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    public void SwitchToUserPanel(){
        mainPanel.remove(chatPanel);
        mainPanel.add(userPanel, new GridConstraints(0, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK |  GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK |GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void DisplayMessage(String msg){
        JOptionPane.showMessageDialog(null, msg);
    }

    public void DisableUI_WithMessage(String msg){
        DisplayMessage(msg);
        this.setEnabled(false);
    }

    public static void main(String[] args) {
        JFrame frame = new LocalDrawBoard(null);
        frame.setVisible(true);
    }
}







