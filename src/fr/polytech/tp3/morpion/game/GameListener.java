package fr.polytech.tp3.morpion.game;

public interface GameListener {
	
	void onStateChanged(EState oldState, EState newState);
	void onTokenChanged(ECell oldToken, ECell newToken);
	
	/**
	 * The function to execute when win
	 * @param winner The player who won the game. If it is `null`, that means it is a draw.
	 * @return Return the parameter `winner` (this method can change the instance `winner`)
	 */
	Player onWin(Player winner);
}
