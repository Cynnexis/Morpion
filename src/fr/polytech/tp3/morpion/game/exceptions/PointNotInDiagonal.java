package fr.polytech.tp3.morpion.game.exceptions;

public class PointNotInDiagonal extends Exception {
	
	public PointNotInDiagonal() { super(); }
	
	public PointNotInDiagonal(String message) { super(message); }
	
	public PointNotInDiagonal(String message, Exception innerException) { super(message, innerException); }
}
