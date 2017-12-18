package fr.polytech.tp3.morpion.game.matrix;

import java.util.EventListener;

/**
 * Listener Interface for the Matrix class
 * @author Valentin Berger
 * @param <T> The type of data the class Matrix uses
 * @see Matrix
 */
public interface MatrixListener<T> {
	
	/**
	 * Method called when a cell in the matrix is changed
	 * @param x The column index of the cell
	 * @param y The row index of the cell
	 * @param value The new value of the cell
	 */
	void OnCellChanged(int x, int y, T value);
}
