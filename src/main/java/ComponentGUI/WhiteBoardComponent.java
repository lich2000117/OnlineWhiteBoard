package ComponentGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class WhiteBoardComponent extends JPanel {
    private ArrayList<Shape> shapeList = new ArrayList<>();
    private drawMode mode = drawMode.DEFAULT;

    private int x1,y1,x2,y2 = 0;

    public WhiteBoardComponent(){
        this.setPreferredSize(new Dimension(500, 500));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                switch (mode){
                    case ELLIPSE:
                    case RECTANGLE:
                        x1 = e.getX();
                        y1 = e.getY();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                switch (mode){
                    case ELLIPSE:
                    case RECTANGLE:
                        x2 = e.getX();
                        y2 = e.getY();
                        break;
                }

                switch (mode){
                    case ELLIPSE:
                        drawEllipse(x1, y1, x2, y2);
                        repaint();
                        break;
                    case RECTANGLE:
                        drawRectangle(x1, y1, x2, y2);
                        repaint();
                        break;
                }
            }
        });
    }

    public void setMode(drawMode mode){
        this.mode = mode;
    }
    public void drawRectangle(int x1, int y1, int x2, int y2){
        int true_x1 = Math.min(x1,x2);
        int true_y1 = Math.min(y1,y2);
        int width=Math.abs(x1-x2);
        int height=Math.abs(y1-y2);

        shapeList.add(new Rectangle2D.Float(true_x1, true_y1, width, height));
    }

    public void drawEllipse(int x1, int y1, int x2, int y2){
        int true_x1 = Math.min(x1,x2);
        int true_y1 = Math.min(y1,y2);
        int width=Math.abs(x1-x2);
        int height=Math.abs(y1-y2);

        shapeList.add(new Ellipse2D.Float(true_x1, true_y1, width, height));
    }

    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;

        g2D.drawRect(0, 0, 490, 490);

        for(Shape s: shapeList){
            g2D.draw(s);
        }
    }

    public enum drawMode{
        DEFAULT,
        RECTANGLE,
        ELLIPSE,
        FREE
    }
}
