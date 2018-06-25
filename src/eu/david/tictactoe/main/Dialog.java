package eu.david.tictactoe.main;

import javax.swing.*;

public class Dialog extends JFrame {

    JPanel panel;
    JLabel label;

    public Dialog(int mode) {

        super("TicTacToe Menu");

        panel = new JPanel();

        String[] messages = {
                "",

                "<html><font size=\"8\" color=\"red\">"
                        + "Maybe you have better luck next time :/"
                        + "</font></html>",

                "<html><font size=\"8\" color=\"orange\">"
                        + "Draw!"
                        + "</font></html>",

                "<html><font size=\"8\" color=\"lime\">"
                        + "\"Congratiolatins you won!"
                        + "</font></html>",
        };

        label = new JLabel(messages[mode]);

        setSize(800, 450);
        setLocationRelativeTo(null);
        setVisible(true);

        /*"Congratiolatins you won!",
                "Maybe you have better luck next time :/",
                "Draw!"*/

        panel.add(label);
        add(panel);
    }

}
