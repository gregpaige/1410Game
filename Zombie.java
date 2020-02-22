package GameMain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Zombie extends NPC {

	public int health;
	private int fullHealth;
	private int coolDown;
	private int originalCoolDown;
	protected boolean isNormalZomb;
	public double speed;
	private boolean remove = false;
	private static BufferedImage img;
	private boolean isColliding;
	public boolean isFrozen;
	public boolean hasBeenFrozen;
	public static int i = 0;

//	static {
//		BufferedImage img2 = null;
//		try {
//			img2 = ImageIO.read(new File("src/GameMain/zombie.gif"));
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		img = img2;
//	}

	public Zombie(Point2D.Double startingPosition, Point2D.Double initHitbox, double speed, int health, int coolDown,
			BufferedImage img, boolean isNormalZomb) {
		super(startingPosition, initHitbox, img, speed, health, coolDown);
		this.health = health;
		this.fullHealth = health;
		this.coolDown = coolDown;
		this.originalCoolDown = coolDown;
		this.speed = speed;
		this.isNormalZomb = isNormalZomb;
		isColliding = false;
		isFrozen = false; 
		hasBeenFrozen = false;
	}

//	public boolean getMoving() {
//		return isMoving;
//	}

	// Checks for when a Zombie is attacking a Plant
	// Hits the plant and stop the zombie
	public static void attackPlant(ArrayList<Plant> plants, ArrayList<Zombie> zombies) {
		ArrayList<Plant> tempPlant = new ArrayList<>();
		
		for (Plant plant : plants) {
			for (Zombie zomb : zombies) {
				if (zomb.isCollidingOther(plant) && zomb.isNormalZomb) {
					zomb.isFrozen = true;
					i++;
					//zomb.hasBeenFrozen = true;
					plant.hit(1);
					//System.out.println("plant hit by NORMAL " + zomb.isFrozen);
				}
			}
			if (!plant.getColliding()) {
				tempPlant.add(plant);
				// System.out.println(tempPlant.size());
			}
			
		}
		//System.out.println(i);
		GameRun.setPLANTS(tempPlant);

	}

	public static void attackHome(Home h, ArrayList<Zombie> zombies) {
		for (Zombie zomb : zombies) {
			if (zomb.isCollidingOther(h) && zomb.isNormalZomb) {
				h.hit(10);
				zomb.isFrozen = true;
				//zomb.hasBeenFrozen = true;
				//System.out.println("Zomb attacking house");
			}

		}
	}

	public void setCooldown(int cooldown) {
		this.coolDown = cooldown;
	}

	public int getCooldown() {
		return coolDown;
	}
	

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void setColliding(boolean collisionStatus) {
		isColliding = collisionStatus;
	}

	public void setRemove(boolean newRemove) {
		remove = newRemove;
	}

	public boolean getRemove() {
		return remove;
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
		}else
			//System.out.println("READY");
		return false;
	}

	public void resetCoolDown() {
		this.coolDown = originalCoolDown;
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

		// Only draws HealthBar for the zombies
		g.setColor(Color.BLACK);
		g.drawRect((int) pos.getX(), (int) pos.getY(), (int) box.getX(), 2);
		g.setColor(Color.RED);
		g.fillRect((int) pos.getX(), (int) pos.getY(), (int) (box.getX() * this.health / (double) this.fullHealth), 2);
	}

	public void hit() {
		health -= 20;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
