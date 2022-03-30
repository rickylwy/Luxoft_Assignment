import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Connect4 {

    //Production quality code: extensibility through configurations / constants
    //This game could be extended by adding more players and enlarging the grid size, or even requiring more connection
    //Adding players with the names starting at the same character requires a change request

    //Production Quality code: proper use of constants instead of hardcode
    public static final List<String> playerList = Arrays.asList("RED", "GREEN");
    public static final int gridRows = 6;
    public static final int gridColumns = 7;
    public static final int requiredConnection = 4;
    public static final char emptyChar = ' ';


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        char[][] grid = new char[gridRows][gridColumns];

        //initialize array
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                grid[row][col] = emptyChar;
            }
        }

        int turn = 1;
        int currentPlayerIndex = playerList.size() - 1;
        String player = playerList.get(currentPlayerIndex);
        boolean isWinningStep = false;
        System.out.println("````text");

        int currentStepRow = 0;
        int currentStepColumn = 0;
        int maxRound = gridColumns * gridRows;

        //play a turn
        while (!isWinningStep && turn <= maxRound) {
            boolean validPlay;
            int playerColumn;

            //switch players
            if (currentPlayerIndex + 1 >= playerList.size()) {
                currentPlayerIndex = 0;
            } else {
                currentPlayerIndex++;
            }
            player = playerList.get(currentPlayerIndex);

            do {
                display(grid);

                System.out.print("Player " + (currentPlayerIndex + 1) + " [" + player + "] - choose column (1-" + gridColumns + "): ");
                playerColumn = in.nextInt() - 1;

                //validate play
                validPlay = validate(playerColumn, grid);

            } while (!validPlay);

            //drop the disc
            for (int row = grid.length - 1; row >= 0; row--) {
                if (grid[row][playerColumn] == emptyChar) {
                    grid[row][playerColumn] = player.charAt(0);
                    currentStepRow = row;
                    currentStepColumn = playerColumn;
                    break;
                }
            }

            //determine if the step produces a win
            isWinningStep = isWinningStep(grid, currentStepRow, currentStepColumn);

            turn++;
        }
        display(grid);

        if (isWinningStep) {
            System.out.println("Player " + (currentPlayerIndex + 1) + " [" + player + "] wins!");
        } else {
            System.out.println("Tie game");
        }
        System.out.println("````");
    }

    public static void display(char[][] grid) {
        for (char[] chars : grid) {
            System.out.print("|");
            for (int col = 0; col < grid[0].length; col++) {
                System.out.print(chars[col]);
                System.out.print("|");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static boolean validate(int column, char[][] grid) {
        //valid column?
        if (column < 0 || column >= grid[0].length) {
            return false;
        }

        //full column?
        return grid[0][column] == emptyChar;
    }

    //Performance optimization: scan the affected lines instead of full board scan
    //Generic logic, extensible to any number of connection instead of only 4
    public static boolean isWinningStep(char[][] grid, int currentStepRow, int currentStepColumn) {
        int currentConnection;
        char previousDisc;
        char currentDisc;


        //horizontal check
        previousDisc = grid[currentStepRow][0];
        currentConnection = previousDisc == emptyChar ? 0 : 1;
        for (int col = 1; col < grid[0].length; col++) {
            currentDisc = grid[currentStepRow][col];

            if (currentDisc == previousDisc && currentDisc != emptyChar) {
                currentConnection++;
            } else if (currentDisc != emptyChar) {
                currentConnection = 1;
            } else {
                currentConnection = 0;
            }

            if (currentConnection >= requiredConnection) return true;
            previousDisc = currentDisc;
        }

        //vertical check
        previousDisc = grid[0][currentStepColumn];
        currentConnection = previousDisc == emptyChar ? 0 : 1;
        for (int row = 1; row < grid.length; row++) {
            currentDisc = grid[row][currentStepColumn];

            if (currentDisc == previousDisc && currentDisc != emptyChar) {
                currentConnection++;
            } else if (currentDisc != emptyChar) {
                currentConnection = 1;
            } else {
                currentConnection = 0;
            }

            if (currentConnection >= requiredConnection) return true;
            previousDisc = currentDisc;
        }

        // "/" diagonal check
        int startingRow = Math.min(currentStepRow + currentStepColumn, grid.length - 1);
        int startingColumn = currentStepColumn - (startingRow - currentStepRow);
        previousDisc = grid[startingRow][startingColumn];
        currentConnection = previousDisc == emptyChar ? 0 : 1;

        int counter = 1;
        while (startingRow - counter >= 0 && startingColumn + counter <= grid[0].length - 1) {
            currentDisc = grid[startingRow - counter][startingColumn + counter];

            if (currentDisc == previousDisc && currentDisc != emptyChar) {
                currentConnection++;
            } else if (currentDisc != emptyChar) {
                currentConnection = 1;
            } else {
                currentConnection = 0;
            }

            if (currentConnection >= requiredConnection) return true;
            previousDisc = currentDisc;
            counter++;
        }

        // "\" diagonal check
        startingRow = Math.max(currentStepRow - currentStepColumn, 0);
        startingColumn = currentStepColumn - (currentStepRow - startingRow);
        previousDisc = grid[startingRow][startingColumn];
        currentConnection = previousDisc == emptyChar ? 0 : 1;

        counter = 1;
        while (startingRow + counter <= grid.length - 1 && startingColumn + counter <= grid[0].length - 1) {
            currentDisc = grid[startingRow + counter][startingColumn + counter];

            if (currentDisc == previousDisc && currentDisc != emptyChar) {
                currentConnection++;
            } else if (currentDisc != emptyChar) {
                currentConnection = 1;
            } else {
                currentConnection = 0;
            }

            if (currentConnection >= requiredConnection) return true;
            previousDisc = currentDisc;
            counter++;
        }

        return false;
    }
}
