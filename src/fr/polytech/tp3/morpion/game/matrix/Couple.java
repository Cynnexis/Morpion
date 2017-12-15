package fr.polytech.tp3.morpion.game.matrix;

import java.util.Objects;

public class Couple<T, U> {
	
	private T x;
	private U y;
	
	public Couple() {
		x = null;
		y = null;
	}
	public Couple(T x, U y) {
		setX(x);
		setY(y);
	}
	
	/* GETTERS & SETTERS */
	
	public T getX() {
		return x;
	}
	
	public void setX(T x) {
		this.x = x;
	}
	
	public U getY() {
		return y;
	}
	
	public void setY(U y) {
		this.y = y;
	}
	
	/* OVERRIDES */
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Couple)) return false;
		Couple<?, ?> couple = (Couple<?, ?>) o;
		return Objects.equals(getX(), couple.getX()) &&
				Objects.equals(getY(), couple.getY());
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(getX(), getY());
	}
	
	@Override
	public String toString() {
		return "Couple{" +
				"x=" + x.toString() +
				", y=" + y.toString() +
				'}';
	}
}
