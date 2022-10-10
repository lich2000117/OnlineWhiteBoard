package ComponentGUI;

import client.ClientRMI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class LocalDrawBoardComponent extends JPanel {
    private ClientRMI clientRMI;
    private ArrayList<Shape> shapeList = new ArrayList<>();
    private drawMode dMode = drawMode.DEFAULT;
    private shapeMode sMode = shapeMode.RECTANGLE;

    private int x1,y1,x2,y2 = 0;

    public LocalDrawBoardComponent(ClientRMI clientRMI){
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

    public void drawShape(LocalDrawBoardComponent.shapeMode mode, int x1, int y1, int x2, int y2){
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
                Point2D pt1, pt2, pt3;
                if(width < height){
                    pt1 = new Point2D.Float(x1, y1);
                    pt2 = new Point2D.Float(x2, y1);
                    pt3 = new Point2D.Float(((float)x1 + x2) / 2, y2);
                } else{
                    pt1 = new Point2D.Float(x1, y1);
                    pt2 = new Point2D.Float(x1, y2);
                    pt3 = new Point2D.Float(x2, ((float)y1 + y2) / 2);
                }
                shapeList.add(new TriangleShape(pt1, pt2, pt3));
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

    //TriangleShape: All credit to http://www.java2s.com/Tutorials/Java/Graphics_How_to/Shape/Draw_a_triangle.htm
    class TriangleShape extends Path2D.Double {
        public TriangleShape(Point2D... points) {
            moveTo(points[0].getX(), points[0].getY());
            lineTo(points[1].getX(), points[1].getY());
            lineTo(points[2].getX(), points[2].getY());
            closePath();
        }
    }

    public enum drawMode{
        DEFAULT,
        SHAPE,
        POLYGON,
        FREE
    }

    public enum shapeMode{
        RECTANGLE,
        ELLIPSE,
        TRIANGLE,
        LINE
    }
}
