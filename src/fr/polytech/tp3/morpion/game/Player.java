package fr.polytech.tp3.morpion.game;

import fr.polytech.tp3.morpion.game.matrix.Point;

import java.util.Objects;
import java.util.Scanner;

public class Player {

	private String name = "";
	private ECell type = ECell.CROSS;
	private int nbWin = 0;
	private int nbGame = 0;
	
	public Player(String name, ECell type) {
		setName(name);
		setType(type);
	}
	public Player(ECell type) {
		setType(type);
	}
	
	public Point play(int nbColumns, int nbRows) {
		Point coordinates = new Point();
		Scanner sc = new Scanner(System.in);
		boolean error = true;
		
		do {
			System.out.println(name + ", choose a coordinate x between 1 and " + nbColumns + ":");
			coordinates.setX(sc.nextInt());
			error = coordinates.getX() < 1 || coordinates.getX() > nbColumns;
			if (error)
				System.out.println("Sorry, this coordinate is not valid.");
		} while(error);
		
		do {
			System.out.println(name + ", choose a coordinate y between 1 and " + nbRows + ":");
			coordinates.setY(sc.nextInt());
			error = coordinates.getY() < 1 || coordinates.getY() > nbRows;
			if (error)
				System.out.println("Sorry, this coordinate is not valid.");
		} while(error);
		
		// Changing the base of the coordinate: [(1, 1); (nbColumns, nbRows)] becomes [(0, 0); (nbColumns-1, nbRows-1)]
		coordinates.setX(coordinates.getX() - 1);
		coordinates.setY(coordinates.getY() - 1);
		
		return coordinates;
	}
	
	public void incrementNbGame() {
		nbGame++;
	}
	
	public void incrementNbGameAndNbWin() {
		nbWin++;
	}
	
	/* GETTERS & SETTERS */
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ECell getType() {
		return type;
	}
	
	public void setType(ECell type) {
		if (type != null && type != ECell.EMPTY)
			this.type = type;
	}
	
	public int getNbWin() {
		return nbWin;
	}
	
	public void setNbWin(int nbWin) {
		if (nbGame >= 0)
			this.nbWin = nbWin;
	}
	
	public int getNbGame() {
		return nbGame;
	}
	
	public void setNbGame(int nbGame) {
		if (nbGame >= 0)
			this.nbGame = nbGame;
	}
	
	/* OVERRIDES */
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Player)) return false;
		Player player = (Player) o;
		return getNbWin() == player.getNbWin() &&
				getNbGame() == player.getNbGame() &&
				Objects.equals(getName(), player.getName()) &&
				getType() == player.getType();
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(getName(), getType(), getNbWin(), getNbGame());
	}
}
