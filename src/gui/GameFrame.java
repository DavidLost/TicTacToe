package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Integer.parseInt;
import static javax.swing.JOptionPane.showMessageDialog;

public class GameFrame extends JFrame {

    private boolean finished = false;
    private int finishState = -1;

    private int sizeProportion = 100;
    private int columns;
    private int rows;
    private int fieldsInRowToWin;
    private int allFields;
    private boolean gravity;
    private int mode;
    private boolean playerTurn = false;
    private boolean iconError = false;

    private JButton[][] buttons;
    private byte[][] matrix;
    private int[] winComboY;
    private int[] winComboX;

    private int gameCounter = 0;
    private ImageIcon kreutzIcon;
    private ImageIcon kreisIcon;
    private ImageIcon kreutzIconWinner;
    private ImageIcon kreisIconWinner;


    public GameFrame(int columns, int rows, int fieldsInRowToWin, boolean gravity, int mode) {

        super("TicTacToe");

        this.columns = columns;
        this.rows = rows;
        this.fieldsInRowToWin = fieldsInRowToWin;
        this.allFields = columns*rows;
        this.gravity = gravity;
        this.mode = mode;

        matrix = new byte[rows][columns];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                matrix[y][x] = 0;
            }
        }

        winComboY = new int[fieldsInRowToWin];
        winComboX = new int[fieldsInRowToWin];

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
                //System.out.println("name: "+name);
                panel.add(buttons[y][x]);
                buttons[y][x].addActionListener(event -> actions(event));
            }
        }

        if (!loadIcons()) {finished = true;}

        add(panel);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        getNewSizeProportion();

        getContentPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {

                getNewSizeProportion();
                updateButtonIcons();
            }
        });
    }

    private boolean loadIcons() {

        String kreutzIconPath = "res\\icons\\test.png";
        String kreisIconPath = "res\\icons\\kreis.png";
        String kreutzIconWinnerPath = "res\\icons\\kreutz_winner.png";
        String kreisIconWinnerPath = "res\\icons\\kreis_winner.png";

        kreutzIcon = getImageIcon(kreutzIconPath);
        kreisIcon = getImageIcon(kreisIconPath);
        kreutzIconWinner = getImageIcon(kreutzIconWinnerPath);
        kreisIconWinner = getImageIcon(kreisIconWinnerPath);

        if (iconError) {
            return false;
        }

        return true;
    }

    private ImageIcon getImageIcon(String path) {

        ImageIcon icon = new ImageIcon(path);
        if (icon.getIconHeight() == -1) {
            showMessageDialog(this, "icons couldn't be found!", "Error", JOptionPane.ERROR_MESSAGE);
            iconError = true;
            return null;
        }
        int maxPixels = 512;
        if (icon.getIconHeight() > maxPixels || icon.getIconWidth() > maxPixels) {
            showMessageDialog(this, "icons are not valid", "Error", JOptionPane.ERROR_MESSAGE);
            iconError = true;
            return null;
        }
        return icon;
    }

    private void getNewSizeProportion() {

        double boxHeight = getHeight()/rows;
        double boxWidth = getWidth()/columns;
        double length = boxHeight < boxWidth ? boxHeight : boxWidth;

        sizeProportion = (int)(length/kreisIcon.getIconHeight()*100);
    }

    private void updateButtonIcons() {

        ImageIcon scaledKreutzIcon = scaleImageIcon(kreutzIcon, sizeProportion);
        ImageIcon scaledKreisIcon = scaleImageIcon(kreisIcon, sizeProportion);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                switch (matrix[y][x]) {
                    case 1 : buttons[y][x].setIcon(scaledKreutzIcon);
                        break;
                    case 2 : buttons[y][x].setIcon(scaledKreisIcon);
                }
            }
        }
    }

    ImageIcon scaleImageIcon(ImageIcon icon, int percent) {

        int width = icon.getIconWidth();
        int height = icon.getIconHeight();
        double newWidth = width*((double)256/width)*percent/100;
        double newHeight = height*((double)256/height)*percent/100;
        Image temp = icon.getImage();
        temp = temp.getScaledInstance((int)newWidth, (int)newHeight, Image.SCALE_SMOOTH);
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
            if (checkForFinish() && finishState != 0) {return;}
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

    private void botActions() {

        for (int i = 1; i <= 2; i++) {
            boolean found = false;
            if (isHorizontalRow(fieldsInRowToWin-1, i)) {
                int nextY1 = winComboY[0];
                found = true;
            }
            if (isVerticalRow(fieldsInRowToWin-1, i)) {
                found = true;
            }
            if (isDiagonalRow1(fieldsInRowToWin-1, i)) {
                found = true;
            }
            if (isDiagonalRow2(fieldsInRowToWin-1, i)) {
                found = true;
            }
            if (found) {
                for (int x = 0; x < fieldsInRowToWin-1; x++) {
                    System.out.print(winComboY[x]+"/");
                    System.out.println(winComboX[x]);
                }
                System.out.println();
            }

        }

        int temp[] = getBotButton();
        int y = temp[0];
        int x = temp[1];
        if (gravity) {
            y = getGravityY(y, x);
        }
        matrix[y][x] = 2;
        buttons[y][x].setIcon(scaleImageIcon(kreisIcon, sizeProportion));
    }

    private boolean checkForFinish() {

        if (isWinner(fieldsInRowToWin, 1)) {
            finished = true;
            finishState = 2;
        }
        else if (isWinner(fieldsInRowToWin, 2)) {
            finished = true;
            finishState = 1;
        }
        else if (isfieldFull()) {
            finished = true;
            finishState = 0;
        }
        if (finished && finishState != 0) {
            for (int i = 0; i < fieldsInRowToWin; i++) {
                if (finishState == 2) {
                    buttons[winComboY[i]][winComboX[i]].setIcon(scaleImageIcon(kreutzIconWinner, sizeProportion));
                }
                else {
                    buttons[winComboY[i]][winComboX[i]].setIcon(scaleImageIcon(kreisIconWinner, sizeProportion));
                }
            }
        }

        return finished;
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

    private boolean isWinner(int rowLength, int type) {

        if (isHorizontalRow(rowLength, type) ||
            isVerticalRow(rowLength, type) ||
            isDiagonalRow1(rowLength, type) ||
            isDiagonalRow2(rowLength, type))
        {
            return true;
        }
        return false;
    }

    private boolean isHorizontalRow(int rowLength, int type) {

        for (int locY = 0; locY < rows; locY++) {
            for (int locX = 0; locX < columns-rowLength+1; locX++) {
                int counter = 0;
                for (int partsX = locX; partsX < rowLength+locX; partsX++) {
                    if (matrix[locY][partsX] == type) {
                        winComboY[counter] = locY;
                        winComboX[counter] = partsX;
                        counter++;
                    }
                }
                if (counter == rowLength) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isVerticalRow(int rowLength, int type) {

        for (int locX = 0; locX < columns; locX++) {
            for (int locY = 0; locY < rows-rowLength+1; locY++) {
                int counter = 0;
                for (int partsY = locY; partsY < rowLength+locY; partsY++) {
                    if (matrix[partsY][locX] == type) {
                        winComboY[counter] = partsY;
                        winComboX[counter] = locX;
                        counter++;
                    }
                }
                if (counter == rowLength) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isDiagonalRow1(int rowLength, int type) {

        for (int locY = 0; locY < rows-rowLength+1; locY++) {
            for (int locX = 0; locX < columns-rowLength+1; locX++) {
                int counter = 0;
                for (int partsX = locX; partsX < rowLength+locX; partsX++) {
                    if (matrix[locY+partsX-locX][partsX] == type) {
                        winComboY[counter] = locY+partsX-locX;
                        winComboX[counter] = partsX;
                        counter++;
                    }
                }
                if (counter == rowLength) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isDiagonalRow2(int rowLength, int type) {

        for (int locY = rows-1; locY >= rowLength-1; locY--) {
            for (int locX = 0; locX < columns-rowLength+1; locX++) {
                int counter = 0;
                int offsetY = 0;
                for (int partsX = locX; partsX < rowLength+locX; partsX++, offsetY++) {
                    if (matrix[locY-offsetY][partsX] == type) {
                        winComboY[counter] = locY-offsetY;
                        winComboX[counter] = partsX;
                        counter++;
                    }
                }
                if (counter == rowLength) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getFinishState() {
        return finishState;
    }

    /*private void matrixOutput() {

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                System.out.print(matrix[y][x]);
                System.out.print("-");
            }
            System.out.println();
        }
        System.out.println();
    }*/

}
