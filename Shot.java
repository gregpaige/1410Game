package GameMain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.imageio.ImageIO;

public class Shot extends Sprite {
	private ArrayList<BufferedImage> images;
	private static Point2D.Double startPosition;
	private static Point2D.Double hitbox;
	private int coolDownCounter;
	private int coolDown;
	public static BufferedImage img;
	public boolean isColliding;
	public boolean isFreezer;
	
	static { 
		BufferedImage img2 = null;
		try {
			img2 = ImageIO.read(new File("src/GameMain/Spit.png"));
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		img = img2;
	}
	
	public Shot(Point2D.Double startPosition, Point2D.Double hitbox, int speed, int coolDown, boolean isFrozen) {
		super(startPosition, hitbox, img, speed);
		this.coolDown = coolDown;
		this.startPosition = startPosition;
		this.hitbox = hitbox;
		this.img = img;
		isFreezer = isFrozen;
		images = new ArrayList<BufferedImage>();
		images.add(img);
	}
	
	
	public void add(BufferedImage img) {
		images.add(img);
	}

	public BufferedImage get(int frameNumber) {
		return images.get(frameNumber % images.size());
	}

	public Point2D.Double getPosition() {
		return startPosition;
	}

	public void setColliding(boolean collisionStatus) {
		isColliding = collisionStatus;
	}

	public boolean getColliding() {
		return isColliding;
	}

	public void update() {
		isColliding = false;
		coolDownCounter--;
	}

	public boolean readyForAction() {
		if (coolDownCounter <= 0)
			return true;
		return false;
	}

	public void resetCoolDown() {
		coolDownCounter = coolDown;
	}

}