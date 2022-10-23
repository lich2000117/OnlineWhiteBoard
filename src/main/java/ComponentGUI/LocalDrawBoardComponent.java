package ComponentGUI;

import client.ClientRMI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class LocalDrawBoardComponent extends JPanel {
    private ClientRMI clientRMI;

    public String fileName = null;
    private ArrayList<DrawingType> drawingTypeList = new ArrayList();
    private ArrayList<Shape> shapeList = new ArrayList<>();
    private ArrayList<Float> shapeListBrushSize = new ArrayList<>();
    private ArrayList<Color> shapeListColor = new ArrayList<>();
    private ArrayList<Boolean> shapeListFilling = new ArrayList<>();
    private ArrayList<String> textList = new ArrayList<>();
    private ArrayList<Point2D> textPosList = new ArrayList<>();
    private ArrayList<Color> colorTextList = new ArrayList<>();
    private ArrayList<String> fontNameList = new ArrayList<>();
    private ArrayList<Integer> fontStyleList = new ArrayList<>();
    private ArrayList<Integer> fontSizeList = new ArrayList<>();
    private float currentBrushSize = 2.5f;
    private Color currentColor = Color.BLACK;
    private boolean currentFilling = false;
    private Shape tempShape = null;

    private ArrayList<Integer> ptsXList = new ArrayList<>();
    private ArrayList<Integer> ptsYList = new ArrayList<>();
    private String currentText = "";
    private String currentFontName = "Dialog";
    private int currentFontStyle = Font.PLAIN;
    private int currentFontSize = 14;
    private boolean isWritting = false;
    private drawMode dMode = drawMode.DEFAULT;
    private shapeMode sMode = shapeMode.RECTANGLE;

    private int x1,y1,x2,y2 = 0;

    public LocalDrawBoardComponent(ClientRMI clientRMI){
        this.clientRMI = clientRMI;
        this.setPreferredSize(new Dimension(800, 600));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setBackground(Color.WHITE);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if(!isWritting) return;
                else if (e.getKeyChar() == KeyEvent.VK_ENTER){
                    try {
                        clientRMI.request_drawText(currentText, x1, y1, currentFontName, currentFontStyle, currentFontSize, currentColor.getRGB());
                    } catch (RemoteException ex) {
                        clientRMI.kickClientUI("Lost Connection to Server");
                    }
                    currentText="";
                    isWritting = false;
                } else if(e.getKeyChar() == KeyEvent.VK_BACK_SPACE){
                    if (currentText.length() > 0) currentText = currentText.substring(0, currentText.length()-1);
                    repaint();
                }else{
                    currentText += e.getKeyChar();
                    repaint();
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
                    case SHAPE:
                        x2 = e.getX();
                        y2 = e.getY();

                        drawShape(sMode, x1, y1, x2, y2, currentBrushSize, currentFilling, currentColor.getRGB(), true);
                        break;
                    case FREE:
                        ptsXList.add(e.getX());
                        ptsYList.add(e.getY());

                        drawFree(convertIntegers(ptsXList), convertIntegers(ptsYList), currentBrushSize, currentFilling, currentColor.getRGB(), true);
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
                            clientRMI.request_drawShape(sMode, x1, y1, x2, y2, currentBrushSize, currentFilling, currentColor.getRGB());
                        } catch (RemoteException ex) {
                            clientRMI.kickClientUI("Lost Connection to Server");
                        }
                        break;

                    case POLYGON:
                        if(SwingUtilities.isLeftMouseButton(e)){
                            ptsXList.add(e.getX());
                            ptsYList.add(e.getY());

                            drawPoly(convertIntegers(ptsXList), convertIntegers(ptsYList), currentBrushSize, currentFilling, currentColor.getRGB(), true);
                        } else if(SwingUtilities.isRightMouseButton(e) && ptsXList.size() > 2){
                            ptsXList.add(e.getX());
                            ptsYList.add(e.getY());


                            try {
                                clientRMI.request_drawPolygon(convertIntegers(ptsXList), convertIntegers(ptsYList), currentBrushSize, currentFilling, currentColor.getRGB());
                                tempShape = null;
                            } catch (RemoteException ex) {
                                clientRMI.kickClientUI("Lost Connection to Server");
                            }
                            clearPtsList();
                        }
                        break;

                    case FREE:
                        try {
                            clientRMI.request_drawFree(convertIntegers(ptsXList), convertIntegers(ptsYList), currentBrushSize, currentFilling, currentColor.getRGB());
                        } catch (RemoteException ex) {
                            clientRMI.kickClientUI("Lost Connection to Server");
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
    public String getCurrentFontName(){return this.currentFontName;}
    public void setCurrentFontName(String name){this.currentFontName = name;}
    public int getCurrentFontStyle(){return this.currentFontStyle;}
    public void setCurrentFontStyle(int style){this.currentFontStyle = style;}
    public int getCurrentFontSize(){return this.currentFontSize;}
    public void setCurrentFontSize(int size){this.currentFontSize = size;}

    public void setDrawMode(drawMode mode){
        this.dMode = mode;
        currentText = "";
        clearPtsList();
    }
    public void setShapeMode(shapeMode mode){this.sMode = mode;}

    private void clearPtsList(){
        ptsXList.clear();
        ptsYList.clear();
    }

    public void drawShape(LocalDrawBoardComponent.shapeMode mode, int x1, int y1, int x2, int y2, float brushSize, boolean filling, int colorRgb, boolean temporary){
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


        if(temporary){
            tempShape = s;
            //printShapeOnBoard(s, brushSize, filling, colorRgb);
        } else{
            tempShape = null;
            addShapeToBoard(s, brushSize, filling, colorRgb);
        }
        repaint();
    }

    public void drawPoly(int[] xList, int[] yList, float brushSize, boolean filling, int rgb, boolean temporary){
        Point2D[] pts = new Point2D[xList.length];
        for(int i = 0; i < xList.length; i++){
            pts[i] = new Point2D.Float(xList[i], yList[i]);
        }

        Shape s = new PolygonShape(pts);

        if(temporary){
            tempShape = s;
        } else{
            addShapeToBoard(s, brushSize, filling, rgb);
        }
        repaint();
    }

    public void drawFree(int[] xList, int[] yList, float brushSize, boolean filling, int rgb, boolean temporary){
        if(xList.length == 0) return;

        Path2D path = new Path2D.Double();
        path.moveTo(xList[0], yList[0]);

        for (int i = 1; i < xList.length; i++){
            path.lineTo(xList[i], yList[i]);
        }

        if(temporary){
            tempShape = path;
        } else{
            addShapeToBoard(path, brushSize, filling, rgb);
        }
        repaint();
    }

    public void drawText(String text, int x, int y, String name, int style, int size, int rgb){
        drawingTypeList.add(DrawingType.TEXT);
        textList.add(text);
        textPosList.add(new Point2D.Float(x, y));
        colorTextList.add(new Color(rgb));
        fontNameList.add(name);
        fontStyleList.add(style);
        fontSizeList.add(size);
        repaint();
    }

    private void addShapeToBoard(Shape s, float brushSize, boolean filling, int rgb){
        drawingTypeList.add(DrawingType.SHAPE);
        shapeList.add(s);
        shapeListBrushSize.add(brushSize);
        shapeListFilling.add(filling);
        shapeListColor.add(new Color(rgb));
    }

    //Function used to draw on the board based on given component
    private void printShapeOnBoard(Graphics2D g2D, Shape s, float brushSize, boolean filling, Color color){
        g2D.setStroke(new BasicStroke(brushSize));
        g2D.setColor(color);

        if(filling) g2D.fill(s);
        g2D.draw(s);
    }

    private void printTextOnBoard(Graphics2D g2D, String text, int x, int y, String name, int style, int size, Color color){
        g2D.setColor(color);
        g2D.setFont(new Font(name, style, size));
        g2D.drawString(text, x, y);
    }

    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int i = 0; int j = 0;
        for(int m=0;m<drawingTypeList.size();m++){
            DrawingType dt = drawingTypeList.get(m);
            switch (dt){
                case SHAPE:
                    printShapeOnBoard(g2D, shapeList.get(i), shapeListBrushSize.get(i), shapeListFilling.get(i), shapeListColor.get(i));
                    i++;
                    break;

                case TEXT:
                    printTextOnBoard(g2D, textList.get(j), (int) textPosList.get(j).getX(), (int) textPosList.get(j).getY(), fontNameList.get(j), fontStyleList.get(j), fontSizeList.get(j), colorTextList.get(j));
                    j++;
                    break;
            }
        }

        printTextOnBoard(g2D, currentText, x1, y1, currentFontName, currentFontStyle, currentFontSize, currentColor);
        if (tempShape != null) {
            printShapeOnBoard(g2D, tempShape, currentBrushSize, currentFilling, currentColor);
        }
    }


    public void cleanBoard(){
        tempShape = null;

        drawingTypeList.clear();
        shapeList.clear();
        shapeListBrushSize.clear();
        shapeListColor.clear();
        shapeListFilling.clear();
        textList.clear();
        textPosList.clear();
        colorTextList.clear();
        fontNameList.clear();
        fontStyleList.clear();
        fontSizeList.clear();

        repaint();
    }
    public void openFile(){
        try{
            FileInputStream fo = new FileInputStream(fileName);
            clientRMI.request_openFile(fo.readAllBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void saveFile(byte[] file, String filename){
        try{
            FileOutputStream fo = new FileOutputStream(filename);
            fo.write(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // https://stackoverflow.com/questions/8202253/saving-a-java-2d-graphics-image-as-png-file
    public void savePaintAsPng(String fName)
    {
        BufferedImage bImg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D cg = bImg.createGraphics();
        paintAll(cg);
        try {
            ImageIO.write(bImg, "png", new File(fName));
        } catch (IOException e) {
            e.printStackTrace();
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
