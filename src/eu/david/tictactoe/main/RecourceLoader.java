package eu.david.tictactoe.main;

import javax.swing.*;
import java.awt.*;

public class RecourceLoader {

    static RecourceLoader rl = new RecourceLoader();

    public static ImageIcon getIcon(String fileName) {

        return new ImageIcon(Toolkit.getDefaultToolkit().getImage(rl.getClass().getResource("res\\"+fileName)));
    }

}
