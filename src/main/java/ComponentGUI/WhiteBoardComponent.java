package ComponentGUI;

import client.ClientRMI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.awt.image.AffineTransformOp;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class WhiteBoardComponent extends JPanel {
    private ClientRMI clientRMI;
    private ArrayList<Shape> shapeList = new ArrayList<>();
    private Shape temporaryShape = null;
    private drawMode dMode = drawMode.DEFAULT;
    private shapeMode sMode = shapeMode.RECTANGLE;

    private int x1,y1,x2,y2 = 0;

    public WhiteBoardComponent(ClientRMI clientRMI){
        this.clientRMI = clientRMI;
        this.setPreferredSize(new Dimension(500, 500));


        MouseAdapter mouseAdapter = new MouseAdapter() {
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
            public void mouseDragged(MouseEvent e) {
                super.mouseMoved(e);

                switch (dMode){
                    case SHAPE:
                        x2 = e.getX();
                        y2 = e.getY();

                        drawShape(sMode, x1, y1, x2, y2, false);
                        break;
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
        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    public void setDrawMode(drawMode mode){
        this.dMode = mode;
    }
    public void setShapeMode(shapeMode mode){this.sMode = mode;}

    public void drawShape(WhiteBoardComponent.shapeMode mode, int x1, int y1, int x2, int y2, boolean remains){
        int true_x1 = Math.min(x1,x2);
        int true_y1 = Math.min(y1,y2);
        int width=Math.abs(x1-x2);
        int height=Math.abs(y1-y2);

        Shape s = null;
        switch (mode){
            case RECTANGLE:
                s = new Rectangle2D.Float(true_x1, true_y1, width, height);
                break;
            case ELLIPSE:
                s = new Ellipse2D.Float(true_x1, true_y1, width, height);
                break;
            case LINE:
                s = new Line2D.Float(x1, y1, x2, y2);
                break;
        }

        if(s != null && remains){
            shapeList.add(s);
            temporaryShape = null;
        } else{
            if (temporaryShape == null){
                temporaryShape = s;
            } else{
                AffineTransform af = new AffineTransform();
                af.translate(2, 3);
                temporaryShape.getBounds().setBounds(true_x1, true_y1, width, height);
                temporaryShape = af.createTransformedShape(temporaryShape);
            }
        }

        repaint();
    }

    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2D.drawRect(0, 0, 490, 490);

        for(Shape s: shapeList){
            g2D.draw(s);
        }

        if(temporaryShape != null){
            g2D.draw(temporaryShape);
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
        LINE
    }
}
