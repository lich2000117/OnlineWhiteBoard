package client;

import ComponentGUI.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import server.UserSTATUS;
import server.MethodRunner;
import server.User;
import server.UserSTATUS;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class LocalDrawBoard extends JFrame {
    public final int F_buttonSize = 50;
    public final String userDirectory = Paths.get("")
            .toAbsolutePath()
            .toString() + "/src/";

    private JFrame frame = this;
    private JFileChooser fileChooser = new JFileChooser();
    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenuItem menuNew;
    private JMenuItem menuOpen;
    private JMenuItem menuSave;
    private JMenuItem menuSaveAs;
    private JMenuItem menuClose;
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
        this.setResizable(false);

        ActionOnCloseWindow(clientRMI);
    }

    /**
     * action when user closes whole application window
     * @param clientRMI
     */
    private void ActionOnCloseWindow(ClientRMI clientRMI) {
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                int res;
                if (clientRMI.userStatus== UserSTATUS.MANAGER){
                    res = JOptionPane.showConfirmDialog(frame, "This action will terminate WhiteBoard, Are you Sure?", "Exit Message", JOptionPane.YES_NO_OPTION);
                }
                else {
                    res = JOptionPane.showConfirmDialog(frame, "Are you sure?", "Exit Message", JOptionPane.YES_NO_OPTION);
                }
                if (res==JOptionPane.YES_OPTION) {
                    clientRMI.request_userLeave();
                }
                else{
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }

    /**
     * Set up board UI, using grid
     */
    private void setupUI() {
        mainPanel.setLayout(new GridLayoutManager(2, 3, new Insets(10, 10, 10, 10), -1, -1));

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
        fileChooser.setFileFilter(filter);

        //SETUP MENU
        menuBar = new JMenuBar();
        menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_ESCAPE);
        menuBar.add(menuFile);


        //MENU RESERVER TO MANAGER
        menuNew = new JMenuItem(new AbstractAction("New") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    clientRMI.request_cleanBoard();
                    whiteBoard.fileName = null;
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }});
        menuNew.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menuFile.add(menuNew);

        menuOpen = new JMenuItem(new AbstractAction("Open") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();

                    whiteBoard.fileName = file.getAbsolutePath();
                }

                whiteBoard.openFile();
            }
        });
        menuOpen.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menuFile.add(menuOpen);

        menuSave = new JMenuItem(new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(whiteBoard.fileName == null){
                    saveAs();
                } else{
                    try {
                        clientRMI.ask_save_file(whiteBoard.fileName);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        menuSave.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuFile.add(menuSave);

        menuSaveAs = new JMenuItem(new AbstractAction("Save as") {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAs();
            }
        });
        menuSaveAs.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuFile.add(menuSaveAs);


        menuSaveAs = new JMenuItem(new AbstractAction("Export as png") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showSaveDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String fName = fileChooser.getSelectedFile().getAbsolutePath();
                    whiteBoard.savePaintAsPng(fName);
                }
            }
        });

        menuSaveAs.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_E, ActionEvent.ALT_MASK));
        menuFile.add(menuSaveAs);

        menuClose = new JMenuItem(new AbstractAction("Close") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        menuFile.add(menuClose);

        setJMenuBar(menuBar);


        Setup_LeftPanel();

        Setup_WhiteBoardDrawPart();

        Setup_BottomPanel();

        Setup_ChatUserPanels();

    }

    private void Setup_ChatUserPanels() {
        // display chat/user panel
        chatPanel = new ChatPanel(clientRMI, this);
        userPanel = new UserPanel(clientRMI, this);
        // at beginning, display chatPanel only.
        mainPanel.add(chatPanel, new GridConstraints(0, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK |  GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK |GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    private void Setup_BottomPanel() {
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(bottomPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));

        bottomButtonList = new ImageButton[4];
        bottomButtonList[0] = new ImageButton(userDirectory + "images/brushSize.png", userDirectory + "images/brushSize.png", new Dimension(F_buttonSize, F_buttonSize));
        bottomButtonList[1] = new ImageButton(userDirectory + "images/figureEmpty.png", userDirectory + "images/figureFilled.png", new Dimension(F_buttonSize, F_buttonSize));
        bottomButtonList[2] = new ImageButton(userDirectory + "images/defaultFont.png", userDirectory + "images/defaultFont.png", new Dimension(F_buttonSize, F_buttonSize));
        bottomButtonList[3] = new ImageColoredButton(userDirectory + "images/neutralImage.png", userDirectory + "images/neutralImage.png", new Dimension(F_buttonSize, F_buttonSize));
        ((ImageColoredButton)bottomButtonList[3]).setColor(whiteBoard.getCurrentColor());

        setUpBottomButtonListener();
        for(int i = 0; i < bottomButtonList.length; i++) {
            bottomPanel.add(bottomButtonList[i], new GridConstraints(0, i+1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.SIZEPOLICY_FIXED, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK, null, null, null, 0, false));
        }

        final Spacer spacer3 = new Spacer();
        bottomPanel.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        bottomPanel.add(spacer4, new GridConstraints(0, bottomButtonList.length+1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    private void Setup_WhiteBoardDrawPart() {
        whiteBoard = new LocalDrawBoardComponent(clientRMI);
        whiteBoard.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(whiteBoard, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED | GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED | GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.black, 3));
    }

    private void Setup_LeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayoutManager(9, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(leftPanel, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));

        leftButtonList = new ImageButton[7];
        leftButtonList[0] = new ImageButton(userDirectory + "images/defaultSquare.png", userDirectory + "images/selectedSquare.png", new Dimension(F_buttonSize, F_buttonSize));
        leftButtonList[1] = new ImageButton(userDirectory + "images/defaultCircle.png", userDirectory + "images/selectedCircle.png", new Dimension(F_buttonSize, F_buttonSize));
        leftButtonList[2] = new ImageButton(userDirectory + "images/defaultTriangle.png", userDirectory + "images/selectedTriangle.png", new Dimension(F_buttonSize, F_buttonSize));
        leftButtonList[3] = new ImageButton(userDirectory + "images/defaultLine.png", userDirectory + "images/selectedLine.png", new Dimension(F_buttonSize, F_buttonSize));
        leftButtonList[4] = new ImageButton(userDirectory +"images/defaultPolygon.png", userDirectory + "images/selectedPolygon.png", new Dimension(F_buttonSize, F_buttonSize));
        leftButtonList[5] = new ImageButton(userDirectory + "images/defaultFree.png", userDirectory + "images/selectedFree.png", new Dimension(F_buttonSize, F_buttonSize));
        leftButtonList[6] = new ImageButton(userDirectory + "images/defaultText.png", userDirectory + "images/selectedText.png", new Dimension(F_buttonSize, F_buttonSize));

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
    }

    private void setUpMenuListener(){
        menuNew = new JMenuItem();
        menuNew.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                System.out.println("");

            }
        });

        menuOpen = new JMenuItem();
        menuOpen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);


            }
        });

        menuSave = new JMenuItem();
        menuSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);


            }
        });

        menuSaveAs = new JMenuItem();
        menuSaveAs.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);


            }
        });

        menuClose = new JMenuItem();
        menuClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);


            }
        });
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

        leftButtonList[6].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                whiteBoard.setDrawMode(LocalDrawBoardComponent.drawMode.TEXTMODE);
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


                PickFontDialog dial = new PickFontDialog(frame, whiteBoard.getCurrentFontName(), whiteBoard.getCurrentFontStyle(), whiteBoard.getCurrentFontSize());
                String[] returnVal = dial.run();

                whiteBoard.setCurrentFontName(returnVal[0]);
                whiteBoard.setCurrentFontStyle(PickFontDialog.styleStringToInt(returnVal[1]));
                whiteBoard.setCurrentFontSize(Integer.parseInt(returnVal[2]));
            }
        });

        bottomButtonList[3].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                JColorChooser colorChooser = new JColorChooser(whiteBoard.getCurrentColor());
                ActionListener onOk = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Color c = colorChooser.getColor();
                        ((ImageColoredButton) bottomButtonList[3]).setColor(c);
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

    /**
     * Display dialog message and check if need to close window after dialog done
     * @param msg
     * @param closeWindow
     */
    public void DisplayMessage(String msg, Boolean closeWindow){
        JOptionPane.showOptionDialog(this, msg, "System Message", JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, null, null);
        if (closeWindow){
            System.exit(0);
        }
    }

    public void DisableUI_WithMessage(String msg, Boolean closeWindow){
        DisplayMessage(msg, closeWindow);
        this.setEnabled(false);
    }

    /**
     * Confirm the joining of new user, if click yes, return 1, otherwise 0
     * @param msg
     * @return
     */
    public int ConfirmWindow(String msg){
        return JOptionPane.showConfirmDialog(this,msg, "Alert", JOptionPane.YES_NO_OPTION);
    }

    private void saveAs(){
        int returnVal = fileChooser.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            whiteBoard.fileName = fileChooser.getSelectedFile().getAbsolutePath();

            try{
                clientRMI.ask_save_file(whiteBoard.fileName);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void savePaint() {
        System.out.println("Save paint");
        ArrayList<MethodRunner> methodsLst = new ArrayList<>();
        methodsLst.add(new MethodRunner() {
            @Override
            public void run(User u) throws RemoteException {
                System.out.println("Hello world !");
            }
        });

        try {
            FileOutputStream myfileoutput = new FileOutputStream(new File("mytestfile"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(myfileoutput);

            for (MethodRunner mr: methodsLst){
                objectOutputStream.writeObject(mr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//
//    public static void main(String[] args) {
//        try{
//            JFrame frame = new LocalDrawBoard(new ClientRMI(null, "Me"));
//            frame.setVisible(true);
//        } catch (RemoteException e) {
//            throw new RuntimeException(e);
//        }
//    }
}







