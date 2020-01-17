import java.util.Scanner;
import java.lang.String;

public class Minesweeper {
    private String [][] displayBoard;
    private boolean [][] mineBoard;
    private boolean [][] chosenBoard;
    private int totalMines;
    private boolean isBust;
    public Minesweeper(int m,  int n) {
        this.displayBoard = new String[m][n];
        this.totalMines = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                this.displayBoard[i][j] = "[-]";
            }
        }
        this.mineBoard = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (Math.random() < 0.15) {
                    this.mineBoard[i][j] = true;
                    this.totalMines++;
                }
            }
        }
        this.chosenBoard = new boolean[m][n];
        this.isBust = false;
    }
    private boolean getBust() {
        return this.isBust;
    }

    private int getMines() {
        return this.totalMines;
    }

    private void displayCurrentBoard() {
        for (int i = -1; i < this.displayBoard.length; i++) {
            StringBuilder strB = new StringBuilder();
            if (i < 0) strB.append("[ ]");
            else strB.append("[" + i + "] ");
            for (int j = 0; j < this.displayBoard[0].length; j++) {
                if (i < 0) strB.append(" [" + j + "]");
                else strB.append(this.displayBoard[i][j] + " ");
            }
            strB.append("\n");
            System.out.println(strB.toString());
        }
    }

    private void displayFinalBoard() {
        for (int i = 0; i < this.displayBoard.length; i++) {
            for (int j = 0; j < this.displayBoard[0].length; j++) {
                if (this.mineBoard[i][j]) this.displayBoard[i][j] = "[*]";
            }
        }
        this.displayCurrentBoard();
    }

    private void pickSpot(int i, int j) {
        this.updateDisplayBoard(i, j);
        this.updateChosenBoard(i, j);
    }

    private void updateDisplayBoard(int i, int j) {
        if (this.mineBoard[i][j]) {
            this.displayBoard[i][j] = "[*]";
            this.isBust = true;
            return;
        }
        int sum = 0;
        if (i > 0) if (this.mineBoard[i - 1][j]) sum++;
        if (i < this.mineBoard.length - 1) if (this.mineBoard[i + 1][j]) sum++;
        if (j > 0) if (this.mineBoard[i][j - 1]) sum++;
        if (j < this.mineBoard[0].length - 1) if (this.mineBoard[i][j + 1]) sum++;
        if (sum > 0) this.displayBoard[i][j] = "["+sum+"]";
        else this.displayBoard[i][j] = "[ ]";
    }
    private void updateChosenBoard(int i, int j) {
        this.chosenBoard[i][j] = true;
    }

    private static void errorAndClose(String message, Scanner scanner) {
        System.out.println("Something went wrong! Please try again!\n");
        scanner.close();
    }

    public static void main(String[]args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: \n");
        String name = scanner.nextLine();
        System.out.printf("Hi, %s! Remember: don't pick spots with bombs!\n", name);
        System.out.println("How many rows should the Minesweeper board have?");
        int rows = 0, cols = 0;
        try {
            rows = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            errorAndClose("Something went wrong! Please try again!\n", scanner);
            return;
        }
        System.out.print("Great! How many columns should the board have?\n");
        try {
            cols = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Something went wrong! Please try again!\n");
            scanner.close();
            return;
        }
        if (rows < 2 || cols < 2) {
            errorAndClose("Board not big enough! Please try again!\n", scanner);
            return;
        }
        if (rows > 100 || cols > 100) {
            errorAndClose("Board too big! Please try again!\n", scanner);
            return;
        }
        Minesweeper currentGame = new Minesweeper(rows, cols);
        int spotsChosen = 0;
        while (currentGame.getBust() != true) {
            System.out.printf("Here's the board! There are %d mines on the board!\n", currentGame.getMines());
            currentGame.displayCurrentBoard();
            System.out.print("Pick a space: (for example, type 'row column' as '0 0' for the top left spot of the board)\n");
            try {
                spotsChosen++;
                String[] spots = scanner.nextLine().split(" ");
                currentGame.pickSpot(Integer.parseInt(spots[0]), Integer.parseInt(spots[1]));
                System.out.println("***************************************************************\n");
                System.out.println(currentGame.getBust() ? "You hit a mine!\n" : "You didn't hit a mine!\n");
            } catch (Exception e) {
                System.out.print("Error: please try again!\n");
                continue;
            }
            if (spotsChosen + currentGame.getMines() >= rows * cols) {
                System.out.println("You won the game!\n");
                break;     
            }
        }
        System.out.println("Thanks for playing! Here is the final board:\n");
        currentGame.displayFinalBoard();
        scanner.close();
    }
}