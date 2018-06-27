package res;

import javax.swing.*;
import java.awt.*;

public class ResourceLoader {

    static ResourceLoader rl = new ResourceLoader();

    public static ImageIcon getIcon(String fileName) {

        return new ImageIcon(Toolkit.getDefaultToolkit().getImage(rl.getClass().getResource("icons\\"+fileName)));
    }

}
