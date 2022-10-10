package ComponentGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageColoredButton extends ImageButton{

    private BufferedImage bufImg;
    private Dimension dim;

    public ImageColoredButton(String defaultImagePath, String selectedImagePath, Dimension dimension) {
        super(defaultImagePath, selectedImagePath, dimension);
        dim = dimension;

        try{
            bufImg = ImageIO.read(new File(defaultImagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setColor(Color color){
        for(int i = 0; i < bufImg.getWidth(); i++){
            for(int j = 0; j < bufImg.getHeight(); j++){
                bufImg.setRGB(i, j, color.getRGB());
            }
        }

        ImageIcon icon = new ImageIcon(bufImg.getScaledInstance(dim.width, dim.height, Image.SCALE_SMOOTH ));
        setIcon(icon);
    }
}
