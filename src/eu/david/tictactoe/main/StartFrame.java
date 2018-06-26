package eu.david.tictactoe.main;

import javax.swing.*;

public class StartFrame extends JFrame {

    JPanel panel;

    JSlider rowSlider;
    JSlider columnSlider;
    JSlider fieldInRowToWinSlider;

    JButton startButton;

    int option;

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
        columnSlider = new JSlider(3, 15, 3);
        fieldInRowToWinSlider = new JSlider();

        startButton = new JButton("start game");

        panel.add(rowSlider);
        panel.add(columnSlider);
        panel.add(fieldInRowToWinSlider);
        panel.add(startButton);

        add(panel);
        setSize(800, 450);
        setLocationRelativeTo(null);
        setVisible(true);

        startButton.addActionListener(event -> startGame());
    }

    void startGame() {

        setVisible(false);
        GameFrame.columns = 4;
        GameFrame.rows = 4;
        GameFrame.fieldInRowToWin = 3;
        GameFrame game = new GameFrame();

        Thread checkGameState = new Thread(new Runnable() {
            @Override
            public void run() {

                while (!game.hasFinished) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Dialog dialog = new Dialog(game.finishState, (JFrame)game);
                game.dispose();
                switch (dialog.choice) {
                    case 1 : restart();
                        break;
                    case 2 : changeSettings();
                        break;
                    case 3 : exit();
                        break;
                }
            }
        });

        checkGameState.start();
    }

    private void restart() {
        startGame();
    }

    private void changeSettings() {
        setVisible(true);
    }

    private void exit() {
        dispose();
    }

}
