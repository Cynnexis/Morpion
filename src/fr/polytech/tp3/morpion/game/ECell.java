package fr.polytech.tp3.morpion.game;

public enum ECell {
	EMPTY(' '),
	CROSS('x'),
	CIRCLE('o');

	private char character = 0;

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
