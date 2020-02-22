package GameMain;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import GameMain.GameRun.STATE;

public class GameKeyListener extends KeyAdapter implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
		int keycode = e.getKeyCode();
		System.out.println(keycode);
		// Pauses the game if the escape button is pressed and the state is game
		if (GameRun.State == STATE.GAME && keycode == KeyEvent.VK_ESCAPE) {
			GameRun.State = STATE.PAUSE;
			System.out.println("GAME PAUSED");
		} else if (GameRun.State == STATE.PAUSE && keycode == KeyEvent.VK_ESCAPE) { // If the game is paused,
			System.out.println("GAME RESUMED");																// unpause when esc pressed
			GameRun.State = STATE.GAME;
		}
	}
	
	public void main(String[] args) {
		System.out.println("listening");
	}
	
}
