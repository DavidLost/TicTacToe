package eu.david.tictactoe.main;

import javax.swing.*;

public class StartFrame extends JFrame {

    JPanel panel;

    JSlider columns;
    JSlider rows;
    JSlider fieldInRowToWin;

    public StartFrame() {

        super("TicTacToe Menu");

        GameFrame.columns = 6;
        GameFrame.rows = 6;
        GameFrame.fieldInRowToWin = 3;
        new GameFrame();
    }

}
