package GameMain;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author dejohnso
 *
 */
public class Sprite {

	private double speed;
	private Random rand;
	private ArrayList<BufferedImage> images;
	public Point2D.Double position;
	private Point2D.Double hitbox;

	/**
	 * A sprite is a sequence of images. A new Sprite always has at least one image
	 * (passed into the constructor). There is an assumption that these images are
	 * all the same size.
	 * 
	 * @param startingPosition the upper left corner of the image on the screen.
	 * @param initHitbox       the width and height of the rectangle to be used for
	 *                         collision detection.
	 * @param img
	 */
	public Sprite(Point2D.Double startingPosition, Point2D.Double initHitbox, BufferedImage img, double speed) {
		position = startingPosition;
		this.speed = speed;
		hitbox = initHitbox;
		images = new ArrayList<BufferedImage>();
		images.add(img);
		rand = new Random();
	}

	/***
	 * Add img to the ArrayList of images
	 * 
	 * @param img
	 */
	public void add(BufferedImage img) {
		images.add(img);
	}

	public static void attack(ArrayList<Shot> shots, ArrayList<Zombie> zombies) {
		ArrayList<Shot> tempShot = new ArrayList<>();

		for (Shot shot : shots) {
			for (Zombie z : zombies) {
				if (z.isCollidingOther(shot) && shot.isFreezer) {
					shot.setColliding(true);
					z.setColliding(true);
					z.isFrozen = true;
					z.hasBeenFrozen = true;
					z.setCooldown(50);
					z.hit();
					//System.out.println("shot hit");
				} else if (z.isCollidingOther(shot)) {
					shot.setColliding(true);
					z.setColliding(true);
					z.hit();
				}
			}
			if (!shot.isColliding) {
				tempShot.add(shot);
			}
		}
		GameRun.setSHOTS(tempShot);
	}

	/**
	 * Get the image at frameNumber. If frameNumber would be out-of-bounds then mod
	 * it with the number of images.
	 * 
	 * The idea is that we can track the overall frame count of the game, pass it
	 * in, and this would cycle through the sprite images.
	 * 
	 * @param frameNumber
	 * @return the image at frameNumber mod the number of frames.
	 */
	public BufferedImage get(int frameNumber) {
		return images.get(frameNumber % images.size());
	}

	public Point2D.Double getPosition() {
		return position;
	}

	// Moves the sprite forward by 2 pixels
	public void move() {
		position.setLocation(position.getX() + speed, position.getY());
	}

	public void stop() {
		position.setLocation(position.getX(), position.getY());
	}

	// Moves the sprite down
	public void moveVertical() {
		position.setLocation(position.getX(), position.getY() + speed);
	}

	public void moveTo(Point2D.Double newPosition) {
		position = newPosition;
	}

	public void shiftPosition(Point2D.Double offset) {
		position.setLocation(position.getX() + offset.getX(), position.getY() + offset.getY());
	}

	public Point2D.Double getHitbox() {
		return hitbox;
	}

	public void draw(Graphics g, int frameNumber) {
		g.drawImage(get(frameNumber), (int) position.getX(), (int) position.getY(), null);
//		System.out.println("redraw " + frameNumber);
	}

	/**
	 * See if point is within the rectangle from position to position + hitbox
	 * 
	 * @param point
	 * @return true if the point is in the rectangle and false otherwise.
	 */
	public boolean isCollidingPoint(Point2D.Double point) {
		return point.getX() >= position.getX() && point.getX() <= position.getX() + hitbox.getX() && // within x range
				point.getY() >= position.getY() && point.getY() <= position.getY() + hitbox.getY(); // within y range
	}

	public boolean isCollidingOther(Sprite other) {
		// See if this rectangle is above the other
		if (position.getY() + hitbox.getY() < other.position.getY())
			return false;
		// See if this rectangle is below the other
		if (position.getY() > other.position.getY() + other.hitbox.getY())
			return false;

		// See if this rectangle is left of the other
		if (position.getX() + hitbox.getX() < other.position.getX())
			return false;
		// See if this rectangle is right of the other
		if (position.getX() > other.position.getX() + other.hitbox.getX())
			return false;

		// If it is not above or below or left or right of the other, it is colliding.
		return true;
	}

	public static void main(String[] args) {

	}

}
