package ComponentGUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PickFontDialog extends JDialog{
    public static final int F_minFontSize = 1;
    public static final int F_maxFontSize = 99;

    public static final String stylePlain = "Plain";
    public static final String styleBold = "Bold";
    public static final String styleItalic = "Italic";
    JList<String> nameList;
    JList<String> styleList;
    SpinnerNumberModel sizeSpinner;


    public PickFontDialog(Frame frame, String fontName, int fontStyle, int fontSize){
        super(frame, "Pick font", true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayoutManager(3, 3, new Insets(20, 20, 20, 20), -1, -1));

        JLabel l1 = new JLabel("Name:");
        panel.add(l1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, null, null, 0, false));

        JLabel l2 = new JLabel("Style:");
        panel.add(l2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, null, null, 0, false));

        JLabel l3 = new JLabel("Size:");
        panel.add(l3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, null, null, 0, false));


        String[] strNameList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        nameList = new JList<>(strNameList);
        for (int i = 0; i < strNameList.length; i++){
            if(strNameList[i].equals(fontName)){
                nameList.setSelectedIndex(i);
                break;
            }
        }

        JScrollPane scrollName = new JScrollPane(nameList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollName.setAutoscrolls(true);
        panel.add(scrollName, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, null, null, 0, false));



        String[] strStyleList = {stylePlain, styleBold, styleItalic};
        styleList = new JList<>(strStyleList);
        for (int i = 0; i < strStyleList.length; i++){
            if(strStyleList[i].equals(styleIntToString(fontStyle))){
                styleList.setSelectedIndex(i);
                break;
            }
        }
        panel.add(styleList, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, null, null, 0, false));


        sizeSpinner = new SpinnerNumberModel();
        sizeSpinner.setValue(fontSize);
        sizeSpinner.setMinimum(F_minFontSize);
        sizeSpinner.setMaximum(F_maxFontSize);
        JSpinner spin = new JSpinner(sizeSpinner);
        panel.add(spin, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, null, null, 0, false));

        JButton quit = new JButton("Ok");
        quit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                dispose();
            }
        });
        panel.add(quit, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, null, null, 0, false));


        setResizable(false);
        getContentPane().add(panel);
        pack();

        Point loc = frame.getLocation();
        setLocation(loc.x + frame.getWidth()/2 - getWidth()/2, loc.y + frame.getHeight()/2 - getHeight()/2);
    }

    public String[] run() {
        this.setVisible(true);


        return new String[]{nameList.getSelectedValue(), styleList.getSelectedValue(), sizeSpinner.getValue().toString()};
    }


    public static int styleStringToInt(String style){
        if(style.equals(stylePlain)){
            return Font.PLAIN;
        } else if(style.equals(styleBold)){
            return Font.BOLD;
        } else if(style.equals(styleItalic)){
            return Font.ITALIC;
        }
        return -1;
    }

    public static String styleIntToString(int style){
        switch (style){
            case Font.PLAIN:
                return stylePlain;
            case Font.BOLD:
                return styleBold;
            case Font.ITALIC:
                return styleItalic;
        }
        return null;
    }
}
