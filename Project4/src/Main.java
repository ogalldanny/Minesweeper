//Import Section
import java.util.Random;
import java.util.Scanner;
/*
 * Provided in this class is the neccessary code to get started with your game's implementation
 * You will find a while loop that should take your minefield's gameOver() method as its conditional
 * Then you will prompt the user with input and manipulate the data as before in project 2
 * 
 * Things to Note:
 * 1. Think back to project 1 when we asked our user to give a shape. In this project we will be asking the user to provide a mode. Then create a minefield accordingly
 * 2. You must implement a way to check if we are playing in debug mode or not.
 * 3. When working inside your while loop think about what happens each turn. We get input, user our methods, check their return values. repeat.
 * 4. Once while loop is complete figure out how to determine if the user won or lost. Print appropriate statement.
 */
public class Main{
    public static void main(String[] args) {

    


        //These variables are instantiated inside if-else statements, or they need to be changed often
        //So I defined them early
        boolean mineFound = false;
        Minefield board;
        int numMines;
        boolean gameIsOver = false;
        //Asking the user what difficulty they want to play in
        Scanner input = new Scanner(System.in);
        System.out.println("Would you like to play in easy, medium, or hard mode? ");
        String mode = input.nextLine();

        //Creating the board based on what the user entered
        if(mode.toLowerCase().equals("easy")){
           numMines = 5;
          board = new Minefield(5,5,5);

        }
        else if(mode.toLowerCase().equals("medium")){
             numMines = 12;
             board = new Minefield(9,9,12);

        }

        else{
            numMines = 40;
             board = new Minefield(20,20,40);

        }

        //Asking the user whether they want to play in debug mode or not
        System.out.println("Would you like to play in debug mode? Enter true of false ");
        boolean dMode = input.nextBoolean();
        input.nextLine();
        //If the user said true, it will create the mines and then print out the whole board
        if(dMode){
            Random r = new Random();
            int randomRow = r.nextInt(board.getRows());
            int randomCol = r.nextInt(board.getColumns() - 1);
            board.createMines(randomRow,randomCol, numMines);
            board.evaluateField();
            board.debug();
        }
        //If the user said false, the game will proceed as normal
        else{
            int startRow;
            int startCol;
            //Asking the user the set the starting area
            System.out.println("Where would you like to reveal the starting area from. Enter two numbers with a space ");
            System.out.println(board);
            String startSpot = input.nextLine();
            //This if statement is to account for when the user occasionally enters double digit numbers on hard mode
            if(startSpot.length() == 4 || startSpot.length() == 5){
                if(startSpot.substring(1,2).equals(" ")){
                    startRow = Integer.parseInt(startSpot.substring(0,1));
                    startCol = Integer.parseInt(startSpot.substring(2));
                }
                else{
                    startRow = Integer.parseInt(startSpot.substring(0,2));
                    startCol = Integer.parseInt(startSpot.substring(3));
                }


            }
            else{
                startRow = Integer.parseInt(startSpot.substring(0,1));
                startCol = Integer.parseInt(startSpot.substring(2));

            }


          //These lines set up the board and then print out the starting area for the user
            board.createMines(startRow,startCol, numMines);
            board.evaluateField();
            board.revealStartingArea(startRow,startCol);
            System.out.println(board);


        }




        //Asking the user to make guesses until the game is over
        while( !gameIsOver  ){
            boolean theGuess;
            int guessRow;
            int guessCol;
            String guessSpot;
            //Asking the user to pick where they want to make a guess
            System.out.println("Where would you like to make a guess? Enter two numbers with a space ");
            guessSpot = input.nextLine();
          //This again accounts for when the user has to input one or two double digit numbers
            if(guessSpot.length() == 4 || guessSpot.length() == 5){
                if(guessSpot.substring(1,2).equals(" ")){
                    guessRow = Integer.parseInt(guessSpot.substring(0,1));
                    guessCol = Integer.parseInt(guessSpot.substring(2));
                }
                else{
                    guessRow = Integer.parseInt(guessSpot.substring(0,2));
                    guessCol = Integer.parseInt(guessSpot.substring(3));
                }



            }
            else{
                 guessRow = Integer.parseInt(guessSpot.substring(0,1));
                 guessCol = Integer.parseInt(guessSpot.substring(2));

            }

            //Asking the user if they want to place a flag on their guess spot
            System.out.println("Would you like to place a flag? Enter 1 for yes");
            int flag = input.nextInt();
            input.nextLine();
            //Calling the guess method based on what the user entered
            if(flag == 1){
             theGuess = board.guess(guessRow,guessCol, true);

            }
            else{
              theGuess = board.guess(guessRow,guessCol, false);

            }

            //If theGuess is true, then a mine has been found and the game is over
            //So the loop has to end
            if(theGuess){
                mineFound = true;

                break;
            }


           //If a mine hasn't been found, this will check if the game is over
           //If all non-mine squared have been revealed
            gameIsOver = board.gameOver();

            //This is for printing out the board for the user to make another guess
            if(dMode){
                //If debug mode is on, it will print the full board and the unrevealed board
                board.debug();
                System.out.println(board);

            }
            else{
                System.out.println(board);
            }



        }

        //If a mine was found, it will print out the board and show the user what they did wrong
        if(mineFound){
            System.out.println(board);
            System.out.println("Mine found! Game over! ");
            board.debug();
            System.out.println("Here is the full board ");

        }
        //If mineFound is false and the loop ended, that means the user won, so this statement
        //Prints out a message for them
        else if(gameIsOver){
            System.out.println(board);
            System.out.println("You've flagged all the mines! You win!");
        }

    }



}

