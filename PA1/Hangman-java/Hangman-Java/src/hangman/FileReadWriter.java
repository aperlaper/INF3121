package hangman;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileReadWriter {
	//OutputStream used to write to file
	private ObjectOutputStream output;
	//InputStream used to read from file
	private ObjectInputStream input;
	//list of previous winners
	ArrayList<Players> myArr = new ArrayList<Players>();

	//opens a OutputStream to be written to
	public void openFileToWrite() {
		try // open file
		{
			output = new ObjectOutputStream(new FileOutputStream("players.ser", true));

		} catch (IOException ioException) {
			System.err.println("Error opening file to write.");
		}
	}

	// adds a record to file
	public void addRecords(int scores, String name) {
		Players players = new Players(name, scores); // object to be written to file

		try { // output values to file
			output.writeObject(players);
		} catch (IOException ioException) {
			System.err.println("Error writing to file.");
			return;
		}
	}

	//closes a OutputStream if it is open
	public void closeFileFromWriting() {
		try // close file
		{
			if (output != null){
			  output.close();
			}
		} catch (IOException ioException) {
			//show error
			System.err.println("Error closing file.");
			//exit
			System.exit(1);
		}
	}

	//opens a file to be read from
	public void openFiletoRead() {
		try {
			input = new ObjectInputStream(new FileInputStream("players.ser"));
		} catch (IOException ioException) {
			System.err.println("Error opening file to read.");
		}
	}

	//reads the scores and names of previous winners from a OutputStream
	public void readRecords() {
		Players records;

		try // input the values from the file
		{
			Object obj = null;

			//true while there is a object to read from the OutputStream 
			while (!(obj = input.readObject()).equals(null)) {
				//if the object read is a Players object, add it to the ArrayList of previous winners 
				if (obj instanceof Players) {
					records = (Players) obj;
					myArr.add(records);
					System.out.printf("DEBUG: %-10d%-12s\n", records.getScores(), records.getName());
				}
			}
		} // end try
		catch (EOFException endOfFileException) {
			return; // end of file was reached
		} catch (ClassNotFoundException classNotFoundException) {
			System.err.println("Unable to create object.");
		} catch (IOException ioException) {
			System.err.println("Error during reading from file.");
		}
	}

	//closes a InputStream if it is open
	public void closeFileFromReading() {
		try {
			if (input != null){
				input.close();
			}
			
			// exit
			System.exit(0);
		} catch (IOException ioException) {
			System.err.println("Error closing file.");
			System.exit(1);
		}
	}

	//calls a method to sort the scores and prints the sorted scores in a score board
	public void printAndSortScoreBoard() {
		//calls a sorting method to sort the ArrayList with players with the lowest scores first
		sortPlayersArrayList();
		
		//prints a score board with placement, name of player and score
		//the lowest score is the best
		System.out.println("Scoreboard:");
		for (int i = 0; i < myArr.size(); i++) {
			System.out.printf("%d. %s ----> %d\n", i+1, myArr.get(i).getName(), myArr.get(i).getScores());
		}
	}
	
	//sorts a ArrayList with players
	//the lowest score gets first place in the list
	private void sortPlayersArrayList(){
		Players temp;
		int n = myArr.size();
		
		//sorting a list with the lowest score first followed by increasing scores
		for (int pass = 1; pass < n; pass++) {
			for (int i = 0; i < n - pass; i++) {
				if (myArr.get(i).getScores() > myArr.get(i + 1).getScores()) {
					temp = myArr.get(i);
					myArr.set(i, myArr.get(i + 1));
					myArr.set(i + 1, temp);
				}
			}
		}
	}
	
	
}
