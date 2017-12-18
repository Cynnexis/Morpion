package fr.polytech.tp3.morpion;

import fr.polytech.tp3.morpion.console.GameConsole;
import fr.polytech.tp3.morpion.gui.Frame;

import java.util.Scanner;

/**
 * Main class
 * @author Valentin Berger
 */
public class Morpion {
	
	/**
	 * The main procedure. It asks the user(s) if they want to play Tic Tac Toe (or "Morpion" in French) in console or
	 * graphic mode.
	 * @param args The arguments of the program. It will not be parsed by the program.
	 */
	public static void main(String args[]) {
		// Setting up the name of the application
		System.setProperty("program.name", "Morpion");
		
		Scanner sc = new Scanner(System.in);
		boolean error = false;
		char choice = (char) 0;
		
		// Ask the user(s) if they want to play Tic Tac Toe in console or graphic mode.
		do {
			System.out.println("Would you like to play in Console mode or in Window mode? Type \'c\' for console, \'w\' for window or \'e\' to exit the application.");
			String strChoice = sc.next();
			if (strChoice != null && !strChoice.isEmpty())
			{
				choice = strChoice.charAt(0);
				error = choice != 'c' && choice != 'w' && choice != 'e';
			}
			else
				error = true;
			
			if (error)
				System.out.println("Please enter \'c\', \'w\' or \'e\'.");
		} while (error);
		
		// Launch either the console mode or frame to play the game according to the user's choice
		switch (choice)
		{
			case 'c':
				GameConsole game = new GameConsole();
				break;
			case 'w':
				Frame frame = Frame.getInstance();
				frame.display();
				break;
			default:
				System.exit(0);
				break;
		}
	}
}
