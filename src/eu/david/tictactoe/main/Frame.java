package eu.david.tictactoe.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Frame extends JFrame {

    public static int width;
    public static int height;
    public static int rowLength;
    private int allFields = width*height;

    private JPanel panel;
    private JButton[][] buttons = new JButton[height][width];
    private byte matrix[][] = new byte[height][width];

    private int gameCounter = 0;
    private ImageIcon kreutzIcon = new ImageIcon("D:\\Bibliotheken\\Bilder\\Zeug\\KreutzS.png");
    private ImageIcon kreisIcon = new ImageIcon("D:\\Bibliotheken\\Bilder\\Zeug\\KreisS.png");

    public Frame() {

        super("TicTacToe");

        /*System.out.println(halfOfButtons);
        System.out.println(winCombos.length);
        System.out.println(winCombos[0].length);*/

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }

        panel = new JPanel();
        panel.setLayout(new GridLayout(height, width));

        for (int y = 0 ; y < height; y++) {
            for (int x = 0 ; x < width; x++) {
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
        setAlwaysOnTop(true);
        setVisible(true);
    }

    private void playerActions(ActionEvent event) {

        String temp = event.toString();
        int length = temp.length();
        int x = Integer.parseInt(temp.substring(length-2, length));
        int y = Integer.parseInt(temp.substring(length-5, length-3));
        if (matrix[y][x] != 0) {
            return;
        }
        matrix[y][x] = 1;
        buttons[y][x].setIcon(kreutzIcon);
        botActions();
        matrixOutput();
        if (isWinner(1)) {
            System.out.println("you won!");
        }
        if (isGameFinish()) {
            System.out.println("aha");
        }
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
            y = generator.nextInt(height);
            x = generator.nextInt(width);
        } while (matrix[y][x] != 0);

        return new int[] {y, x};
    }

    private boolean isGameFinish() {

        gameCounter++;

        System.out.println("count: "+gameCounter*2);

        if (gameCounter*2 >= allFields-1) {
            gameCounter = 0;
            System.out.println("finished!");
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    matrix[y][x] = 0;
                    buttons[y][x].setIcon(null);
                }
            }
            return true;
        }

        return false;
    }

    private boolean isWinner(int type) {

        for (int locY = 0; locY < height; locY++) {
            for (int locX = 0; locX < width-rowLength+1; locX++) {
                int counter = 0;
                for (int partsX = locX; partsX < rowLength+locX; partsX++) {
                    if (matrix[locY][partsX] == type) {
                        counter++;
                    }
                }
                if (counter == rowLength) {
                    return true;
                }
            }
        }

        for (int locX = 0; locX < width; locX++) {
            for (int locY = 0; locY < height-rowLength+1; locY++) {
                int counter = 0;
                for (int partsY = locY; partsY < rowLength+locY; partsY++) {
                    if (matrix[partsY][locX] == type) {
                        counter++;
                    }
                }
                if (counter == rowLength) {
                    return true;
                }
            }
        }

        for (int locY = 0; locY < height; locY++) {
            for (int locX = 0; locX < width-rowLength+1; locX++) {
                int counter = 0;
                for (int partsX = locX; partsX < rowLength+locX; partsX++) {
                    if (matrix[locY+locX][partsX] == type) {
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

    private void matrixOutput() {

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(matrix[y][x]+"-");
            }
            System.out.println();
        }
        System.out.println();
    }

    /*boolean isWinner(int type) {

        String currentFields = getIntArrayString(type);
        System.out.println("currentFields: "+currentFields);
        for (int winCombo = 0; winCombo < winCombos.length; winCombo++) {
            int correctParts = 0;
            System.out.print("if contains: ");
            for (int parts = 0; parts < mode; parts++) {
                System.out.print(winCombos[winCombo][parts]);
                if (currentFields.contains(Integer.toString(winCombos[winCombo][parts]))) {
                    correctParts++;
                    System.out.print("(true), ");
                    if (correctParts == 3) {
                        return true;
                    }
                }
                else {
                    System.out.print("(false), ");
                }
            }
            System.out.println();
            System.out.println("next poss");
        }

        return false;
    }

    String getIntArrayString(int type) {

        String result = "";
        for (int i = 0; i < gameCounter+1; i++) {
            //result += Integer.toString(fields[index][i]);
            result += (char)(fields[type][i]+48);
        }

        //System.out.println("result: "+result);

        return result;
    }*/

}
