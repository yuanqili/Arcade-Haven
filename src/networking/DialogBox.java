import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DialogBox {

    public static void main(String[] args) {
        new DialogBox();
    }

    private DialogBox() {

        JFrame guiFrame = new JFrame();

        guiFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        guiFrame.setTitle("Dialog box");
        guiFrame.setSize(300, 250);



        guiFrame.setVisible(true);
    }

}