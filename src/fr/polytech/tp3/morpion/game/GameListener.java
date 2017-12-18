package fr.polytech.tp3.morpion.game;

/**
 * Listener Interface of the game class
 * @author Valentin Berger
 * @see Game
 */
public interface GameListener {
	
	/**
	 * Method called when the game state has changed
	 * @param oldState the old game state
	 * @param newState the new game state
	 */
	void onStateChanged(EState oldState, EState newState);
	
	/**
	 * Method called when the token has changed
	 * @param oldToken the old token
	 * @param newToken the new token
	 */
	void onTokenChanged(ECell oldToken, ECell newToken);
	
	/**
	 * Method called when the number of games has changed
	 * @param nbGame the number of games
	 */
	void onNbGameChanged(int nbGame);
}
