package eu.david.tictactoe.main;

import javax.swing.*;

public class Dialog extends JOptionPane {

    int choice = 0;

    String[] options = {
            "revanche",
            "change settings",
            "exit"
    };
    String[][] messages = {
            {
                    "<html><font size=\"8\" color=\"orange\">"
                            + "Draw!"
                            + "</font></html>",

                    "<html><font size=\"8\" color=\"red\">"
                            + "Bot has won the game :/"
                            + "</font></html>",

                    "<html><font size=\"8\" color=\"lime\">"
                            + "Congratiolatins you won the game!"
                            + "</font></html>"
            },
            {
                    "<html><font size=\"8\" color=\"orange\">"
                            + "Draw!"
                            + "</font></html>",

                    "<html><font size=\"8\" color=\"lime\">"
                            + "Player 2 has won the game!"
                            + "</font></html>",

                    "<html><font size=\"8\" color=\"lime\">"
                            + "Player 1 has won the game!"
                            + "</font></html>"
            }
    };

    public Dialog(JFrame frame, int winner, int mode) {

        if (mode == 0) {
            choice = this.showOptionDialog(
                    frame,
                    messages[mode][winner],
                    "Game Finished",
                    DEFAULT_OPTION,
                    PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            )+1;
        }
        else {
            choice = this.showOptionDialog(
                    frame,
                    messages[mode][winner],
                    "Game Finished",
                    DEFAULT_OPTION,
                    PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            )+1;
        }
    }
}
