package fr.polytech.tp3.morpion.game;

public interface GameListener {
	
	void onStateChanged(EState oldState, EState newState);
	void onTokenChanged(ECell oldToken, ECell newToken);
	void onNbGameChanged(int nbGame);
}
