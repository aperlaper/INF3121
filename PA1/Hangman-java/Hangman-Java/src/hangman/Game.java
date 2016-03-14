package hangman;

import java.util.Random;
import java.util.Scanner;

public class Game {
	//collection of words that can be chosen as the word to be guessed 
	private static final String[] wordForGuesing = { "computer", "programmer",
			"software", "debugger", "compiler", "developer", "algorithm",
			"array", "method", "variable" };

	//the word to be guessed
	private String guesWord;
	//buffer to hold the found letters of the word to be guessed
	private StringBuffer dashedWord;
	private FileReadWriter filerw;
  
	public Game(boolean autoStart) {
		//getting the word to be guessed
		guesWord = getRandWord();
		//initiates a buffer to hold the found letters
		dashedWord = getW(guesWord);
		filerw = new FileReadWriter();
		
		if(autoStart) {
			//starts the game
			displayMenu();
		}
	}

	//chooses a random word from the collection of words
	private String getRandWord() {
		Random rand = new Random();
		String randWord = wordForGuesing[rand.nextInt(9)];
		return randWord;
	}
	
	//displays the menu, starting the game
	public void displayMenu() {
		System.out.println("Welcome to �Hangman� game. Please, try to guess my secret word.\n"
						+ "Use 'TOP' to view the top scoreboard, 'RESTART' to start a new game,"
						+ "'HELP' to cheat and 'EXIT' to quit the game.");

		findLetterAndPrintIt();
	}
	
	/*
	 * takes a letter or command
	 * if command: executes the command
	 * if letter: checks if the letter is in the word to be guessed or not and acts accordingly
	 */
	private void findLetterAndPrintIt() {
		//true = help has been used, false = help has not been used
		boolean isHelpUsed = false;
		//storage for the letter or command given by the player
		String letter = "";
		
		int mistakes = 0;
		
		//main game loop
		while (!dashedWord.toString().equals(guesWord)) {
			//shows the progress of the player 
			System.out.println("The secret word is: " + printDashes(dashedWord));
			System.out.println("DEBUG " + guesWord);
			// do-while loop will run as long as the player does not provide appropriate input 
			do {
				System.out.println("Enter your gues(1 letter alowed): ");
				Scanner input = new Scanner(System.in);
				//takes input form the player
				letter = input.next();
				
				//checks if the player has used the Help command
				//if the last letter in the word has been found by the use of the Help command, the Help command will not do anything when called again
				//the player has to enter a letter for the game to continue/finish
				if (letter.equals(Command.help.toString())) {
					isHelpUsed = true;
					
					//calls a method to find the next unknown letter
					findHelpLetter();
					//reveals the letter found by the Help command
					System.out.println("The secret word is: " + printDashes(dashedWord));
				}// end if
				//checks if any commands except from the Help command has been used
				else{
					menu(letter);
				}				
			} while (!letter.matches("[a-z]"));

			//counter to store the number of occurrences, if any, the given letter has in the word to be guessed
			int counter = 0;
			for (int i = 0; i < guesWord.length(); i++) {
				String currentLetter = Character.toString(guesWord.charAt(i));
				//checks if the given letter equals the current letter in guesWord
				if (letter.equals(currentLetter)) {
					//increase the counter if a match is found
					++counter;
					//replace the '_' in the buffer for found letters with the found letter at the appropriate place
					dashedWord.setCharAt(i, letter.charAt(0));
				}
			}
			
			//if no matches were found, the player guessed wrong
			if (counter == 0) {
				//increase the counter for mistakes made
				++mistakes;
				System.out.printf("Sorry! There are no unrevealed letters \'%s\'. \n", letter);
			} else {
				//tells the player how many letters that were revealed by the players guess
				System.out.printf("Good job! You revealed %d letter(s).\n", counter);
			}

		}

		if (!isHelpUsed) {
			System.out.println("You won with " + mistakes + " mistake(s).");
			System.out.println("The secret word is: " + printDashes(dashedWord));

			System.out.println("Please enter your name for the top scoreboard:");
			Scanner input = new Scanner(System.in);
			//takes a input string from the player as a name or nickname for the score board
			String playerName = input.next();
  
			filerw.openFileToWrite();
			//adds the name and score of the current player
			filerw.addRecords(mistakes, playerName);
			filerw.closeFileFromWriting();
			filerw.openFiletoRead();
			filerw.readRecords();
			filerw.closeFileFromReading();
			//prints the score board, including the current players entry
			filerw.printAndSortScoreBoard();

		} else {
			//if the player has used the Help command, the player is not allowed to enter the score board
			System.out.println("You won with " + mistakes + " mistake(s). but you have cheated. You are not allowed to enter into the scoreboard.");
			System.out.println("The secret word is: " + printDashes(dashedWord));
		}
		
		// restart the game
		new Game(true);
		
	}// end method
	
	//method for handling all commands except Help
	private void menu(String letter) {
		//the Restart command starts a new game
		if (letter.equals(Command.restart.toString())) {
			new Game(true);
		} 
		//the Top command prints the score board containing previus winners
		else if (letter.equals(Command.top.toString())) {
			filerw.openFiletoRead();
			filerw.readRecords();
			filerw.closeFileFromReading();
			filerw.printAndSortScoreBoard();
			new Game(true);
		} 
		//the Exit command exits the program
		else if (letter.equals(Command.exit.toString())) {
			System.exit(1);
		}
	}
	
	//finds the next letter in the word to be guessed not already found
	private void findHelpLetter(){
		int i = 0, j = 0;
		
		//true while a unknown character has not been found and i does not exceed the length of the word to be guessed
		while (j < 1 && i < dashedWord.length()) {
			if (dashedWord.charAt(i) == '_') {
				//if the current character in the buffer is unknown, reveal it
				dashedWord.setCharAt(i, guesWord.charAt(i));
				//exits the loop
				++j;
			}
			//go to the next letter in the buffer
			++i;
		}
	}
	
	//returns a StringBuffer with the same length as the given String and with all the characters set to '_'
	private StringBuffer getW(String word) {
		StringBuffer dashes = new StringBuffer("");
		
		for (int i = 0; i < word.length(); i++) {
			dashes.append("_");
		}
		return dashes;
	}
	
	//returns a String with all the characters in the given StringBuffer with a space before each character 
	private String printDashes(StringBuffer word) {
		String toDashes = "";
		
		for (int i = 0; i < word.length(); i++) {
			toDashes += (" " + word.charAt(i));
		}
		return toDashes;
	}
}
