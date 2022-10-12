package ComponentGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A dialogue pop up for every user joining.
 * return selected user name.
 */

public class JoinDialogue {
    private static String selectedName;

    public JoinDialogue(String msg) {
        popUpInput(msg);
    }

    private static void popUpInput(String msg) {
        if (msg.equals("")){
            msg = "Please Choose a UserName";
        }
        selectedName = (String) JOptionPane.showInputDialog(
                null,
                msg,
                "Welcome on BOARD!",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "NewUser"
        );
    }

    public static String getSelectedName() {
        return selectedName;
    }
}
