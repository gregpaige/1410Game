package GameMain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ExploderZombie extends Zombie {

	private int health;
	private int fullHealth;
	private int originalCoolDown;
	private int coolDown;
	public double speed;
	private boolean remove;
	private boolean isNormalZomb;
	public static BufferedImage img;
	private boolean isColliding;

	static {
		BufferedImage img2 = null;
		try {
			img2 = ImageIO.read(new File("src/GameMain/Exploder.gif"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		img = img2;
	}

	public ExploderZombie(Point2D.Double startingPosition, Point2D.Double initHitbox, double speed, int health,
			int coolDown, boolean isNormalZomb) {
		super(startingPosition, initHitbox, speed, health, coolDown, img, isNormalZomb);
		this.health = health;
		this.fullHealth = health;
		this.originalCoolDown = coolDown;
		this.coolDown = coolDown;
		this.speed = speed;
		this.isNormalZomb = isNormalZomb;
		isColliding = false;
		remove = false;

	}

	// Instead of hitting the plant slowly by one, exploding zombie completely
	// erases a plant, but then vanishes

	public static void attackPlant(ArrayList<Plant> plants, ArrayList<Zombie> zombies) {
		ArrayList<Plant> tempPlant = new ArrayList<>();

		for (Plant plant : plants) {
			for (Zombie zomb : zombies) {
				if (zomb.isCollidingOther(plant) && !zomb.isNormalZomb) {
					plant.hit(plant.getHealth());
					zomb.isFrozen = true;
					zomb.setRemove(true);
					//System.out.println("plant hit by EXPLODER");
				}
			}
			if (!plant.getColliding()) {
				tempPlant.add(plant);
				// System.out.println(tempPlant.size());
			}
		}

		GameRun.setPLANTS(tempPlant);

	}

	public static void attackHome(Home h, ArrayList<Zombie> zombies) {
		for (Zombie zomb : zombies) {
			if (zomb.isCollidingOther(h) && !zomb.isNormalZomb) {
				h.hit(10);
				zomb.isFrozen = true;
				//System.out.println(" Exploder Zomb attacking house");
			}

		}
	}

	public void setCooldown(int cooldown) {
		this.coolDown = cooldown;
	}

	public int getCooldown() {
		return coolDown;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void setColliding(boolean collisionStatus) {
		isColliding = collisionStatus;
	}

	public boolean getColliding() {
		return isColliding;
	}

	public void update() {
		isColliding = false;
		coolDown--;
	}

	public boolean readyForAction() {
		if (coolDown <= 0) {
			return true;
		} else
			System.out.println("READY");
		return false;
	}

	public void resetCoolDown() {
		coolDown = originalCoolDown;
	}

	public void changeHealth(int change) {
		health += change;
	}

	public boolean isAlive() {
		return health > 0;
	}

	public void setRemove(boolean newRemove) {
		remove = newRemove;
	}

	public boolean getRemove() {
		return remove;
	}

	public void drawHealthBar(Graphics g) {
		Point2D.Double pos = this.getPosition();
		Point2D.Double box = this.getHitbox();

		// Only draws HealthBar for the zombies
		g.setColor(Color.BLACK);
		g.drawRect((int) pos.getX(), (int) pos.getY(), (int) box.getX(), 2);
		g.setColor(Color.RED);
		g.fillRect((int) pos.getX(), (int) pos.getY(), (int) (box.getX() * this.health / (double) this.fullHealth), 2);
	}

	public void hit() {
		this.health -= 20;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
