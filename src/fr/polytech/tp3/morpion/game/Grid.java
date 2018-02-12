package fr.polytech.tp3.morpion.game;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import fr.polytech.tp3.morpion.game.matrix.Matrix;
import fr.polytech.tp3.morpion.game.matrix.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A matrix adapted for the Tic Tac Toe game
 * @author Valentin Berger
 * @see Matrix
 * @see Game
 */
public class Grid extends Matrix<ECell> {
	
	/**
	 * Construct a grid with {@code nbColumns} and {@code nbRows}
	 * @param nbColumns The number of columns
	 * @param nbRows The number of rows
	 */
	public Grid(int nbColumns, int nbRows) {
		super(nbColumns, nbRows, ECell.EMPTY);
	}
	
	/**
	 * Construct a 3-by-3 grid
	 */
	public Grid() {
		super(3, 3, ECell.EMPTY);
	}
	
	/**
	 * Construct grid by copy
	 */
	public Grid(@NotNull Grid grid) {
		super(3, 3, ECell.EMPTY);
		
		if (grid == null)
			throw new NullPointerException();
		
		setNbColumns(grid.getNbColumns());
		setNbRows(grid.getNbRows());
		
		for (int i = 0; i < getNbColumns(); i++)
			for (int j = 0; j < getNbRows(); j++)
				set(i, j, grid.get(i, j) != null ? grid.get(i, j) : ECell.EMPTY);
	}
	
	/**
	 * Clear the grid by filling all the cells with {@code ECell.EMPTY}
	 */
	@Override
	public void clear() {
		init(getNbColumns(), getNbRows(), ECell.EMPTY);
	}
	
	/**
	 * Check if all cells in the grid have the value {@code ECell.EMPTY}
	 * @return Return {@code true} if all cells in the grid have the value {@code ECell.EMPTY}, otherwise {@code false}
	 */
	public boolean isEmpty() {
		boolean empty = true;
		
		for (int i = 0, imax = getNbColumns(); i < imax && empty; i++)
			for (int j = 0, jmax = getNbRows(); j < jmax && empty; j++)
				if (get(i, j) != ECell.EMPTY)
					empty = false;
		
		return empty;
	}
	
	/**
	 * Check if all cells in the grid have a different the value of {@code ECell.EMPTY}
	 * @return Return {@code true} if all cells in the grid have a different the value of {@code ECell.EMPTY}, otherwise {@code false}
	 */
	public boolean isFull() {
		boolean full = true;
		
		for (int i = 0, imax = getNbColumns(); i < imax && full; i++)
			for (int j = 0, jmax = getNbRows(); j < jmax && full; j++)
				if (get(i, j) == ECell.EMPTY)
					full = false;
		
		return full;
	}
	
	public @NotNull ArrayList<Point> getAvailableCells() {
		ArrayList<Point> points = new ArrayList<>(9);
		
		for (int i = 0; i < this.getNbColumns(); i++)
			for (int j = 0; j < this.getNbColumns(); j++)
				if (this.get(i, j) == ECell.EMPTY)
					points.add(new Point(i, j));
		
		return points;
	}
	
	public @NotNull ArrayList<Point> getAll(@Nullable ECell type) {
		ArrayList<Point> points = new ArrayList<>(9);
		
		for (int i = 0; i < this.getNbColumns(); i++)
			for (int j = 0; j < this.getNbColumns(); j++)
				if (this.get(i, j) == type)
					points.add(new Point(i, j));
		
		return points;
	}
	
	/**
	 * Build a Tic Tac Toe grid in string, using current value
	 * @return A Tic Tac Toe grid
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append("  ");
		for (int x = 0, xmax = getNbColumns(); x < xmax; x++)
			result.append(Integer.toString(x+1)).append(' ');
		
		result.append('\n');
		
		for (int y = 0, ymax = getNbRows(); y < ymax; y++)
		{
			result.append(Integer.toString(y+1)).append(' ');
			
			for (int x = 0, xmax = getNbColumns(); x < xmax; x++)
			{
				result.append(get(x, y).toString());
				if (x != xmax - 1)
					result.append('|');
			}
			
			result.append('\n');
			
			if (y != ymax - 1) {
				result.append("  ");
				for (int k = 0; k < ymax + (ymax - 1); k++)
					result.append('-');
				
				result.append('\n');
			}
		}
		
		return result.toString();
	}
}
