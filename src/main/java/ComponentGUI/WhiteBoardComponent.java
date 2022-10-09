package ComponentGUI;

import javax.swing.*;
import java.awt.*;

public class WhiteBoardComponent extends JPanel {
    public WhiteBoardComponent(){
        this.setPreferredSize(new Dimension(500, 500));
    }

    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;

        g2D.drawRect(0, 0, 490, 490);
        g2D.drawLine(100, 150, 50, 100);
    }
}
