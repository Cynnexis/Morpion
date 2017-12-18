package fr.polytech.tp3.morpion.game;

import com.sun.istack.internal.NotNull;
import fr.polytech.tp3.morpion.game.exceptions.CellFullException;
import fr.polytech.tp3.morpion.game.matrix.Point;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * The Tic Tac Toe game engine
 * @author Valentin Berger
 */
public class Game {
	
	/**
	 * The player 1
	 */
	private Player p1;
	/**
	 * The player 2
	 */
	private Player p2;
	/**
	 * The current token. It shows which player has the turn to play. If player 1 is playing, the token is
	 * {@code ECell.CROSS}, and if it is player 2, the token is {@code ECell.CIRCLE}
	 */
	private ECell token;
	/**
	 * The Tic Tac Toe grid
	 */
	private Grid grid;
	/**
	 * The current state of the game. At the beginning, it is {@code EState.INITIALIZING}
	 */
	private EState state = EState.INITIALIZING;
	/**
	 * The number of games the players play from the beginning of the program
	 */
	private int nbGame = 0;
	/**
	 * The listener of the game. It cannot be null
	 */
	@NotNull
	private GameListener listener = new GameListener() {
		@Override
		public void onStateChanged(EState oldState, EState newState) { }
		@Override
		public void onTokenChanged(ECell oldToken, ECell newToken) { }
		@Override
		public void onNbGameChanged(int nbGame) { }
	};
	
	/**
	 * Initialize a game with the name of the player
	 * @param p1Name The name of player 1
	 * @param p2Name The name of player 2
	 */
	public Game(String p1Name, String p2Name) {
		init(p1Name, p2Name);
	}
	/**
	 * Initialize a game with and give to player 1 the name "p1", and to player 2 "p2"
	 */
	public Game() {
		init("p1", "p2");
	}
	
	/**
	 * Initialize the game
	 * @param p1Name The name of player 1
	 * @param p2Name The name of player 2
	 */
	private void init(String p1Name, String p2Name) {
		p1 = new Player(ECell.CROSS);
		p1.setName(p1Name);
		p2 = new Player(ECell.CIRCLE);
		p2.setName(p2Name);
		token = ECell.CROSS;
		setState(EState.INITIALIZING);
		nbGame = 0;
		grid = new Grid(3, 3);
	}
	
	/**
	 * <p>
	 * If the state of the game is {@code EState.PLAYING}, then this method will make the player chosen by the
	 * {@code token} play. It will fill the cell of coordinates {@code coordinates} in the grid {@code grid} with the
	 * player's type if the rules are respected. Then, the procedure checks if there is a winner by using the
	 * {@code checkWin} method. In the case there is a winner, the game state turns into {@code EState.P1WON} or
	 *  {@code EState.P2WON}, the number of victory of the winner and the number of game is incremented by 1
	 * </p>
	 * @param coordinates The coordinates
	 * @throws CellFullException Throw if the cell of coordinates {@code coordinates} is already full
	 * @see Player
	 * @see CellFullException
	 */
	public void play(Point coordinates) throws CellFullException {
		if (getState() == EState.PLAYING) {
			// The current player is placed in the variable p.
			/* Note: if p changes (ex: `p.setName("test")`), then the associated player (p1 ou p2) changes too because
			   the instantiation in Java transfer the pointer to the object in the memory (even if the notion of pointer
			   does not exist in Java). Then, p is like a pointer to p1 or p2 according to the value of the token. */
			Player p = token == ECell.CROSS ? p1 : p2;
			
			boolean error = false;
			
			do {
				error = grid.get(coordinates) != ECell.EMPTY;
				if (error)
					throw new CellFullException(grid.get(coordinates), coordinates);
			} while (grid.get(coordinates) != ECell.EMPTY);
			
			try {
				grid.set(coordinates, token);
			} catch (ArrayIndexOutOfBoundsException ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
			
			// Inverting the token:
			setToken(token == ECell.CROSS ? ECell.CIRCLE : ECell.CROSS);
			
			// Check if someone won:
			ECell result;
			result = checkWin();
			switch (result)
			{
				case EMPTY:
					// If checkWin() returns 'EMPTY' and the grid is full, it is a draw:
					if (grid.isFull()) {
						setNbGame(getNbGame() + 1);
						setState(EState.DRAW);
					}
					// Otherwise, the game continues (state = PLAYING)
					break;
				case CROSS:
					p1.incrementNbWin();
					setNbGame(getNbGame() + 1);
					setState(EState.P1WON);
					break;
				case CIRCLE:
					p2.incrementNbWin();
					setNbGame(getNbGame() + 1);
					setState(EState.P2WON);
					break;
			}
		}
	}
	/**
	 * <p>
	 * If the state of the game is {@code EState.PLAYING}, then this method will make the player chosen by the
	 * {@code token} play. It will fill the cell of coordinates ({@code x} ; {@code y}) in the grid {@code grid} with the
	 * player's type if the rules are respected. Then, the procedure checks if there is a winner by using the
	 * {@code checkWin} method. In the case there is a winner, the game state turns into {@code EState.P1WON} or
	 * {@code EState.P2WON}, the number of victory of the winner and the number of game is incremented by 1
	 * </p>
	 * @param x The column index
	 * @param y The column index
	 * @throws CellFullException Throw if the cell of coordinates ({@code x} ; {@code y}) is already full
	 * @see Player
	 * @see CellFullException
	 */
	public void play(int x, int y) throws CellFullException {
		play(new Point(x, y));
	}
	
	/**
	 * Clear the grid and set the game state to {@code EState.PLAYING}
	 */
	public void newGame() {
		grid.clear();
		setState(EState.PLAYING);
	}
	
	/**
	 * <p>
	 * Check if one of the two players won. To do so, the method checks the pattern of each token in the grid
	 * </p>
	 * <p>
	 * The method cannot check a m*n grid. See https://en.wikipedia.org/wiki/M,n,k-game
	 * </p>
	 * @return Return `CROSS` if the player associated to the cross won, `CIRCLE` if the player associated to the circle
	 * won, or `EMPTY` if nothing happen (it can be a draw, so it is advise to check if the grid is full after calling
	 * this method).
	 * @throws NotImplementedException Throw an exception if the grid is not a 3-by-3 grid
	 */
	private ECell checkWin() throws NotImplementedException {
		if (grid.getNbColumns() != 3 || grid.getNbRows() != 3)
			throw new NotImplementedException();
		
		ECell winner = ECell.EMPTY;
		
		// This 'for' will test the 8 possibilities of winning using the CROSS token the first time, and then CIRCLE
		// token:
		for (ECell tok : ECell.values())
		{
			// 8 cases to check:
			if (tok != ECell.EMPTY && (
				(grid.get(0, 0) == tok && grid.get(1, 1) == tok && grid.get(2, 2) == tok) || // Diagonal \
				(grid.get(2, 0) == tok && grid.get(1, 1) == tok && grid.get(0, 2) == tok) || // Diagonal /
				(grid.get(0, 0) == tok && grid.get(1, 0) == tok && grid.get(2, 0) == tok) || // Top row
				(grid.get(0, 1) == tok && grid.get(1, 1) == tok && grid.get(2, 1) == tok) || // Middle row
				(grid.get(0, 2) == tok && grid.get(1, 2) == tok && grid.get(2, 2) == tok) || // Bottom row
				(grid.get(0, 0) == tok && grid.get(0, 1) == tok && grid.get(0, 2) == tok) || // Right column
				(grid.get(1, 0) == tok && grid.get(1, 1) == tok && grid.get(1, 2) == tok) || // Center column
				(grid.get(2, 0) == tok && grid.get(2, 1) == tok && grid.get(2, 2) == tok))) {
				winner = tok;
				break;
			}
		}
		
		return winner;
	}
	
	/**
	 * Returns the player who is playing, according to the token.
	 * @return Return the player {@code p1}, {@code p2} or {@code null} if the game is stopped.
	 */
	public Player getCurrentPlayer() {
		switch (token)
		{
			case CROSS:
				return p1;
			case CIRCLE:
				return p2;
			default:
				return null;
		}
	}
	
	/* GETTERS & SETTERS */
	
	public Player getP1() {
		return p1;
	}
	
	public void setP1(Player p1) {
		this.p1 = p1;
	}
	
	public Player getP2() {
		return p2;
	}
	
	public void setP2(Player p2) {
		this.p2 = p2;
	}
	
	public ECell getToken() {
		return token;
	}
	
	public void setToken(ECell token) {
		ECell old = this.token;
		this.token = token;
		listener.onTokenChanged(old, token);
	}
	
	public Grid getGrid() {
		return grid;
	}
	
	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	
	public EState getState() {
		return state;
	}
	
	public void setState(EState state) {
		EState old = this.state;
		this.state = state;
		listener.onStateChanged(old, state);
	}
	
	public int getNbGame() {
		return nbGame;
	}
	
	public void setNbGame(int nbGame) {
		if (nbGame >= 0) {
			this.nbGame = nbGame;
			listener.onNbGameChanged(nbGame);
		}
	}
	
	public GameListener getListener() {
		return listener;
	}
	
	public void setListener(GameListener listener) {
		this.listener = listener != null ? listener : new GameListener() {
			@Override
			public void onStateChanged(EState oldState, EState newState) { }
			@Override
			public void onTokenChanged(ECell oldToken, ECell newToken) { }
			@Override
			public void onNbGameChanged(int nbGame) { }
		};
	}
}
