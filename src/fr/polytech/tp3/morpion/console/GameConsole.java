package fr.polytech.tp3.morpion.console;

import fr.polytech.tp3.morpion.game.*;

import java.lang.management.GarbageCollectorMXBean;
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
			
			}
			
			@Override
			public void onTokenChanged(ECell oldToken, ECell newToken) {
			
			}
			
			@Override
			public Player onWin(Player winner) {
				if (winner != null)
					System.out.println("Congratualtion " + winner.getName() + "!!! You won!");
				else
					System.out.println("Draw!");
				
				System.out.println("Score:\n\t" + game.getP1().getName() + ": " + game.getP1().getNbWin() + "\n" +
						"\t" + game.getP2().getName() + ": " + game.getP2().getNbWin() + "\n" +
						"\tNumber of games: " + game.getNbGame());
				
				return winner;
			}
		});
		
		game.play();
	}
}
