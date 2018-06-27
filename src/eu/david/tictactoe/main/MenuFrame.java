package eu.david.tictactoe.main;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

class MenuFrame extends JFrame {

    private JSlider[] sliders = new JSlider[3];
    private JLabel[] counterLabels = new JLabel[3];

    private JCheckBox gravityBox;
    private JComboBox modeBox;

    MenuFrame() {
        super("TicTacToe Menu");
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel;
        JLabel[] infoLabels = new JLabel[3];
        JButton startButton;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
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

        int min = 3;
        int max = 15;

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

        gravityBox = new JCheckBox("gravity");
        gravityBox.setHorizontalAlignment(SwingConstants.CENTER);
        modeBox = new JComboBox(new String[] {"play against bot", "play against other player"});
        startButton = new JButton("start game");

        panel.add(gravityBox);
        panel.add(modeBox);
        panel.add(startButton);

        add(panel);
        setSize(700, 160);
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

    private void startGame() {

        int columns = sliders[0].getValue();
        int rows = sliders[1].getValue();
        int fieldInRowToWin = sliders[2].getValue();
        if (fieldInRowToWin > columns && fieldInRowToWin > rows) {
            return;
        }
        boolean gravity = gravityBox.isSelected();
        int mode = modeBox.getSelectedIndex();

        setVisible(false);

        GameFrame game = new GameFrame(columns, rows, fieldInRowToWin, gravity, mode);

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
                Dialog dialog = new Dialog(game, game.finishState, mode);
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
