package client;

import ComponentGUI.LocalDrawBoardComponent;
import remote.iClient;
import remote.iServer;

import java.util.ArrayList;

public class ClientUI {
    private LocalDrawBoard frame;

    public ClientUI(ClientRMI clientRMI) {
        this.frame = new LocalDrawBoard(clientRMI);
    }

    public void drawShape(LocalDrawBoardComponent.shapeMode mode, int x1, int y1, int x2, int y2, float brushSize, boolean filling, int rgb){
        this.frame.getWhiteBoard().drawShape(mode, x1, y1, x2, y2, brushSize, filling, rgb, false);
    }

    public void drawPolygon(int[] lstX, int[] lstY, float brushSize, boolean filling, int rgb){
        this.frame.getWhiteBoard().drawPoly(lstX, lstY, brushSize, filling, rgb, false);
    }

    public void drawFree(int[] lstX, int[] lstY, float brushSize, boolean filling, int rgb){
        this.frame.getWhiteBoard().drawFree(lstX, lstY, brushSize, filling, rgb, false);
    }

    public void drawText(String text, int x, int y, String name, int style, int size, int rgb){
        this.frame.getWhiteBoard().drawText(text, x, y, name, style, size, rgb);
    }

    public void sendChatMessage(String username, String message){
        this.frame.getChatPanel().addMessage(username, message);
    }

    public void updateUserList(ArrayList<String> userList) {
        this.frame.getUserPanel().updateUserList(userList);
    }

    public void kickClient(String msg){
        this.frame.DisableUI_WithMessage(msg, true);
    }

    public void displayAlert(String msg, Boolean closeWindow){
        this.frame.DisplayMessage(msg, closeWindow);
    }

    public boolean displayUserJoinRequest(String userName){
        String msg = "User: " + userName + " ask to join.";
        return this.frame.ConfirmWindow(msg)==0;
    }
}
