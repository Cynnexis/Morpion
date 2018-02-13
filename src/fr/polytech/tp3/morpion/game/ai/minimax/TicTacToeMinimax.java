package fr.polytech.tp3.morpion.game.ai.minimax;

import com.sun.deploy.util.ArgumentParsingUtil;
import com.sun.istack.internal.NotNull;
import fr.berger.enhancedlist.tree.Node;
import fr.polytech.tp3.morpion.game.ECell;
import fr.polytech.tp3.morpion.game.Game;
import fr.polytech.tp3.morpion.game.Grid;
import fr.polytech.tp3.morpion.game.matrix.Point;

import java.util.ArrayList;

public class TicTacToeMinimax extends AbstractMinimax<Grid> {
	
	public @NotNull
	Node<Grid> constructNode(@NotNull Grid grid, @NotNull ECell token, int depth) {
		// Add one to depth because thr root is 'null'
		return constructNode(new Node<>(grid), grid, token, ++depth);
	}
	public @NotNull Node<Grid> constructNode(@NotNull Node<Grid> node, @NotNull Grid grid,
	                                         @NotNull ECell token, int depth) {
		if (depth <= 0)
			return null;
		
		if (node == null || grid == null || token == null)
			throw new NullPointerException();
		
		if (token == ECell.EMPTY)
			throw new IllegalArgumentException();
		
		ArrayList<Point> available = grid.getAvailableCells();
		
		if (available.size() > 0) {
			for (Point p : available) {
				Grid copy = new Grid(grid);
				
				copy.set(p, token);
				
				Node<Grid> child = new Node<>(copy);
				if (depth > 1)
					node.addChild(constructNode(child, copy, token == ECell.CROSS ? ECell.CIRCLE : ECell.CROSS, depth - 1));
			}
		}
		
		return node;
	}
	
	@Override
	public int compute(@NotNull Grid data) {
		if (data == null)
			throw new NullPointerException();
		
		// Search for a winner
		switch (Game.checkWin(data))
		{
			case CROSS:
				return -10;
			case CIRCLE:
				return 10;
			default: // EMPTY => Draw or not finished yet
				return 0;
		}
	}
}
