package ComponentGUI;

import client.ClientRMI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class WhiteBoardComponent extends JPanel {
    private ClientRMI clientRMI;
    private ArrayList<Shape> shapeList = new ArrayList<>();
    private drawMode dMode = drawMode.DEFAULT;
    private shapeMode sMode = shapeMode.RECTANGLE;

    private int x1,y1,x2,y2 = 0;

    public WhiteBoardComponent(ClientRMI clientRMI){
        this.clientRMI = clientRMI;
        this.setPreferredSize(new Dimension(500, 500));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                switch (dMode){
                    case SHAPE:
                        x1 = e.getX();
                        y1 = e.getY();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                switch (dMode){
                    case SHAPE:
                        x2 = e.getX();
                        y2 = e.getY();

                        try {
                            clientRMI.request_drawShape(sMode, x1, y1, x2, y2);
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                        repaint();
                        break;
                }
            }
        });
    }

    public void setDrawMode(drawMode mode){
        this.dMode = mode;
    }
    public void setShapeMode(shapeMode mode){this.sMode = mode;}

    public void drawShape(WhiteBoardComponent.shapeMode mode, int x1, int y1, int x2, int y2){
        int true_x1 = Math.min(x1,x2);
        int true_y1 = Math.min(y1,y2);
        int width=Math.abs(x1-x2);
        int height=Math.abs(y1-y2);

        switch (mode){
            case RECTANGLE:
                shapeList.add(new Rectangle2D.Float(true_x1, true_y1, width, height));
                break;
            case ELLIPSE:
                shapeList.add(new Ellipse2D.Float(true_x1, true_y1, width, height));
                break;
            case TRIANGLE:
                //shapeList.add(new .Float(true_x1, true_y1, width, height));
                break;
            case LINE:
                shapeList.add(new Line2D.Float(x1, y1, x2, y2));
                break;
        }

        repaint();
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
        SHAPE,
        FREE
    }

    public enum shapeMode{
        RECTANGLE,
        ELLIPSE,
        TRIANGLE,
        LINE
    }
}
