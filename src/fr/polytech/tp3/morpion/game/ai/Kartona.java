package fr.polytech.tp3.morpion.game.ai;

import com.sun.javafx.scene.web.Debugger;
import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.exceptions.InfiniteLoopException;
import fr.berger.enhancedlist.tree.Node;
import fr.polytech.tp3.morpion.game.ECell;
import fr.polytech.tp3.morpion.game.Grid;
import fr.polytech.tp3.morpion.game.ai.minimax.TicTacToeMinimax;
import fr.polytech.tp3.morpion.game.exceptions.GridFullException;
import fr.polytech.tp3.morpion.game.exceptions.PointNotInDiagonal;
import fr.polytech.tp3.morpion.game.matrix.Point;
import javafx.util.Callback;

import java.util.*;

/**
 * Kartona is an Artifical Intelligence.
 * @author Valentin Berger
 * @see Intelligence
 * @see Situation
 */
public class Kartona {
	
	private Intelligence intelligence = Intelligence.HARD;
	
	/**
	 * Constructor of Kartona
	 * @param intelligence THe itelligence of Kartona
	 */
	public Kartona(Intelligence intelligence) {
		setIntelligence(intelligence);
	}
	
	/**
	 * Compute a point according to the grid {@code currentGrid} to win the Tic Tac Toe game
	 * @param currentGrid The current grid
	 * @return Return the point that Kartona chose
	 * @throws GridFullException Throw this exception if {@code grid} is already full
	 */
	public Point thinkAboutMove(Grid currentGrid) throws GridFullException {
		Point coordinates = new Point();
		
		switch (intelligence)
		{
			case EASY:
				Random rand = new Random();
				boolean empty = true;
				int iterations = 0;
				int limit = 1000; // 1000 iterations max
				
				do {
					coordinates.setX((int) (rand.nextInt((2 - 0) + 1) + 0));
					coordinates.setY((int) (rand.nextInt((2 - 0) + 1) + 0));
					empty = currentGrid.get(coordinates) == ECell.EMPTY;
					iterations++;
				} while (!empty && iterations <= limit);
				
				if (iterations >= limit)
					throw new GridFullException();
				break;
			case HARD:
				ArrayList<Point> available = currentGrid.getAvailableCells();
				ArrayList<Situation> situations = new ArrayList<>(4 * available.size());
				
				if (available != null && available.size() > 0) {
					try {
						// For all available point, compute the situation
						for (Point p : available) {
							situations.add(new Situation(currentGrid, p, Situation.Type.COLUMN));
							situations.add(new Situation(currentGrid, p, Situation.Type.ROW));
							try {
								situations.add(new Situation(currentGrid, p, Situation.Type.DIAGONAL_INC));
							} catch (PointNotInDiagonal ignored) { }
							try {
								situations.add(new Situation(currentGrid, p, Situation.Type.DIAGONAL_DEC));
							} catch (PointNotInDiagonal ignored) { }
						}
						
						sortAsc(situations, new Comparator<Situation>() {
							@Override
							public int compare(Situation s1, Situation s2) {
								// s1 < s2 <=> s1 - s2 < 0
								// Compute a kind of score using the attribute of Situation class
								return s1.getNumberOfCell2() - s2.getNumberOfCell2();
							}
						});
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					
					// The best solution is the last situations in the list. However, the program must check
					// if there is a solution where player one is about to win
					if (situations != null && situations.size() > 0) {
						boolean criticalSituation = false;
						for (int i = 0; i < situations.size() && !criticalSituation; i++) {
							if (situations.get(i).getNumberOfCell1() == 2) {
								coordinates = situations.get(i).getPoint();
								criticalSituation = true;
							}
						}
						
						if (!criticalSituation)
							coordinates = situations.get(situations.size() - 1).getPoint();
					}
				}
				break;
			case MINIMAX:
				TicTacToeMinimax minimax = new TicTacToeMinimax();
				Node<Grid> root = minimax.constructNode(currentGrid, ECell.CIRCLE, 10);
				
				Couple<Integer, Node<Grid>> result;
				try {
					result = minimax.minimax(root);
				} catch (InfiniteLoopException e) {
					e.printStackTrace();
				}
				
				break;
		}
		
		return coordinates;
	}
	
	/**
	 * Sort a list of any type {@code T} from the smallest to the largest
	 * @param list The list to sort
	 * @param comparator The comparator to campare two objects of type {@code T}
	 * @param <T> The type of the objects in the {@code list}
	 */
	public static <T> void sortAsc(ArrayList<T> list, Comparator<T> comparator) {
		int i = 0, imin = 0, j = 0;
		
		for (i = 0; i < list.size() - 1; i++)
		{
			imin = i;
			
			for (j = i + 1; j < list.size(); j++)
				if (comparator.compare(list.get(j), list.get(imin)) < 0)
					imin = j;
			
			swap(list, imin, i);
		}
	}
	
	/**
	 * Swamp two elements in a list
	 * @param list the list
	 * @param index1 the first index
	 * @param index2 the second index
	 */
	public static void swap(ArrayList<?> list, int index1, int index2) {
		Collections.swap(list, index1, index2);
	}
	
	/* GETTER & SETTER */
	
	public Intelligence getIntelligence() {
		return intelligence;
	}
	
	public void setIntelligence(Intelligence intelligence) {
		this.intelligence = intelligence;
	}
}
