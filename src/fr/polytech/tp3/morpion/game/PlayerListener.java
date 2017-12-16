package fr.polytech.tp3.morpion.game;

public interface PlayerListener {
	
	void onNameChanged(String name);
	
	void onTypeChanged(ECell type);
	
	void onNbWinChanged(int nbWin);
	
	void onNbGameChanged(int nbGame);
}
