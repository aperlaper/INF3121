package hangman;

import java.io.Serializable;

public class Players implements Serializable{
	
	//generated unique ID for serialization
	private static final long serialVersionUID = -9134533025368583658L;
	//name of the player
	private String name;
	//the players score
	private int scores;

	public Players(String name, int scores) {
		this.name = name;
		this.scores = scores;
	}
	
	public String getName() {
		return name;
	}

	public int getScores() {
		return scores;
	}
}
