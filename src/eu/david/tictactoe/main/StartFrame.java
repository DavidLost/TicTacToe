package eu.david.tictactoe.main;

import javax.swing.*;

public class StartFrame extends JFrame {

    JPanel panel;

    JSlider rowSlider;
    JSlider columnSlider;
    JSlider fieldInRowToWinSlider;

    public StartFrame() {

        super("TicTacToe Menu");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }

        panel = new JPanel();

        rowSlider = new JSlider(3, 15, 3);
        //rowSlider.
        columnSlider = new JSlider(3, 15, 3);
        fieldInRowToWinSlider = new JSlider();

        panel.add(rowSlider);
        panel.add(columnSlider);
        panel.add(fieldInRowToWinSlider);

        add(panel);
        setSize(800, 450);
        setLocationRelativeTo(null);
        setVisible(true);


        GameFrame.columns = 3;
        GameFrame.rows = 3;
        GameFrame.fieldInRowToWin = 3;
        new GameFrame();
    }

}
