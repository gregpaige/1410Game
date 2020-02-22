package GameMain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.imageio.ImageIO;

public class FreezePlant extends Plant {

	private int health;
	private int fullHealth;
	private int coolDownCounter;
	private int coolDown;
	private boolean isColliding;
	public static BufferedImage img;

	// public ScheduledExecutorService scheduler =
	// Executors.newScheduledThreadPool(1);
	// public boolean isShooting = false;

	static {
		BufferedImage img2 = null;
		try {
			img2 = ImageIO.read(new File("src/GameMain/Shooter.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		img = img2;
	}

	public static void attack(ArrayList<Shot> shots, ArrayList<Zombie> zombies) {
		ArrayList<Shot> tempShot = new ArrayList<>();

		for (Shot shot : shots) {
			for (Zombie z : zombies) {
				if (z.isCollidingOther(shot)) {
					shot.setColliding(true);
					z.hit();

				}
			}
			if (!shot.isColliding) {
				tempShot.add(shot);
				// System.out.println("ADDED");
				// System.out.println(tempShot.size());
			}
		}
		GameRun.setSHOTS(tempShot);
	}

	public FreezePlant(Double startingPosition, Double initHitbox, int speed, int health, int coolDown,
			int attackDamage, boolean isFreezer) {
		super(startingPosition, initHitbox, speed, health, coolDown, attackDamage, isFreezer, img);
		this.health = health;
		this.fullHealth = health;
		this.coolDownCounter = coolDown;
		this.coolDown = coolDown;
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