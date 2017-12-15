package fr.polytech.tp3.morpion.game;

import fr.polytech.tp3.morpion.game.matrix.Matrix;

public class Grid extends Matrix<ECell> {
	
	public Grid(int nbColumns, int nbRows) {
		super(nbColumns, nbRows, ECell.EMPTY);
		
	}
	public Grid() {
		super(3, 3, ECell.EMPTY);
	}
	
	@Override
	public void clear() {
		init(getNbColumns(), getNbRows(), ECell.EMPTY);
	}
	
	public boolean isEmpty() {
		boolean empty = true;
		
		for (int i = 0, imax = getNbColumns(); i < imax && empty; i++)
			for (int j = 0, jmax = getNbRows(); j < jmax && empty; j++)
				if (get(i, j) != ECell.EMPTY)
					empty = false;
		
		return empty;
	}
	
	public boolean isFull() {
		boolean full = true;
		
		for (int i = 0, imax = getNbColumns(); i < imax && full; i++)
			for (int j = 0, jmax = getNbRows(); j < jmax && full; j++)
				if (get(i, j) == ECell.EMPTY)
					full = false;
		
		return full;
	}
	
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
