package fr.polytech.tp3.morpion.game;

import fr.polytech.tp3.morpion.game.exceptions.CellFullException;
import fr.polytech.tp3.morpion.game.matrix.Point;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Scanner;

public class Game {
	
	private Player p1;
	private Player p2;
	private ECell token;
	private Grid grid;
	private EState state = EState.INITIALIZING;
	private int nbGame = 0;
	private GameListener listener = new GameListener() {
		@Override
		public void onStateChanged(EState oldState, EState newState) { }
		@Override
		public void onTokenChanged(ECell oldToken, ECell newToken) { }
		@Override
		public void onNbGameChanged(int nbGame) { }
	};
	
	public Game(String p1Name, String p2Name) {
		init(p1Name, p2Name);
	}
	public Game() {
		init("p1", "p2");
	}
	
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
	
	@Deprecated
	public void play() {
		grid.clear();
		setState(EState.PLAYING);
		
		while (getState() == EState.PLAYING) {
			playOneTurn();
		}
		
		Player winner;
		switch (getState())
		{
			case P1WON:
				p1.incrementNbGameAndNbWin();
				p2.incrementNbGame();
				winner = p1;
				break;
			case P2WON:
				p2.incrementNbGameAndNbWin();
				p1.incrementNbGame();
				winner = p2;
				break;
			default:
				p1.incrementNbGame();
				p2.incrementNbGame();
				winner = null;
				break;
		}
		
		setNbGame(getNbGame() + 1);
		
		// The token is given to the looser (if exists), otherwise it is p1
		if (state != EState.DRAW)
		{
			if (state == EState.P1WON)
				setToken(p2.getType());
			else
				setToken(p1.getType());
		}
		else
			setToken(p1.getType());
		
		setState(EState.PLAYING);
	}
	
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
						p1.incrementNbGame();
						p2.incrementNbGame();
						setNbGame(getNbGame() + 1);
						setState(EState.DRAW);
					}
					// Otherwise, the game continues (state = PLAYING)
					break;
				case CROSS:
					p1.incrementNbGameAndNbWin();
					p2.incrementNbGame();
					setNbGame(getNbGame() + 1);
					setState(EState.P1WON);
					break;
				case CIRCLE:
					p2.incrementNbGameAndNbWin();
					p1.incrementNbGame();
					setNbGame(getNbGame() + 1);
					setState(EState.P2WON);
					break;
			}
		}
	}
	public void play(int x, int y) throws CellFullException {
		play(new Point(x, y));
	}
	
	public void newGame() {
		grid.clear();
		setState(EState.PLAYING);
	}
	
	@Deprecated
	private void playOneTurn() {
		Point coordinates = new Point();
		boolean error = false;
		
		System.out.println(grid.toString());
		
		if (getState() == EState.PLAYING) {
			// The current player is placed in the variable p.
			/* Note: if p changes (ex: `p.setName("test")`), then the associated player (p1 ou p2) changes too because
			   the instantiation in Java transfer the pointer to the object in the memory (even if the notion of pointer
			   does not exist in Java). Then, p is like a pointer to p1 or p2 according to the value of the token. */
			Player p = token == ECell.CROSS ? p1 : p2;
			
			do {
				coordinates = p.play(grid.getNbColumns(), grid.getNbRows());
				error = grid.get(coordinates) != ECell.EMPTY;
				if (error)
					System.out.println("The point (" + (coordinates.getX() + 1) + ", " + (coordinates.getY() + 1) + ") is already filled. Try again.");
			} while (grid.get(coordinates) != ECell.EMPTY);
			
			try {
				if (grid.get(coordinates) == ECell.EMPTY)
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
					if (grid.isFull())
						setState(EState.DRAW);
					// Otherwise, the game continue (state = PLAYING)
					break;
				case CROSS:
					state = EState.P1WON;
					break;
				case CIRCLE:
					state = EState.P2WON;
					break;
			}
		}
	}
	
	/**
	 * The method cannot check a m*n grid. See https://en.wikipedia.org/wiki/M,n,k-game
	 * @return Return `CROSS` if the player associated to the cross won, `CIRCLE` if the player associated to the circle
	 * won, or `EMPTY` if nothing happen (it can be a draw, so it is advise to check if the grid is full after calling
	 * this method).
	 * @throws NotImplementedException
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
	 * @return Return the player `p1`, `p2` or `null` if the game is stopped.
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
