package fr.polytech.tp3.morpion.game.exceptions;

import fr.polytech.tp3.morpion.game.ECell;
import fr.polytech.tp3.morpion.game.matrix.Point;

public class CellFullException extends Exception {
	
	public CellFullException(ECell type, Point coordinates) {
		super("The cell (" + coordinates.getX() + ", " + coordinates.getY() + ") is full by \'" + type.toString() + "\'.");
	}
}
