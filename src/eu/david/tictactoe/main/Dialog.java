package eu.david.tictactoe.main;

import javax.swing.*;

public class Dialog extends JOptionPane {

    int choice = 0;

    String[] options = {
            "restart",
            "change settings",
            "exit"
    };
    String[] messages = {
            "<html><font size=\"8\" color=\"orange\">"
                    + "Draw!"
                    + "</font></html>",

            "<html><font size=\"8\" color=\"red\">"
                    + "Maybe you have better luck next time :/"
                    + "</font></html>",

            "<html><font size=\"8\" color=\"lime\">"
                    + "Congratiolatins you won!"
                    + "</font></html>"
    };

    public Dialog(int winner, JFrame frame) {

        choice = this.showOptionDialog(
                frame,
                messages[winner],
                "Game Finished",
                DEFAULT_OPTION,
                PLAIN_MESSAGE,
                null,
                options,
                options[0]
        )+1;

    }

}
