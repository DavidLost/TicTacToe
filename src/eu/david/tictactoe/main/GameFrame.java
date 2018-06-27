package eu.david.tictactoe.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import res.ResourceLoader;

import static java.lang.Integer.parseInt;

class GameFrame extends JFrame {

    boolean hasFinished = false;
    int finishState = -1;

    private int sizeProportion = 100;
    private int columns;
    private int rows;
    private int fieldInRowToWin;
    private int allFields;
    private boolean gravity;
    private int mode;
    private boolean playerTurn = false;

    private JButton[][] buttons;
    private byte[][] matrix;
    private int[][] winCombo;

    private int gameCounter = 0;
    private ImageIcon kreutzIcon = ResourceLoader.getIcon("kreutz.png");
    private ImageIcon kreisIcon = ResourceLoader.getIcon("kreis.png");

    public GameFrame(int columns, int rows, int fieldInRowToWin, boolean gravity, int mode) {

        super("TicTacToe");

        this.columns = columns;
        this.rows = rows;
        this.fieldInRowToWin = fieldInRowToWin;
        this.allFields = columns*rows;
        this.gravity = gravity;
        this.mode = mode;

        matrix = new byte[rows][columns];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                matrix[y][x] = 0;
            }
        }
        winCombo = new int[2][fieldInRowToWin];

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(rows, columns));

        buttons = new JButton[rows][columns];

        for (int y = 0 ; y < rows; y++) {
            for (int x = 0 ; x < columns; x++) {
                buttons[y][x] = new JButton();
                String name = "button";
                if (y < 10) {name += "0";}
                name += y+"-";
                if (x < 10) {name += "0";}
                name += x;
                buttons[y][x].setName(name);
                System.out.println("name: "+name);
                panel.add(buttons[y][x]);
                buttons[y][x].addActionListener(event -> actions(event));
            }
        }

        add(panel);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);

        getNewSizeProportion();

        getContentPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {

                getNewSizeProportion();
                System.out.println("sizeProportion: "+sizeProportion);
                updateButtonIcons();
            }
        });
    }


    private void getNewSizeProportion() {

        double boxHeight = getHeight()/rows;
        double boxWidth = getWidth()/columns;
        double length = boxHeight < boxWidth ? boxHeight : boxWidth;

        sizeProportion = (int)(length/kreisIcon.getIconHeight()*100);
    }

    private void updateButtonIcons() {

        for (int y = 0; y < columns; y++) {
            for (int x = 0; x < rows; x++) {
                switch (matrix[y][x]) {
                    case 1 : buttons[y][x].setIcon(scaleImageIcon(kreutzIcon, sizeProportion));
                        break;
                    case 2 : buttons[y][x].setIcon(scaleImageIcon(kreisIcon, sizeProportion));
                }
            }
        }
    }

    ImageIcon scaleImageIcon(ImageIcon icon, int percent) {

        Image temp = icon.getImage();
        temp = temp.getScaledInstance(icon.getIconWidth()*percent/100, icon.getIconHeight()*percent/100, Image.SCALE_SMOOTH);
        return new ImageIcon(temp);
    }

    private void actions(ActionEvent event) {

        if (mode == 1) {
            if (playerActions(event, playerTurn)) {return;}
            if (checkForFinish()) {return;}
            playerTurn = !playerTurn;
        }
        else {
            if (playerActions(event, playerTurn)) {return;}
            if (checkForFinish()) {return;}
            botActions();
            if (checkForFinish()) {return;}
        }
    }

    private boolean playerActions(ActionEvent event, boolean player) {

        String temp = event.toString();
        int length = temp.length();
        int x = parseInt(temp.substring(length-2, length));
        int y = parseInt(temp.substring(length-5, length-3));
        if (gravity) {
            y = getGravityY(y, x);
        }
        if (matrix[y][x] != 0) {
            return true;
        }
        if (!player) {
            matrix[y][x] = 1;
            buttons[y][x].setIcon(scaleImageIcon(kreutzIcon, sizeProportion));
        }
        else {
            matrix[y][x] = 2;
            buttons[y][x].setIcon(scaleImageIcon(kreisIcon, sizeProportion));
        }

        if (mode == 0) {gameCounter += 2;}
        else {gameCounter++;}

        return false;
    }

    private boolean checkForFinish() {

        if (isWinner(1)) {
            hasFinished = true;
            finishState = 2;
        }
        else if (isWinner(2)) {
            hasFinished = true;
            finishState = 1;
        }
        else if (isfieldFull()) {
            hasFinished = true;
            finishState = 0;
        }

        return hasFinished;
    }

    private void botActions() {

        int temp[] = getBotButton();
        int y = temp[0];
        int x = temp[1];
        if (gravity) {
            y = getGravityY(y, x);
        }
        matrix[y][x] = 2;
        buttons[y][x].setIcon(scaleImageIcon(kreisIcon, sizeProportion));
    }

    private int getGravityY(int y, int x) {

        try {
            while (matrix[y+1][x] == 0) {
                y++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {}

        return y;
    }

    private int[] getBotButton() {

        Random generator = new Random(ThreadLocalRandom.current().nextInt());
        int y;
        int x;
        do {
            y = generator.nextInt(rows);
            x = generator.nextInt(columns);
        } while (matrix[y][x] != 0);

        return new int[] {y, x};
    }

    private boolean isfieldFull() {

        return gameCounter >= allFields-1;
    }

    private boolean isWinner(int type) {

        //check for horizontal row
        for (int locY = 0; locY < rows; locY++) {
            for (int locX = 0; locX < columns-fieldInRowToWin+1; locX++) {
                int counter = 0;
                for (int partsX = locX; partsX < fieldInRowToWin+locX; partsX++) {
                    if (matrix[locY][partsX] == type) {
                        winCombo[0][counter] = locY;
                        winCombo[1][counter] = partsX;
                        counter++;
                    }
                }
                if (counter == fieldInRowToWin) {
                    return true;
                }
            }
        }

        //check for vertical row
        for (int locX = 0; locX < columns; locX++) {
            for (int locY = 0; locY < rows-fieldInRowToWin+1; locY++) {
                int counter = 0;
                for (int partsY = locY; partsY < fieldInRowToWin+locY; partsY++) {
                    if (matrix[partsY][locX] == type) {
                        winCombo[0][counter] = partsY;
                        winCombo[1][counter] = locX;
                        counter++;
                    }
                }
                if (counter == fieldInRowToWin) {
                    return true;
                }
            }
        }

        //check for diagonal row
        for (int locY = 0; locY < rows-fieldInRowToWin+1; locY++) {
            for (int locX = 0; locX < columns-fieldInRowToWin+1; locX++) {
                int counter = 0;
                for (int partsX = locX; partsX < fieldInRowToWin+locX; partsX++) {
                    if (matrix[locY+partsX-locX][partsX] == type) {
                        winCombo[0][counter] = locY+partsX-locX;
                        winCombo[1][counter] = partsX;
                        counter++;
                    }
                }
                if (counter == fieldInRowToWin) {
                    return true;
                }
            }
        }

        //check for diagonal row
        for (int locY = rows-1; locY >= fieldInRowToWin-1; locY--) {
            for (int locX = 0; locX < columns-fieldInRowToWin+1; locX++) {
                int counter = 0;
                int offsetY = 0;
                for (int partsX = locX; partsX < fieldInRowToWin+locX; partsX++, offsetY++) {
                    if (matrix[locY-offsetY][partsX] == type) {
                        winCombo[0][counter] = locY-offsetY;
                        winCombo[1][counter] = partsX;
                        counter++;
                    }
                }
                if (counter == fieldInRowToWin) {
                    return true;
                }
            }
        }

        return false;
    }

    void matrixOutput() {

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                System.out.print(matrix[y][x]);
                System.out.print("-");
            }
            System.out.println();
        }
        System.out.println();
    }

}
