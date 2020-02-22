package GameMain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.imageio.ImageIO;

public class Plant extends NPC {

	private int health;
	private int fullHealth;
	private int coolDownCounter;
	private int coolDown;
	private boolean isColliding;
	public static BufferedImage img;
	public boolean isFreezer;
	
	//public ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	//public boolean isShooting = false;
	
	static { 
		BufferedImage img2 = null;
		try {
			img2 = ImageIO.read(new File("src/GameMain/GreenShooter.png"));
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		img = img2;
	}
	
	public Plant(Double startingPosition, Double initHitbox, int speed, int health, int coolDown,
			int attackDamage, boolean isFreezer, BufferedImage img) {
		super(startingPosition, initHitbox, img, speed, health, coolDown);
		this.health = health;
		this.fullHealth = health;
		this.coolDownCounter = coolDown;
		this.coolDown = coolDown;
		this.isFreezer = isFreezer;
		isColliding = false;
	}

	@Override
	public void draw(Graphics g, int frameNumber) {
		g.drawImage(get(frameNumber), (int) position.getX(), (int) position.getY(), null);
//		System.out.println("redraw " + frameNumber);
	}

	public void setColliding(boolean collisionStatus) {
		isColliding = collisionStatus;
	}

	public boolean getColliding() {
		return isColliding;
	}
	public int getHealth() {
		return health;
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

	public void changeHealth(int change) {
		health += change;
	}

	public boolean isAlive() {
		return health > 0;
	}

	public void drawHealthBar(Graphics g) {
		Point2D.Double pos = this.getPosition();
		Point2D.Double box = this.getHitbox();

		g.setColor(Color.BLACK);
		g.drawRect((int) pos.getX(), (int) pos.getY(), (int) box.getX(), 2);
		g.setColor(Color.RED);
		g.fillRect((int) pos.getX(), (int) pos.getY(), (int) (box.getX() * this.health / (double) this.fullHealth), 2);
	}

	public void hit(int dmg) {
		health -= dmg;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}