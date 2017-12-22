package fr.polytech.tp3.morpion.game.ai;

import com.sun.istack.internal.NotNull;
import fr.polytech.tp3.morpion.game.ECell;
import fr.polytech.tp3.morpion.game.Grid;
import fr.polytech.tp3.morpion.game.exceptions.CellFullException;
import fr.polytech.tp3.morpion.game.exceptions.PointNotInDiagonal;
import fr.polytech.tp3.morpion.game.matrix.Point;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.security.InvalidParameterException;

/**
 * Give the situation of the artificial intelligence if it marks a specific point in the grid
 * @author Valentin Berger
 */
public class Situation {
	
	/**
	 * The type of situation
	 */
	public enum Type {
		COLUMN, ROW,
		DIAGONAL_INC, DIAGONAL_DEC
	}
	
	/**
	 * The point where the user want his mark to be
	 */
	@NotNull
	private Point point = new Point();
	/**
	 * The number of cells occupied by player 2 (artificial intelligence) in the column/row (depending on {@code type})
	 */
	private int numberOfCell2 = 0;
	/**
	 * The number of cells occupied by player 1 (user) in the column/row (depending on {@code type})
	 */
	private int numberOfCell1 = 0;
	/**
	 * The number of empty cells in the column/row (depending on {@code type})
	 */
	private int numberOfEmptyCell = 0;
	/**
	 * The type of situation
	 */
	@NotNull
	private Type type = Type.COLUMN;
	
	/**
	 * The constructor of Situation. Build the situation by computing the number of cell occupied by player 1, player 2
	 * (Kartona) and empty cell depending on the value of {@code type} and where the artificial intelligence wants to
	 * place its point {@code coordinates}
	 * @param grid The grid containing the game
	 * @param coordinates The coordinates where Kartona wants to place its point
	 * @param type The type of situation
	 * @throws NullPointerException Throw this exception when one of the arguments is null
	 * @throws CellFullException Throw this exception if {@code grid} is already full (there is no need to compute a
	 * situation in a full grid)
	 * @throws NotImplementedException Throw this exception if the desired type of situation {@code type} is equal to
	 * {@code DIAGONAL_INC} or {@code DIAGONAL_DEC} and the number of columns is different from the number of rows in
	 * the grid
	 * @throws PointNotInDiagonal Throw this exception if the desired type of situation {@code type} is equal to
	 * {@code DIAGONAL_INC} or {@code DIAGONAL_DEC} and {@code coordinates} is not in the disired diagonal
	 * @throws InvalidParameterException Throw this exception if the value of {@code type} is unknown
	 * @see Type
	 */
	public Situation(@NotNull Grid grid, @NotNull Point coordinates, @NotNull Type type) throws NullPointerException, CellFullException, NotImplementedException, PointNotInDiagonal, InvalidParameterException {
		if (grid == null || coordinates == null || type == null)
			throw new NullPointerException();
		
		setType(type);
		setPoint(coordinates);
		
		if (grid.get(coordinates) != ECell.EMPTY)
			throw new CellFullException(grid.get(coordinates), coordinates);
		
		boolean pointOnDiagonal;
		
		switch (type)
		{
			case COLUMN:
				for (int j = 0; j < grid.getNbRows(); j++)
					if (j != coordinates.getY())
						update(grid, coordinates.getX(), j);
				break;
			case ROW:
				for (int i = 0; i < grid.getNbColumns(); i++)
					if (i != coordinates.getX())
						update(grid, i, coordinates.getY());
				break;
			case DIAGONAL_INC:
				if (grid.getNbColumns() != grid.getNbRows())
					throw new NotImplementedException();
				
				pointOnDiagonal = false;
				for (int i = 0; i < grid.getNbColumns(); i++)
					if (coordinates.getX() == i && coordinates.getY() == grid.getNbColumns() - i - 1)
						pointOnDiagonal = true;
				
				if (!pointOnDiagonal)
					throw new PointNotInDiagonal();
				
				for (int i = 0; i < grid.getNbColumns(); i++)
					if (i != coordinates.getX() || i != coordinates.getY())
						update(grid, i, grid.getNbColumns() - i - 1);
				break;
			case DIAGONAL_DEC:
				if (grid.getNbColumns() != grid.getNbRows())
					throw new NotImplementedException();
				
				pointOnDiagonal = false;
				for (int i = 0; i < grid.getNbColumns(); i++)
					if (coordinates.getX() == i && coordinates.getY() == i)
						pointOnDiagonal = true;
				
				if (!pointOnDiagonal)
					throw new PointNotInDiagonal();
				
				for (int i = 0; i < grid.getNbColumns(); i++)
					if (i != coordinates.getX() || i != coordinates.getY())
						update(grid, i, i);
				break;
			default:
				throw new InvalidParameterException();
		}
	}
	
	/**
	 * Update the attributes {@code numberOfCell1}, {@code numberOfCell2} and {@code numberOfEmptyCell} according to the
	 * value(s) of {@code grid} and {@code coordinates}
	 * @param grid The grid
	 * @param coordinates The coordinate to parse
	 * @throws NullPointerException Throw this exception if one the arguments is null, or the value in {@code grid}
	 * at the cell {@code coordinates} is null
	 * @throws InvalidParameterException Throw this exception if the value in {@code grid} at the cell
	 * {@code coordinates} is unknown
	 */
	private void update(@NotNull Grid grid, @NotNull Point coordinates) throws NullPointerException, InvalidParameterException {
		if (grid == null || coordinates == null)
			throw new NullPointerException();
		
		if (grid.get(coordinates) == ECell.CROSS)
			numberOfCell1++;
		else if (grid.get(coordinates) == ECell.CIRCLE)
			numberOfCell2++;
		else if (grid.get(coordinates) == ECell.EMPTY)
			numberOfEmptyCell++;
		else if (grid.get(coordinates) == null)
			throw new NullPointerException();
		else
			throw new InvalidParameterException();
	}
	/**
	 * Update the attributes {@code numberOfCell1}, {@code numberOfCell2} and {@code numberOfEmptyCell} according to the
	 * value(s) of {@code grid} and the point ({@code x} ; {@code y})
	 * @param grid The grid
	 * @param x The coordinate x to parse
	 * @param y The coordinate y to parse
	 * @throws NullPointerException Throw this exception if one the arguments is null, or the value in {@code grid}
	 * at the cell ({@code x} ; {@code y}) is null
	 * @throws InvalidParameterException Throw this exception if the value in {@code grid} at the cell
	 * ({@code x} ; {@code y}) is unknown
	 */
	private void update(Grid grid, int x, int y) throws NullPointerException, InvalidParameterException {
		update(grid, new Point(x, y));
	}
	
	/* GETTERS & SETTERS */
	
	public Point getPoint() {
		return point;
	}
	
	public void setPoint(Point point) {
		this.point = point;
	}
	
	public int getNumberOfCell2() {
		return numberOfCell2;
	}
	
	public void setNumberOfCell2(int numberOfCell2) {
		this.numberOfCell2 = numberOfCell2;
	}
	
	public int getNumberOfCell1() {
		return numberOfCell1;
	}
	
	public void setNumberOfCell1(int numberOfCell1) {
		this.numberOfCell1 = numberOfCell1;
	}
	
	public int getNumberOfEmptyCell() {
		return numberOfEmptyCell;
	}
	
	public void setNumberOfEmptyCell(int numberOfEmptyCell) {
		this.numberOfEmptyCell = numberOfEmptyCell;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
}
