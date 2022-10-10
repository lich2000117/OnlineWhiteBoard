package ComponentGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageButton extends JButton {
    ImageIcon defaultIcon;
    ImageIcon selectedIcon;
    private boolean isSelected = false;

    public ImageButton(String defaultImagePath, String selectedImagePath, Dimension dimension){
        Image img = new ImageIcon(defaultImagePath).getImage();
        Image newimg = img.getScaledInstance(dimension.width, dimension.height, java.awt.Image.SCALE_SMOOTH ) ;
        defaultIcon = new ImageIcon( newimg );

        Image img2 = new ImageIcon(selectedImagePath).getImage();
        Image newimg2 = img2.getScaledInstance( dimension.width, dimension.height,  java.awt.Image.SCALE_SMOOTH ) ;
        selectedIcon = new ImageIcon( newimg2 );

        setIcon(defaultIcon);
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);
    }

    public boolean isSelected(){
        return isSelected;
    }
    public void select(){
        isSelected = true;
        setIcon(selectedIcon);
    }

    public void unselect(){
        isSelected = false;
        setIcon(defaultIcon);
    }
}
