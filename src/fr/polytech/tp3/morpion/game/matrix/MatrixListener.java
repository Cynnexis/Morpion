package fr.polytech.tp3.morpion.game.matrix;

import java.util.EventListener;

public interface MatrixListener<T> {
	
	void OnCellChanged(int x, int y, T value);
}
