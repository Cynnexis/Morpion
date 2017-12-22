package fr.polytech.tp3.morpion.game.exceptions;

public class GridFullException extends Exception {
	
	public GridFullException() {
		super("The grid is full.");
	}
}
