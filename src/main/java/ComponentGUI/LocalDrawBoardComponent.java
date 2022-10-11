package ComponentGUI;

import client.ClientRMI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class LocalDrawBoardComponent extends JPanel {
    private ClientRMI clientRMI;

    private ArrayList<DrawingType> drawingTypeList = new ArrayList();
    private ArrayList<Shape> shapeList = new ArrayList<>();
    private ArrayList<Float> shapeListBrushSize = new ArrayList<>();
    private ArrayList<Color> shapeListColor = new ArrayList<>();
    private ArrayList<Boolean> shapeListFilling = new ArrayList<>();
    private ArrayList<String> textList = new ArrayList<>();
    private ArrayList<Point2D> textPosList = new ArrayList<>();
    private float currentBrushSize = 1;
    private Color currentColor = Color.BLACK;
    private boolean currentFilling = false;

    private ArrayList<Integer> ptsXList = new ArrayList<>();
    private ArrayList<Integer> ptsYList = new ArrayList<>();
    private String currentText;
    private boolean isWritting = false;
    private drawMode dMode = drawMode.DEFAULT;
    private shapeMode sMode = shapeMode.RECTANGLE;

    private int x1,y1,x2,y2 = 0;

    public LocalDrawBoardComponent(ClientRMI clientRMI){
        this.clientRMI = clientRMI;
        this.setPreferredSize(new Dimension(1024, 768));


        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if(!isWritting) return;
                else if (e.getKeyChar() == KeyEvent.VK_ENTER){
                    try {
                        clientRMI.request_drawText(currentText, x1, y1);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    currentText="";
                    isWritting = false;
                } else{
                    currentText += e.getKeyChar();
                }
            }
        });
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
                        break;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                switch (dMode){
                    case FREE:
                        ptsXList.add(e.getX());
                        ptsYList.add(e.getY());
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
                            clientRMI.request_drawShape(sMode, x1, y1, x2, y2, currentBrushSize, currentFilling, currentColor.getRGB());
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;

                    case POLYGON:
                        if(SwingUtilities.isLeftMouseButton(e)){
                            ptsXList.add(e.getX());
                            ptsYList.add(e.getY());
                        } else if(SwingUtilities.isRightMouseButton(e) && ptsXList.size() > 2){
                            ptsXList.add(e.getX());
                            ptsYList.add(e.getY());


                            try {
                                clientRMI.request_drawPolygon(convertIntegers(ptsXList), convertIntegers(ptsYList), currentBrushSize, currentFilling, currentColor.getRGB());
                            } catch (RemoteException ex) {
                                throw new RuntimeException(ex);
                            }
                            clearPtsList();
                        }
                        break;

                    case FREE:
                        try {
                            clientRMI.request_drawFree(convertIntegers(ptsXList), convertIntegers(ptsYList), currentBrushSize, currentFilling, currentColor.getRGB());
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                        clearPtsList();
                        break;

                    case TEXTMODE:
                        if(SwingUtilities.isLeftMouseButton(e)){
                            requestFocus();
                            x1 = e.getX();
                            y1 = e.getY();
                            currentText="";
                            isWritting=true;
                        } else if(SwingUtilities.isRightMouseButton(e)){
                            currentText="";
                            isWritting=false;
                        }
                        break;
                }

                repaint();
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    public float getCurrentBrushSize(){return this.currentBrushSize;}
    public void setCurrentBrushSize(float size){this.currentBrushSize = size;}
    public Color getCurrentColor(){return this.currentColor;}
    public void setCurrentColor(Color color){this.currentColor = color;}
    public boolean getCurrentFilling(){return this.currentFilling;}
    public void setCurrentFilling(boolean filling){this.currentFilling = filling;}
    public void setDrawMode(drawMode mode){
        this.dMode = mode;
        clearPtsList();
    }
    public void setShapeMode(shapeMode mode){this.sMode = mode;}

    private void clearPtsList(){
        ptsXList.clear();
        ptsYList.clear();
    }

    public void drawShape(LocalDrawBoardComponent.shapeMode mode, int x1, int y1, int x2, int y2, float brushSize, boolean filling, int colorRgb){
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
                s = new TriangleShape(pt1, pt2, pt3);
                break;
            case LINE:
                s = new Line2D.Float(x1, y1, x2, y2);
                break;
        }


        addShapeToBoard(s, brushSize, filling, colorRgb);
        repaint();
    }

    public void drawPoly(int[] xList, int[] yList, float brushSize, boolean filling, int rgb){
        Point2D[] pts = new Point2D[xList.length];
        for(int i = 0; i < xList.length; i++){
            pts[i] = new Point2D.Float(xList[i], yList[i]);
        }

        Shape s = new PolygonShape(pts);
        addShapeToBoard(s, brushSize, filling, rgb);
        repaint();
    }

    public void drawFree(int[] xList, int[] yList, float brushSize, boolean filling, int rgb){
        if(xList.length == 0) return;

        Path2D path = new Path2D.Double();
        path.moveTo(xList[0], yList[0]);

        for (int i = 1; i < xList.length; i++){
            path.lineTo(xList[i], yList[i]);
        }

        addShapeToBoard(path, brushSize, filling, rgb);
        repaint();
    }

    public void drawText(String text, int x, int y){
        drawingTypeList.add(DrawingType.TEXT);
        textList.add(text);
        textPosList.add(new Point2D.Float(x, y));
        repaint();
    }

    private void addShapeToBoard(Shape s, float brushSize, boolean filling, int rgb){
        drawingTypeList.add(DrawingType.SHAPE);
        shapeList.add(s);
        shapeListBrushSize.add(brushSize);
        shapeListFilling.add(filling);
        shapeListColor.add(new Color(rgb));
    }

    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for(int i = 0; i < shapeList.size(); i++){
            g2D.setStroke(new BasicStroke(shapeListBrushSize.get(i)));
            g2D.setColor(shapeListColor.get(i));


        int i = 0; int j = 0;
        for(DrawingType dt: drawingTypeList){
            switch (dt){
                case SHAPE:
                    g2D.setStroke(new BasicStroke(shapeListBrushSize.get(i)));
                    g2D.setColor(shapeListColor.get(i));

                    Shape s = shapeList.get(i);
                    if(shapeListFilling.get(i)) g2D.fill(s);
                    g2D.draw(s);
                    i++;
                    break;

                case TEXT:
                    //TODO FIX COLOR + FONT
                    g2D.drawString(textList.get(j), (int) textPosList.get(j).getX(), (int) textPosList.get(j).getY());
                    j++;
                    break;
            }
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

    class PolygonShape extends Path2D.Double{
        public PolygonShape(Point2D... points){
            moveTo(points[0].getX(), points[0].getY());
            for(int i = 1; i < points.length; i++){
                lineTo(points[i].getX(), points[i].getY());
            }
            closePath();
        }
    }

    //https://stackoverflow.com/questions/718554/how-to-convert-an-arraylist-containing-integers-to-primitive-int-array
    public static int[] convertIntegers(ArrayList<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }

    public enum drawMode{
        DEFAULT,
        SHAPE,
        POLYGON,
        FREE,
        TEXTMODE,
        ERASE
    }

    public enum shapeMode{
        RECTANGLE,
        ELLIPSE,
        TRIANGLE,
        LINE
    }

    public enum DrawingType {
        SHAPE,
        TEXT
    }
}
