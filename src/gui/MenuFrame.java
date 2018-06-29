package gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class MenuFrame extends JFrame {

    private JPanel panel;

    private JSlider[] sliders = new JSlider[3];
    private JLabel[] infoLabels = new JLabel[3];
    private JLabel[] counterLabels = new JLabel[3];

    private JCheckBox gravityBox;
    private JComboBox modeBox;
    private JButton startButton;

    public MenuFrame() {

        super("TicTacToe Menu");

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

        int min = 3;
        int max = 15;

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
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        startButton.addActionListener(event -> startGame());
    }

    private class Listener implements ChangeListener {

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
        int fieldsInRowToWin = sliders[2].getValue();
        if (fieldsInRowToWin > columns && fieldsInRowToWin > rows) {
            return;
        }
        boolean gravity = gravityBox.isSelected();
        int mode = modeBox.getSelectedIndex();

        setVisible(false);

        GameFrame game = new GameFrame(columns, rows, fieldsInRowToWin, gravity, mode);

        Thread checkGameState = new Thread(() -> {

            while (!game.isFinished()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            if (game.getFinishState() == -1) {
                game.dispose();
                exit();
            }
            Dialog dialog = new Dialog(game, game.getFinishState(), mode);
            game.dispose();
            switch (dialog.getChoice()) {
                case 1 : restart();
                    break;
                case 2 : changeSettings();
                    break;
                case 3 : exit();
                    break;
            }
        });

        checkGameState.start();
    }

    private void restart() {startGame();}

    private void changeSettings() {setVisible(true);}

    private void exit() {
        dispose();
        System.exit(0);
    }

}