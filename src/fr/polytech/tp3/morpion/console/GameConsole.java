package fr.polytech.tp3.morpion.console;

import fr.polytech.tp3.morpion.game.*;
import fr.polytech.tp3.morpion.game.exceptions.CellFullException;
import fr.polytech.tp3.morpion.game.matrix.Point;

import java.util.Scanner;

public class GameConsole {
	
	private Game game;
	
	public GameConsole() {
		String p1, p2;
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Player1, enter your name:");
		p1 = sc.next();
		System.out.println(p1 + ", you will be the CROSS: X\n");
		
		System.out.println("Player2, enter your name:");
		p2 = sc.next();
		System.out.println(p2 + ", you will be the CIRCLE: O\n");
		
		game = new Game(p1, p2);
		
		game.setListener(new GameListener() {
			@Override
			public void onStateChanged(EState oldState, EState newState) {
				switch (newState)
				{
					case INITIALIZING:
						break;
					case PLAYING:
						break;
					case PAUSING:
						break;
					case P1WON:
						win(game.getP1());
						break;
					case P2WON:
						win(game.getP2());
						break;
					case DRAW:
						win(null);
						break;
				}
			}
			
			@Override
			public void onTokenChanged(ECell oldToken, ECell newToken) { }
			
			@Override
			public void onNbGameChanged(int nbGame) { }
		});
		
		boolean keepPlaying = true;
		
		while (keepPlaying) {
			game.newGame();
			
			while (game.getState() == EState.PLAYING) {
				Point coordinates = new Point();
				boolean error = false;
				
				Player p = game.getCurrentPlayer();
				
				do {
					System.out.println(game.getGrid().toString());
					do {
						System.out.println(p.getName() + ", choose a coordinate x between 1 and " + game.getGrid().getNbColumns() + ":");
						coordinates.setX(sc.nextInt());
						error = coordinates.getX() < 1 || coordinates.getX() > game.getGrid().getNbColumns();
						if (error)
							System.out.println("Sorry, those coordinates is not valid.");
					} while(error);
					
					do {
						System.out.println(p.getName() + ", choose a coordinate y between 1 and " + game.getGrid().getNbRows() + ":");
						coordinates.setY(sc.nextInt());
						error = coordinates.getY() < 1 || coordinates.getY() > game.getGrid().getNbRows();
						if (error)
							System.out.println("Sorry, those coordinates is not valid.");
					} while(error);
					
					// Changing the base of the coordinate: [(1, 1); (nbColumns, nbRows)] becomes [(0, 0); (nbColumns-1, nbRows-1)]
					coordinates.setX(coordinates.getX() - 1);
					coordinates.setY(coordinates.getY() - 1);
					
					try {
						game.play(coordinates);
					} catch (CellFullException ex) {
						error = true;
						System.out.println("The point (" + (coordinates.getX() + 1) + ", " + (coordinates.getY() + 1) + ") is already filled. Try again.");
					}
				} while (error);
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
					else {
						choice = strChoice.toLowerCase().charAt(0);
						error = !(choice == 'o' || choice == 'n');
					}
				}
				
				if (error)
					System.out.println("Sorry, this choice is not valid.");
			} while (error);
			
			keepPlaying = choice == 'o';
		}
		
		System.out.println("Goodbye!");
	}
	
	private void win(Player winner) {
		if (winner != null)
			System.out.println("Congratualtion " + winner.getName() + "!!! You won!");
		else
			System.out.println("Draw!");
		
		System.out.println("Score:\n\t" + game.getP1().getName() + ": " + game.getP1().getNbWin() + "\n" +
				"\t" + game.getP2().getName() + ": " + game.getP2().getNbWin() + "\n" +
				"\tNumber of games: " + game.getNbGame());
	}
}
