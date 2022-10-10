package ComponentGUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PickBrushSizeDialog extends JDialog{
    public final String F_brushSentence = "Brush size: ";
    private final int scaling = 100;
    private final int min = 1;
    private final int max = 10000;
    private JTextField brushSizeLabel;
    private JSlider slider;

    public PickBrushSizeDialog(Frame frame, float initSize){
        super(frame, "Pick brush size", true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayoutManager(3, 1, new Insets(20, 20, 20, 20), -1, -1));

        JPanel panelLabel = new JPanel();
        panelLabel.setLayout(new FlowLayout());
        panel.add(panelLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, null, null, 0, false));

        JLabel textLabel = new JLabel(F_brushSentence);
        panelLabel.add(textLabel);

        brushSizeLabel = new JTextField(4);
        brushSizeLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    float val = Float.parseFloat(brushSizeLabel.getText());
                    slider.setValue((int) (val * scaling));
                    update();
                } catch (NumberFormatException ex){
                    update();
                }
            }
        });
        panelLabel.add(brushSizeLabel);

        slider = new JSlider();
        slider.setMinimum(min);
        slider.setMaximum(max);
        slider.setValue(max); //Set to max for correct packing
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                update();
            }
        });
        panel.add(slider, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, null, null, 0, false));


        JButton quit = new JButton("Ok");
        quit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                dispose();
            }
        });
        panel.add(quit, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, null, null, 0, false));


        update();
        setResizable(false);
        getContentPane().add(panel);
        pack();

        slider.setValue((int) (initSize * scaling));
        Point loc = frame.getLocation();
        setLocation(loc.x + frame.getWidth()/2 - getWidth()/2, loc.y + frame.getHeight()/2 - getHeight()/2);
    }

    public float run() {
        this.setVisible(true);
        return getBrushSize();
    }

    private float getBrushSize(){
        return ((float)slider.getValue()) / scaling;
    }

    private void update(){
        brushSizeLabel.setText(String.valueOf(getBrushSize()));
    }
}
