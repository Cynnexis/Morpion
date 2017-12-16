package fr.polytech.tp3.morpion.gui;

import fr.polytech.tp3.morpion.game.matrix.Point;

import javax.swing.*;

public class ArrangedButton extends JButton {
	
	private Point coordinates = new Point();
	
	public ArrangedButton() {
		super();
	}
	public ArrangedButton(String name) {
		super(name);
	}
	public ArrangedButton(Point coordinates) {
		super();
		setCoordinates(coordinates);
	}
	public ArrangedButton(int x, int y) {
		super();
		setCoordinates(x, y);
	}
	public ArrangedButton(String name, Point coordinates) {
		super(name);
		setCoordinates(coordinates);
	}
	public ArrangedButton(String name, int x, int y) {
		super(name);
		setCoordinates(x, y);
	}
	
	/* GETTER & SETTER */
	
	public Point getCoordinates() {
		return coordinates;
	}
	
	public void setCoordinates(Point coordinates) {
		this.coordinates = coordinates;
	}
	public void setCoordinates(int x, int y) {
		this.coordinates = new Point(x, y);
	}
}
