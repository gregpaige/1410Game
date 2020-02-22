package GameMain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class NPC extends Sprite {

	private int health;
	private int fullHealth;
	private int coolDownCounter;
	private int coolDown;
	private double speed;
	private boolean isZomb;
	private boolean isColliding;

	public NPC(Point2D.Double startingPosition, Point2D.Double initHitbox, BufferedImage img, double speed, int health,
			int coolDown) {
		super(startingPosition, initHitbox, img, speed);
		this.health = health;
		this.fullHealth = health;
		this.coolDownCounter = coolDown;
		this.coolDown = coolDown;
		this.speed = speed;
		isColliding = false;
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

	public void changeHealth(int change) {
		health += change;
	}

	// Distinguishes between zombies and plants
	public boolean getZombStat() {
		return isZomb;
	}

	public boolean isAlive() {
		return health > 0;
	}


	public void hit() {
		health -= 20;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
