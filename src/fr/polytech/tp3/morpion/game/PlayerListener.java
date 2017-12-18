package fr.polytech.tp3.morpion.game;

/**
 * Listener Interface for the Player class.
 * @author Valentin Berger
 * @see Player
 */
public interface PlayerListener {
	
	/**
	 * Method called when the name of the player has changed
	 * @param name The new player's name
	 */
	void onNameChanged(String name);
	
	/**
	 * Method called when the type of the player has changed
	 * @param type The new player's type
	 */
	void onTypeChanged(ECell type);
	
	/**
	 * Method called when the number of wins of the player has changed
	 * @param nbWin The new number of wins
	 */
	void onNbWinChanged(int nbWin);
}
