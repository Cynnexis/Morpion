package fr.polytech.tp3.morpion.game.matrix;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;

/**
 * The matrix class is a sort or 2-dimensional ArrayList. It can contains a table of m*n elements by using an
 * ArrayList&lt;ArrayList&lt;T&gt;&gt;
 * @author Valentin Berger
 * @param <T> The type of data
 */
public class Matrix<T> {
	
	private ArrayList<ArrayList<T>> matrix = null;
	private int nbColumns = 0;
	private int nbRows = 0;
	/**
	 * The listener of the matrix. It cannot be null.
	 */
	@NotNull
	private MatrixListener<T> listener = (x, y, value) -> { };
	
	public Matrix(int nbColumns, int nbRows, T defaultValue) {
		init(nbColumns, nbRows, defaultValue);
	}
	public Matrix(int nbColumns, int nbRows) {
		init(nbColumns, nbRows, null);
	}
	public Matrix() {
		init(0, 0, null);
	}
	
	/**
	 * Initialize the attributes and the matrix
	 * @param nbColumns The number of columns of the matrix
	 * @param nbRows The number of rows of the matrix
	 * @param defaultValue The default value to put in every cell of the matrix
	 */
	protected void init(int nbColumns, int nbRows, T defaultValue) {
		setNbColumns(nbColumns);
		setNbRows(nbRows);
		
		matrix = new ArrayList<>(nbColumns);
		
		for (int i = 0; i < nbColumns; i++)
		{
			matrix.add(new ArrayList<>(nbRows));
			for (int j = 0; j < nbColumns; j++)
			{
				matrix.get(i).add(defaultValue);
				listener.OnCellChanged(i, j, defaultValue);
			}
		}
	}
	
	/**
	 * Get the element of the matrix contains in the cell ({@code x} ; {@code y})
	 * @param x The column index
	 * @param y The row index
	 * @return The element. If the coordinates are out of the grid, the result is {@code null}
	 */
	public T get(int x, int y) {
		T result = null;
		try {
			result = matrix.get(x).get(y);
		} catch (ArrayIndexOutOfBoundsException ex) {
			ex.printStackTrace();
			result = null;
		}
		
		return result;
	}
	/**
	 * Get the element of the matrix contains in the cell ({@code point.x} ; {@code point.y})
	 * @param point The coordinates of the cell to fetch
	 * @return The element. If the coordinates are out of the grid, the result is {@code null}
	 */
	public T get(Point point) {
		return get(point.getX(), point.getY());
	}
	
	/**
	 * Set the value of the cell ({@code x} ; {@code y}) to {@code value}
	 * @param x The column index
	 * @param y The row index
	 * @param value The new value
	 * @return Return {@code true} if the method sets the cell to {@code value}, otherwise {@code false}
	 */
	public boolean set(int x, int y, T value) {
		boolean success = true;
		
		try {
			matrix.get(x).set(y, value);
			listener.OnCellChanged(x, y, value);
		} catch (ArrayIndexOutOfBoundsException ex) {
			ex.printStackTrace();
			success = false;
		}
		
		return success;
	}
	/**
	 * Set the value of the cell ({@code point.x} ; {@code point.y}) to {@code value}
	 * @param point The coordinates of the cell to fetch
	 * @param value The new value
	 * @return Return {@code true} if the method sets the cell to {@code value}, otherwise {@code false}
	 */
	public boolean set(Point point, T value) {
		return set(point.getX(), point.getY(), value);
	}
	
	/**
	 * Clear the matrix by setting all cells to {@code defaultValue}
	 * @param defaultValue The value to apply to all cells
	 */
	public void clear(T defaultValue) {
		init(getNbColumns(), getNbRows(), defaultValue);
	}
	/**
	 * Clear the matrix by setting all cells to {@code null}
	 */
	public void clear() {
		clear(null);
	}
	
	/**
	 * Search if the matrix contains {@code object}
	 * @param object The object to search in the matrix
	 * @return Return {@code true} if {@code object} is in the matrix, otherwise {@code false}
	 */
	public boolean contains(T object) {
		boolean found = false;
		
		try {
			for (int i = 0; i < nbColumns && !found; i++)
				if (matrix.get(i).contains(object))
					found = true;
		} catch (ArrayIndexOutOfBoundsException ex) {
			found = false;
		}
		
		return found;
	}
	
	/**
	 * Return the size of the matrix
	 * @return Return a point such that the coordinate {@code x} is the number of columns, and {@code y} the number of
	 * rows.
	 */
	public Point size() {
		return new Point(getNbColumns(), getNbRows());
	}
	
	/* GETTERS & SETTERS */
	
	public ArrayList<ArrayList<T>> getMatrix() {
		return matrix;
	}
	
	public void setMatrix(ArrayList<ArrayList<T>> matrix) {
		this.matrix = matrix;
	}
	
	public int getNbColumns() {
		return nbColumns;
	}
	
	public void setNbColumns(int nbColumns) {
		if (nbColumns >= 0)
			this.nbColumns = nbColumns;
	}
	
	public int getNbRows() {
		return nbRows;
	}
	
	public void setNbRows(int nbRows) {
		if (nbRows >= 0)
			this.nbRows = nbRows;
	}
	
	public MatrixListener getListener() {
		return listener;
	}
	
	public void setListener(MatrixListener listener) {
		this.listener = listener != null ? listener : (x, y, value) -> { };
	}
}
