package fr.polytech.tp3.morpion.game;

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
		public Player onWin(Player winner) {
			return winner;
		}
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
	
	public void play() {
		setState(EState.PLAYING);
		Scanner sc = new Scanner(System.in);
		boolean keepPlaying = true;
		
		while (keepPlaying) {
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
					winner = null;
					break;
			}
			
			nbGame++;
			
			Player w2 = listener.onWin(winner);
			if (winner != null && !winner.equals(w2)) {
				if (getState() == EState.P1WON)
					setP1(w2);
				else if (getState() == EState.P2WON)
					setP2(w2);
			}
			
			String strChoice;
			char choice = (char) 0;
			boolean error;
			do {
				System.out.println("Would like to continue the game? [o/n]");
				strChoice = sc.next();
				
				if (strChoice == null)
					error = true;
				else {
					if (strChoice.length() <= 0)
						error = true;
					else
					{
						choice = strChoice.toLowerCase().charAt(0);
						error = !(choice == 'o' || choice == 'n');
					}
				}
				
				if (error)
					System.out.println("Sorry, this choice is not valid.");
			} while (error);
			
			keepPlaying = choice == 'o';
			
			if (keepPlaying)
			{
				grid.clear();
				
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
		}
		System.out.println("Goodbye!");
	}
	
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
					System.out.println("The point (" + (coordinates.getX() + 1) + ", " + (coordinates.getY() + 1) + ") is already occupied. Try again.");
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
		for (ECell tok = ECell.CROSS; tok != ECell.CIRCLE && winner == ECell.EMPTY; tok = ECell.CIRCLE)
		{
			// 8 cases to win:
			if (
					(grid.get(0, 0) == tok && grid.get(1, 1) == tok && grid.get(2, 2) == tok) ||
							(grid.get(2, 0) == tok && grid.get(1, 1) == tok && grid.get(0, 2) == tok) ||
							(grid.get(0, 0) == tok && grid.get(1, 0) == tok && grid.get(2, 0) == tok) ||
							(grid.get(0, 1) == tok && grid.get(1, 1) == tok && grid.get(2, 1) == tok) ||
							(grid.get(0, 2) == tok && grid.get(1, 2) == tok && grid.get(2, 2) == tok) ||
							(grid.get(0, 0) == tok && grid.get(0, 1) == tok && grid.get(0, 2) == tok) ||
							(grid.get(1, 0) == tok && grid.get(1, 1) == tok && grid.get(1, 2) == tok) ||
							(grid.get(2, 0) == tok && grid.get(2, 1) == tok && grid.get(2, 2) == tok))
				winner = tok;
		}
		
		return winner;
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
		this.nbGame = nbGame;
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
			public Player onWin(Player winner) {
				return winner;
			}
		};
	}
}
