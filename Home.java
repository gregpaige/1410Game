package GameMain;

import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Home extends NPC {
	public static BufferedImage img;
	public int health;
	
	static {
		BufferedImage img2 = null;
		try {
			img2 = ImageIO.read(new File("src/GameMain/Home.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		img = img2;
	}
	
	public Home(Double startingPosition, Double initHitbox, int speed, int health, int coolDown) {
		super(startingPosition, initHitbox, img, speed, health, coolDown);
		this.health = health;
	}
	
	
	public void hit(int dmg) {
		health -= dmg;
	}
	
}
