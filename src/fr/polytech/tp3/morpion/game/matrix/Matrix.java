package fr.polytech.tp3.morpion.game.matrix;

import java.util.ArrayList;

public class Matrix<T> {
	
	private ArrayList<ArrayList<T>> matrix = null;
	private int nbColumns = 0;
	private int nbRows = 0;
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
	public T get(Point point) {
		return get(point.getX(), point.getY());
	}
	
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
	public boolean set(T value, int x, int y) {
		return set(x, y, value);
	}
	public boolean set(Point point, T value) {
		return set(point.getX(), point.getY(), value);
	}
	public boolean set(T value, Point point) {
		return set(value, point.getX(), point.getY());
	}
	
	public T remove(int x, int y) {
		T result = null;
		try {
			result = matrix.get(x).remove(y);
		} catch (ArrayIndexOutOfBoundsException ex) {
			ex.printStackTrace();
			result = null;
		}
		
		return result;
	}
	public T remove(Point point) {
		return remove(point.getX(), point.getY());
	}
	
	public void clear(T defaultValue) {
		init(getNbColumns(), getNbRows(), defaultValue);
	}
	public void clear() {
		clear(null);
	}
	
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
