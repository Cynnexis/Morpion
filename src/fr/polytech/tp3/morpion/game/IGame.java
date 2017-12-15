package fr.polytech.tp3.morpion.game;

import fr.polytech.tp3.morpion.game.matrix.Point;

public interface IGame {
	
	/**
	 * Request the name of the player 1
	 * @return A name for player 1, different from player2 (who has not enter their name yet).
	 */
	String requestPlayer1Name();
	
	/**
	 * Request the name of the player 2
	 * @param error `true` when this method has been called before and the name returned is already used by player1.
	 * @return A name for player 2, different from player1.
	 */
	String requestPlayer2Name(boolean error);
	
	/**
	 *
	 * @param winner The player who won the game. If it is `null`, that means it is a draw.
	 * @return Return the parameter `winner` (this method can change the instance `winner`)
	 */
	Player onWin(Player winner);
	
	/**
	 * The player `player` play and choose a coordinate
	 * @param player The player who plays
	 * @param grid The current grid
	 * @return Return the coordinate in the basis [(0, 0), (nbColumns-1, nbRows-1)] where the player wants to put
	 * their token
	 */
	Point play(Player player, Grid grid);
}
