package fr.polytech.tp3.morpion.game;

/**
 * Enumerate the different type of the Tic Tac Toe
 * @author Valentin Berger
 * @see Grid
 */
public enum ECell {
	EMPTY(' '),
	CROSS('x'),
	CIRCLE('o');
	
	/**
	 * Representation of the type in character
	 */
	private char character = 0;
	
	/**
	 * Construct the type
	 * @param character The representation of the type in character
	 */
	ECell(char character) {
		setCharacter(character);
	}
	
	public char getCharacter() {
		return character;
	}
	
	private void setCharacter(char character) {
		this.character = character;
	}
	
	@Override
	public String toString() {
		return Character.toString(getCharacter());
	}
}
