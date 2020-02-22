package GameMain;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import GameMain.GameRun.STATE;

public class Menu {

	public Rectangle playButton = new Rectangle(GameRun.WIDTH / 2 - 100, 200, 200, 50);

	// public Rectangle quitButton = new Rectangle(0, 205 , 100, 50);

	public void render(Graphics g) {
		// Graphics2D g2d = (Graphics2D) g;

		Font fnt0 = new Font("arial", Font.BOLD, 25);
		g.setFont(fnt0);
		g.setColor(Color.white);
		if (GameRun.State != STATE.DONE)
			g.drawString("Plants vs Zombies", GameRun.WIDTH / 2 - 100, 80);
		else {
			g.drawString("GAME OVER", GameRun.WIDTH / 2 - 75, 25);
		}
		Font fnt1 = new Font("arial", Font.BOLD, 25);
		g.setFont(fnt1);

//		if ( GameRun.State == GameRun.STATE.START)
//			g.drawString("START", playButton.x + 50 , playButton.y + 30);
//		
//		g2d.draw(playButton);
//		g.drawString("Quit", quitButton.x + 19, quitButton.y + 30);
//		g2d.draw(quitButton);
	}
}
