import java.util.Queue;
import java.util.Random;

public class Minefield {
    /**
    Global Section
    */
    public static final String ANSI_YELLOW_BRIGHT = "\u001B[33;1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001b[47m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001b[45m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";

    /* 
     * Class Variable Section
     * 
    */
    private int rows;
    private int columns;
    private int flags;
    private Cell[][] theBoard;

    /*Things to Note:
     * Please review ALL files given before attempting to write these functions.
     * Understand the Cell.java class to know what object our array contains and what methods you can utilize
     * Understand the StackGen.java class to know what type of stack you will be working with and methods you can utilize
     * Understand the QGen.java class to know what type of queue you will be working with and methods you can utilize
     */
    
    /**
     * Minefield
     * 
     * Build a 2-d Cell array representing your minefield.
     * Constructor
     * @param rows       Number of rows.
     * @param columns    Number of columns.
     * @param flags      Number of flags, should be equal to mines
     */
    public Minefield(int rows, int columns, int flags) {

        this.rows = rows;
        this.columns = columns;
        this.flags = flags;
        theBoard = new Cell[rows][columns];
        for(int i = 0; i < theBoard.length; i++){
            for(int j = 0; j < theBoard[i].length; j++) {
                theBoard[i][j] = new Cell(false, "-");

            }
        }

    }
    public int getRows(){
        return rows;
    }
    public int getColumns(){
        return columns;

    }    /**
     * evaluateField
     * 
     *
     * @function:
     * Evaluate entire array.
     * When a mine is found check the surrounding adjacent tiles. If another mine is found during this check, increment adjacent cells status by 1.
     * 
     */
    public void evaluateField() {
        //The nested for loop is for traversing the whole board
        for(int i = 0; i < theBoard.length; i++){
            for(int j = 0; j < theBoard[0].length;j++){
                int numBombs = 0; //Keeping track of all the adjacent bombs

                //If the current square is a mine it will skip this iteration
                if(theBoard[i][j].getStatus().equals("M")){
                    continue;

                }
                //These if statements check every adjacent square to see if it is a mine
                //If it is, numBombs will increase by 1
                if(i - 1 >= 0 && theBoard[i-1][j].getStatus().equals("M")){
                    numBombs++;


                }
                if(i + 1 < theBoard.length && theBoard[i+1][j].getStatus().equals("M")){
                    numBombs++;

                }
                if(j -1 >= 0 && theBoard[i][j-1].getStatus().equals("M")){
                    numBombs++;

                }
                if(j + 1 < theBoard[i].length && theBoard[i][j+1].getStatus().equals("M")){
                     numBombs++;

                }
                if(j -1 >= 0 && i -1 >= 0 && theBoard[i-1][j-1].getStatus().equals("M")){
                    numBombs++;

                }
                if(j - 1 >= 0 && i + 1 < theBoard.length && theBoard[i + 1][j-1].getStatus().equals("M")){
                    numBombs++;
                }

                if(j + 1 < theBoard[0].length && i -1 >= 0 && theBoard[i-1][j+1].getStatus().equals("M")){
                    numBombs++;

                }
                if(j + 1 < theBoard[i].length && i + 1 < theBoard.length && theBoard[i + 1][j+1].getStatus().equals("M")){
                    numBombs++;

                }
                //The status of the square changes to numBombs
                String newStatus = String.valueOf(numBombs);
                theBoard[i][j].setStatus(newStatus);


            }
        }

    }

    /**
     * createMines
     * 
     * Randomly generate coordinates for possible mine locations.
     * If the coordinate has not already been generated and is not equal to the starting cell set the cell to be a mine.
     * utilize rand.nextInt()
     * 
     * @param x       Start x, avoid placing on this square.
     * @param y        Start y, avoid placing on this square.
     * @param mines      Number of mines to place.
     */
    public void createMines(int x, int y, int mines) {
        //This loop keeps going until there are no more mines to place
        while(mines > 0){

            Random r = new Random();
            //Generating a random spot to put a mine
            int randomRow = r.nextInt(rows - 1);
            int randomCol = r.nextInt(columns - 1);
            //Making sure the spot is not the starting value, and making sure the spot is not already a mine
            if((randomRow == x && randomCol == y) || theBoard[randomRow][randomCol].getStatus().equals("M")){
                continue;
            }
            else{
                //Placing the mine
                theBoard[randomRow][randomCol].setStatus("M");
                mines--;

            }
        }

    }

    /**
     * guess
     * 
     * Check if the guessed cell is inbounds (if not done in the Main class). 
     * Either place a flag on the designated cell if the flag boolean is true or clear it.
     * If the cell has a 0 call the revealZeroes() method or if the cell has a mine end the game.
     * At the end reveal the cell to the user.
     * 
     * 
     * @param x       The x value the user entered.
     * @param y       The y value the user entered.
     * @param flag    A boolean value that allows the user to place a flag on the corresponding square.
     * @return boolean Return false if guess did not hit mine or if flag was placed, true if mine found.
     */
    public boolean guess(int x, int y, boolean flag) {
        //All of these if-else statments check for a possible scenario when guessing
        if(x < 0 || x > theBoard.length || y < 0 || y >= theBoard[0].length){
            System.out.println("That is not a valid guess ");
            return false;

        }
       else if(theBoard[x][y].getRevealed()){
            System.out.println("That square has already been revealed ");
           return false;
        }
        else if(flag && flags <= 0){
            System.out.println("You do not have any more flags. ");
            return false;

        }
        //Placing a flag on a square
       else if(flag && flags > 0){
            theBoard[x][y].setStatus("F");
            theBoard[x][y].setRevealed(true);
            flags--;
            return false;

        }
      //Calling revealZeroes if the user finds a zero
       else if (theBoard[x][y].getStatus().equals(ANSI_YELLOW + "0" + ANSI_GREY_BACKGROUND)) {
            revealZeroes(x, y);
            theBoard[x][y].setRevealed(true);
            return false;
        }
       //Only return true if the guess is on a mine and a flag was not placed on it
       else if (theBoard[x][y].getStatus().equals(ANSI_RED_BRIGHT + "M" + ANSI_GREY_BACKGROUND)) {
            theBoard[x][y].setRevealed(true);
            return true;
        }
        

        theBoard[x][y].setRevealed(true);
        return false;
    }

    /**
     * gameOver
     * 
     * Ways a game of Minesweeper ends:
     * 1. player guesses a cell with a mine: game over -> player loses
     * 2. player has revealed the last cell without revealing any mines -> player wins
     * 
     * @return boolean Return false if game is not over and squares have yet to be revealed, otheriwse return true.
     */
    public boolean gameOver() {
        //If the for loop finds a non-mine square that has not been revealed it will return false
        for(int i = 0; i < theBoard.length; i++){
            for(int j = 0; j < theBoard[i].length; j++){
                if(!theBoard[i][j].getRevealed() && !theBoard[i][j].getStatus().equals("M") ){
                           return false;
                }

            }
        }
        return true;

    }

    /**
     * Reveal the cells that contain zeroes that surround the inputted cell.
     * Continue revealing 0-cells in every direction until no more 0-cells are found in any direction.
     * Utilize a STACK to accomplish this.
     *
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a stack be useful here rather than a queue?
     *
     * @param x      The x value the user entered.
     * @param y      The y value the user entered.
     */
    public void revealZeroes(int x, int y) {
        Stack1Gen<int[]> stack = new Stack1Gen<int[]>();
        int[] startIndex = {x,y};
        stack.push(startIndex);

        while(!stack.isEmpty()){
            //Taking the coordinate off the stack and revealing that square
            int[] newIndex = stack.pop();
            int row = newIndex[0];
            int col = newIndex[1];
            theBoard[row][col].setRevealed(true);

           //These 4 if statements check if each neighbor of the current square
           //Is in bounds, is unrevealed, and also equals 0
            if(col - 1 >=0 && !theBoard[row][col - 1].getRevealed() && theBoard[row][col -1 ].getStatus().equals(ANSI_YELLOW + "0" + ANSI_GREY_BACKGROUND)){
                int[] leftIndex = {row,col-1};
                stack.push(leftIndex);
            }

            if(col + 1 < theBoard[row].length && !theBoard[row][col + 1].getRevealed()&& theBoard[row][col + 1].getStatus().equals(ANSI_YELLOW + "0" + ANSI_GREY_BACKGROUND)){
                int[] rightIndex = {row, col + 1};
                stack.push(rightIndex);

            }
            if(row + 1 < theBoard.length && !theBoard[row + 1][col].getRevealed() && theBoard[row + 1][col].getStatus().equals(ANSI_YELLOW + "0" + ANSI_GREY_BACKGROUND)){
                int[] belowIndex = {row + 1, col};
                stack.push(belowIndex);


            }
            if(row - 1 >= 0 && !theBoard[row - 1][col].getRevealed() && theBoard[row - 1][col].getStatus().equals(ANSI_YELLOW + "0" + ANSI_GREY_BACKGROUND)){
                int[] aboveIndex = {row - 1, col};
                stack.push(aboveIndex);

            }



        }




    }

    /**
     * revealStartingArea
     *
     * On the starting move only reveal the neighboring cells of the inital cell and continue revealing the surrounding concealed cells until a mine is found.
     * Utilize a QUEUE to accomplish this.
     * 
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a queue be useful for this function?
     *
     * @param x     The x value the user entered.
     * @param y     The y value the user entered.
     */
    public void revealStartingArea(int x, int y) {
        if(x < 0 || x >= theBoard.length || y < 0 || y >= theBoard.length){
            System.out.println("That is not a valid square ");
            return;
        }
        Q1Gen<int[]> queue = new Q1Gen<int[]>();
        int[] startIndex = {x,y};
        queue.add(startIndex);

        while(queue.length() > 0){
            //Taking the top coordinate off the queue and revealing that square
            int[] newIndex = queue.remove();
           int row = newIndex[0];
           int col = newIndex[1];



           theBoard[row][col].setRevealed(true);
           //If the square is a mine the loop will end
            if(theBoard[row][col].getStatus().equals("M")){
                break;

            }

            //These 4 if statements check each neighbor of the current square
            //If it is in bounds and hasn't been revealed, it is added to the queue
            if(col - 1 >=0 && !theBoard[row][col - 1].getRevealed()){
                int[] leftIndex = {row,col-1};
                queue.add(leftIndex);
            }

            if(col + 1 < theBoard[row].length && !theBoard[row][col + 1].getRevealed()){
                int[] rightIndex = {row, col + 1};
                queue.add(rightIndex);

            }
            if(row + 1 < theBoard.length && !theBoard[row + 1][col].getRevealed()){
                int[] belowIndex = {row + 1, col};
                queue.add(belowIndex);


            }
            if(row - 1 >= 0 && !theBoard[row - 1][col].getRevealed()){
                int[] aboveIndex = {row - 1, col};
                queue.add(aboveIndex);

            }

        }


    }

    /**
     * For both printing methods utilize the ANSI colour codes provided! 
     * 
     * 
     * 
     * 
     * 
     * debug
     *
     * @function This method should print the entire minefield, regardless if the user has guessed a square.
     * *This method should print out when debug mode has been selected. 
     */
    public void debug() {
        createColors();
        System.out.print("  ");
        for(int i = 0; i < theBoard[0].length; i++){
            if(i > 9){
                System.out.print(i);
            }
            else{
                System.out.print(i + " ");

            }

        }
        System.out.println();

        for(int i = 0; i < theBoard.length; i++){
            if(i > 9){
                System.out.print(i);
            }
            else{
                System.out.print(i + " ");

            }
            for(int j = 0; j < theBoard[i].length; j++){
                System.out.print(theBoard[i][j].getStatus() + " ");

            }
            System.out.println();

        }

    }

    //I created this helper method to make the debug and toString methods cleaner
    //The nested for loop will check every square on the board
    //Based on its status, it will be changed to a different color
     public void createColors(){
        for(int i = 0; i < theBoard.length; i++){
            for(int j = 0; j < theBoard[i].length; j++){

                if(theBoard[i][j].getStatus().equals("M")){
                    theBoard[i][j].setStatus(ANSI_RED_BRIGHT +  theBoard[i][j].getStatus() + ANSI_GREY_BACKGROUND  );
                }
                else if(theBoard[i][j].getStatus().equals("0")){
                    theBoard[i][j].setStatus(ANSI_YELLOW + theBoard[i][j].getStatus() + ANSI_GREY_BACKGROUND  );

                }
                else if(theBoard[i][j].getStatus().equals("1")){
                    theBoard[i][j].setStatus(ANSI_YELLOW_BRIGHT +  theBoard[i][j].getStatus() + ANSI_GREY_BACKGROUND  );

                }
                else if(theBoard[i][j].getStatus().equals("2")){
                    theBoard[i][j].setStatus(ANSI_BLUE_BRIGHT +  theBoard[i][j].getStatus() + ANSI_GREY_BACKGROUND  );

                }
                else if(theBoard[i][j].getStatus().equals("3")){
                    theBoard[i][j].setStatus(ANSI_BLUE +  theBoard[i][j].getStatus() + ANSI_GREY_BACKGROUND  );

                }
                else if(theBoard[i][j].getStatus().equals("4")){
                    theBoard[i][j].setStatus(ANSI_RED +  theBoard[i][j].getStatus() + ANSI_GREY_BACKGROUND  );

                }
                else if(theBoard[i][j].getStatus().equals("5")){
                    theBoard[i][j].setStatus(ANSI_GREEN + theBoard[i][j].getStatus() + ANSI_GREY_BACKGROUND  );

                }
                else if(theBoard[i][j].getStatus().equals("6")){
                    theBoard[i][j].setStatus(ANSI_PURPLE + theBoard[i][j].getStatus() + ANSI_GREY_BACKGROUND  );

                }
                else if(theBoard[i][j].getStatus().equals("7")){
                    theBoard[i][j].setStatus(ANSI_CYAN + theBoard[i][j].getStatus() + ANSI_GREY_BACKGROUND  );

                }

            }

        }

    }
    /**
     * toString
     *
     * @return String The string that is returned only has the squares that has been revealed to the user or that the user has guessed.
     */
    public String toString() {
        createColors();
        String total = "";
        total += "  ";
        for(int i = 0; i < theBoard[0].length; i++){
            if(i > 9){
                total += i;
            }
            else{
                total += i + " ";

            }

        }
        total += "\n";

        for(int i = 0; i < theBoard.length; i++){
            if(i > 9){
                total += i;
            }
            else{
                total += i + " ";

            }
            for(int j = 0; j < theBoard[i].length; j++){
                if(!theBoard[i][j].getRevealed()){
                   total += "-" + " ";
                }
                else{
                    total += theBoard[i][j].getStatus() + " ";

                }


            }
           total += "\n";

        }

     return total;
    }
}
