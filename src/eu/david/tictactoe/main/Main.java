package eu.david.tictactoe.main;

public class Main {

    public static void main(String[] args) {

        //new Dialog(0);

        GameFrame.width = 6;
        GameFrame.height = 6;
        GameFrame.rowLength = 3;
        new GameFrame();
    }
}