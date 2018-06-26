package eu.david.tictactoe.main;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class StartFrame extends JFrame {

    JPanel panel;

    JSlider[] sliders = new JSlider[3];
    JLabel[] infoLabels = new JLabel[3];
    JLabel[] counterLabels = new JLabel[3];

    JButton startButton;

    int min = 3;
    int max = 15;

    public StartFrame() {

        super("TicTacToe Menu");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }

        panel = new JPanel(new GridLayout(4, 3));

        String prefix = "<html><font size=\"5\">";
        String suffix = "</font></html>";
        String[] infos = {"columns", "rows", "characters in row to win"};

        for (int i = 0; i < infoLabels.length; i++) {

            infoLabels[i] = new JLabel(prefix+infos[i]+suffix);
            infoLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(infoLabels[i]);
        }

        Listener changeListener = new Listener();
        for (int i = 0; i < sliders.length; i++) {
            sliders[i] = new JSlider(min, max, 3);
            sliders[i].setMajorTickSpacing(1);
            sliders[i].setPaintTicks(true);
            sliders[i].addChangeListener(changeListener);
            panel.add(sliders[i]);
        }

        for (int i = 0; i < counterLabels.length; i++) {
            counterLabels[i] = new JLabel(""+min);
            counterLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(counterLabels[i]);
        }



        startButton = new JButton("start game");

        panel.add(startButton);

        add(panel);
        setSize(700, 180);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        startButton.addActionListener(event -> startGame());
    }

    public class Listener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent event) {

            for (int i = 0; i < counterLabels.length; i++) {
                counterLabels[i].setText(""+sliders[i].getValue());
            }
        }
    }

    void startGame() {

        int columns = sliders[0].getValue();
        int rows = sliders[1].getValue();
        int fieldInRowToWin = sliders[2].getValue();
        if (fieldInRowToWin > columns && fieldInRowToWin > rows) {
            return;
        }
        setVisible(false);
        GameFrame.columns = columns;
        GameFrame.rows = rows;
        GameFrame.fieldInRowToWin = fieldInRowToWin;
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

    private void restart() {startGame();}

    private void changeSettings() {setVisible(true);}

    private void exit() {dispose();}

}
