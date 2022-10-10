package ComponentGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ImageButton extends JButton {
    ImageIcon defaultIcon;
    ImageIcon selectedIcon;

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

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                select();
            }
        });
    }

    public void select(){
        setIcon(selectedIcon);
    }

    public void unselect(){
        setIcon(defaultIcon);
    }
}
