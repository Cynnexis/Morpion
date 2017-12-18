package fr.polytech.tp3.morpion.game;

import com.sun.istack.internal.NotNull;
import fr.polytech.tp3.morpion.game.matrix.Point;

import java.util.Objects;
import java.util.Scanner;

/**
 * Player class
 * @author Valentin Berger
 * @see PlayerListener
 * @see Game
 * @see ECell
 */
public class Player {
	
	/**
	 * The player's name.
	 */
	private String name = "";
	/**
	 * The player's type. By default, it is 'x'
	 */
	private ECell type = ECell.CROSS;
	/**
	 * The number of wins
	 */
	private int nbWin = 0;
	/**
	 * The listener of this class. It cannot be null.
	 */
	@NotNull
	private	PlayerListener playerListener = new PlayerListener() {
		@Override
		public void onNameChanged(String name) { }
		@Override
		public void onTypeChanged(ECell type) { }
		@Override
		public void onNbWinChanged(int nbWin) { }
	};
	
	public Player(String name, ECell type) {
		setName(name);
		setType(type);
	}
	public Player(ECell type) {
		setType(type);
	}
	
	public void incrementNbWin() {
		setNbWin(getNbWin() + 1);
	}
	
	/* GETTERS & SETTERS */
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		if (name != null && !name.isEmpty()) {
			this.name = name;
			playerListener.onNameChanged(name);
		}
	}
	
	public ECell getType() {
		return type;
	}
	
	public void setType(ECell type) {
		if (type != null && type != ECell.EMPTY) {
			this.type = type;
			playerListener.onTypeChanged(type);
		}
	}
	
	public int getNbWin() {
		return nbWin;
	}
	
	public void setNbWin(int nbWin) {
		if (nbWin >= 0) {
			this.nbWin = nbWin;
			playerListener.onNbWinChanged(nbWin);
		}
	}
	
	public PlayerListener getPlayerListener() {
		return playerListener;
	}
	
	public void setPlayerListener(PlayerListener playerListener) {
		this.playerListener = playerListener != null ? playerListener : new PlayerListener() {
			@Override
			public void onNameChanged(String name) { }
			@Override
			public void onTypeChanged(ECell type) { }
			@Override
			public void onNbWinChanged(int nbWin) { }
		};
	}
	
	/* OVERRIDES */
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Player)) return false;
		Player player = (Player) o;
		return getNbWin() == player.getNbWin() &&
				Objects.equals(getName(), player.getName()) &&
				getType() == player.getType();
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(getName(), getType(), getNbWin());
	}
}
