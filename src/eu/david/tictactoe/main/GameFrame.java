package eu.david.tictactoe.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Integer.parseInt;

public class GameFrame extends JFrame {

    protected static int sizeProportion;
    protected static int columns;
    protected static int rows;
    protected static int fieldInRowToWin;
    private int allFields = columns*rows;

    private JPanel panel;
    private JButton[][] buttons = new JButton[rows][columns];
    private byte matrix[][] = new byte[rows][columns];
    int[][] winCombo = new int[2][fieldInRowToWin];

    private int gameCounter = 0;
    private ImageIcon kreutzIcon = new ImageIcon("D:\\Bibliotheken\\Bilder\\Zeug\\KreutzS.png");
    private ImageIcon kreisIcon = new ImageIcon("D:\\Bibliotheken\\Bilder\\Zeug\\KreisS.png");

    public GameFrame() {

        super("TicTacToe");

        //System.out.println(matrix.length);
        //System.out.println(matrix[0].length);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }

        panel = new JPanel();
        panel.setLayout(new GridLayout(rows, columns));

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
                buttons[y][x].addActionListener(event -> playerActions(event));
            }
        }

        add(panel);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void playerActions(ActionEvent event) {

        String temp = event.toString();
        int length = temp.length();
        int x = parseInt(temp.substring(length-2, length));
        int y = parseInt(temp.substring(length-5, length-3));
        if (matrix[y][x] != 0) {
            return;
        }
        matrix[y][x] = 1;
        buttons[y][x].setIcon(scaleImageIcon(kreutzIcon, sizeProportion));
        botActions();
        matrixOutput();
        if (isWinner(1)) {
            System.out.println("you won!");
            new Dialog(3);
        }
        if (isWinner(2)) {
            System.out.println("bot won!");
            new Dialog(1);
        }
        if (isfieldFull()) {
            System.out.println("vooll");
            new Dialog(2);
        }
    }

    ImageIcon scaleImageIcon(ImageIcon icon, int percent) {

        Image temp = icon.getImage();
        temp = temp.getScaledInstance(icon.getIconWidth()*percent, icon.getIconHeight()*percent, Image.SCALE_SMOOTH);
        return new ImageIcon(temp);
    }

    private void botActions() {

        int temp[] = getBotButton();
        int y = temp[0];
        int x = temp[1];

        matrix[y][x] = 2;
        buttons[y][x].setIcon(kreisIcon);
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

        gameCounter++;

        System.out.println("count: "+gameCounter*2);

        if (gameCounter*2 >= allFields-1) {
            gameCounter = 0;
            System.out.println("finished!");
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < columns; x++) {
                    matrix[y][x] = 0;
                    buttons[y][x].setIcon(null);
                }
            }
            return true;
        }

        return false;
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

    private void matrixOutput() {

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                System.out.print(matrix[y][x]+"-");
            }
            System.out.println();
        }
        System.out.println();
    }

}
