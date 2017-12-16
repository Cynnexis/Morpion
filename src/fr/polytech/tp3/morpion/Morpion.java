package fr.polytech.tp3.morpion;

import fr.polytech.tp3.morpion.console.GameConsole;
import fr.polytech.tp3.morpion.game.Game;
import fr.polytech.tp3.morpion.gui.Frame;

import java.util.Scanner;

public class Morpion {

	public static void main(String args[]) {
		System.setProperty("program.name", "Morpion");
		
		Scanner sc = new Scanner(System.in);
		boolean error = false;
		char choice = (char) 0;
		
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
		
		switch (choice)
		{
			case 'c':
				GameConsole game = new GameConsole();
				break;
			case 'w':
				Frame frame = new Frame();
				break;
			default:
				System.exit(0);
				break;
		}
	}
}
