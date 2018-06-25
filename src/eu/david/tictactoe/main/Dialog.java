package eu.david.tictactoe.main;

import javax.swing.*;

public class Dialog extends JFrame {

    JPanel panel;
    JLabel label;

    public Dialog(int winner) {

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

        label = new JLabel(messages[winner]);

        setSize(800, 450);
        setLocationRelativeTo(null);
        setVisible(true);

        panel.add(label);
        add(panel);
    }

}
