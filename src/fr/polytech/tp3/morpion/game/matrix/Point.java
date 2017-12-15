package fr.polytech.tp3.morpion.game.matrix;

import java.util.Objects;

public class Point extends Couple<Integer, Integer> {
	
	public Point() {
		setX(0);
		setY(0);
	}
	public Point(int x, int y) {
		setX(x);
		setY(y);
	}
	
	/* OVERRIDES */
	@Override
	public String toString() {
		return "Point{" +
				"x=" + getX().toString() +
				", y=" + getY().toString() +
				'}';
	}
}
