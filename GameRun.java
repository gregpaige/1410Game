package GameMain;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameRun extends JPanel implements ActionListener, MouseListener, KeyListener {

	private static final long serialVersionUID = 1L;

	private static ArrayList<Zombie> ZOMBIES;
	private static ArrayList<Sprite> SUNRAYS;
	public static ArrayList<Shot> SHOTS;
	private static ArrayList<Plant> PLANTS;
	public Home h = new Home(new Point2D.Double(0, 0), new Point2D.Double(Home.img.getWidth(), Home.img.getHeight()), 0,
			1000, 0);

	private Timer timer;
	private Random rand;

	public static int WIDTH = 500;
	public static int HEIGHT = 300;

	private int spacing;
	private int sunScore;
	public int zombLevelHealth;
	public int exploderLevelHealth;
	public int numberOfZomb;
	private int zombKilled;
	public static int startX;
	public static int startY;
	
	Point2D.Double buyButtonSize;
	Point2D.Double pt;
	Point2D.Double point;

	private int row;
	private int col;


	int score;
	JLabel scoreLabel;
	JLabel infoLabel;
	JFrame jFrame;

	MouseListener mouse;
	Menu menu;

	public static enum STATE {
		START, GAME, PAUSE, BUY, FROZEN, DONE
	};

	public static enum LEVEL {
		FIRST, SECOND, THIRD
	};

	public static STATE State = STATE.START;
	public static LEVEL Level = LEVEL.FIRST;

	public GameRun() {
		System.out.println("PRESS ESC TO OPEN PAUSE MENU \nPRESS B TO OPEN BUY MENU");
		menu = new Menu();

		// How big characters are
		spacing = 50;
		startX = 50;
		startY = 0;
		zombLevelHealth = 100;
		exploderLevelHealth = 50;
		addMouseListener(this);
		point = new Point2D.Double();
		
		numberOfZomb = 0;
		zombKilled = 0;

		// Creates a keylistener for keyboard input
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(this);

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
//		setMinimumSize(new Dimension(WIDTH,HEIGHT));
//		setMaximumSize(new Dimension(WIDTH,HEIGHT));

		rand = new Random();
		score = 0;
		sunScore = 25;
		ZOMBIES = new ArrayList<>();
		SHOTS = new ArrayList<>();
		SUNRAYS = new ArrayList<>();
		PLANTS = new ArrayList<>();

		// Get the javax.swing Timer, not from util.
		timer = new Timer(30, this);
		timer.start();

		scoreLabel = new JLabel("Score: ");
		infoLabel = new JLabel("Press B to Open the Shop!");

		buyButtonSize = new Point2D.Double();
	}

	/***
	 * Implement the paint method to draw the ZOMBIES
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			BufferedImage img = ImageIO.read(new File("src/GameMain/SpaceBackground.png"));
			BufferedImage pauseImg = ImageIO.read(new File("src/GameMain/space.gif"));
			BufferedImage pauseButtonImg = ImageIO.read(new File("src/GameMain/ResumeButton.png"));
			BufferedImage startButtonImg = ImageIO.read(new File("src/GameMain/StartButton.png"));
			// BufferedImage shooterImg = ImageIO.read(new
			// File("src/GameMain/Shooter.png"));
			BufferedImage buyMenuImg = ImageIO.read(new File("src/GameMain/BuyMenu.png"));
			buyButtonSize.setLocation(buyMenuImg.getWidth(), buyMenuImg.getHeight());

			if (State == STATE.GAME || State == STATE.FROZEN) {
				g.drawImage(img, startX, 0, null);
				h.draw(g, 0);
				for (Plant plant : PLANTS) {
					plant.update();
					plant.draw(g, 0);
					plant.drawHealthBar(g);
				}
				for (Zombie zombie : ZOMBIES) {
					zombie.update();
					zombie.draw(g, 0);
					zombie.drawHealthBar(g);
				}
				for (Shot s : getSHOTS()) {
					s.update();
					s.draw(g, 0);

				}
				for (Sprite s : SUNRAYS) {
					s.draw(g, 0);
				}
			} else if (State == STATE.START) {
				// draws the background img and the startbutton img with the menu overlay
				g.drawImage(pauseImg, 0, 0, null);
				g.drawImage(startButtonImg, WIDTH / 2 - 100, 200, null);
				menu.render(g);
			} else if (State == STATE.PAUSE) {
				g.drawImage(pauseImg, 0, 0, null);
				g.drawImage(pauseButtonImg, WIDTH / 2 - 100, 200, null);
				menu.render(g);
			} else if (State == STATE.BUY) {
				g.drawImage(buyMenuImg, 0, 0, null);
				// g.drawImage(shooterImg, WIDTH / 2 - 100, 200, null);
				g.setFont(new Font("arial", Font.BOLD, 25));
				g.setColor(Color.black);
				g.drawString("SCORE:" + sunScore, WIDTH / 2 - 50, 100);
			} else if (State == STATE.DONE) {
				g.drawImage(pauseImg, 0, 0, null);
				menu.render(g);
				g.setFont(new Font("arial", Font.BOLD, 25));
				g.setColor(Color.white);
				g.drawString("ZOMBIES KILLED:" + zombKilled, WIDTH / 2 - 100, 75);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * This is triggered by the timer. It is the game loop of this test.
	 * 
	 * @param e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		int col = WIDTH;
		int row = rand.nextInt(6) * spacing;
		int sunCol = (rand.nextInt(9) * spacing);

		if (State == STATE.FROZEN) {
			scoreLabel.setText("Score " + sunScore);
			scoreLabel.repaint();
		} else if (State == STATE.GAME) {
			scoreLabel.setText("Score " + sunScore);
			scoreLabel.repaint();

			// Changes the Enum state LEVEL based on the amount of zombies spawned in
			if (numberOfZomb < 15) {
				// System.out.println("Low Health zombies");
				Level = LEVEL.FIRST;
			} else if (numberOfZomb >= 15 && numberOfZomb <= 35) {
				// System.out.println("Med health");
				Level = LEVEL.SECOND;
			} else if (numberOfZomb > 35 && numberOfZomb <= 50) {
				System.out.println("Maximum health");
				Level = LEVEL.THIRD;
			}

			// Changes the zombies health as level increases
			if (Level == LEVEL.FIRST) {
				zombLevelHealth = 100;
				exploderLevelHealth = 35;
			} else if (Level == LEVEL.SECOND) {
				zombLevelHealth = 125;
				exploderLevelHealth = 50;
			} else if (Level == LEVEL.THIRD) {
				zombLevelHealth = 150;
				exploderLevelHealth = 65;
			}

			// Randomly adds NORMAL ZOMBIES off screen
			int popControl = rand.nextInt(300);

			if (popControl >= 100 && popControl <= 102) {
				BufferedImage img;
				try {
					img = ImageIO.read(new File("src/GameMain/zombie.png"));
					
					Zombie zombie = new Zombie(new Point2D.Double(col, row),
							new Point2D.Double(img.getWidth() - 1, img.getHeight() - 1), -0.5, zombLevelHealth, 1, img,
							true);
					ZOMBIES.add(zombie);
					numberOfZomb++;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			// Randomly adds EXPLODER ZOMBIES off screen
			if (popControl == 70) {
				// System.out.println("CREATE EXPLODER");
				ExploderZombie exploder = new ExploderZombie(new Point2D.Double(col, row),
						new Point2D.Double(ExploderZombie.img.getWidth() - 1, ExploderZombie.img.getHeight() - 1), -0.3,
						exploderLevelHealth, -1, false);
				ZOMBIES.add(exploder);

				numberOfZomb++;
			}
			
			if (popControl == 60) {
				BufferedImage img2;
				try {
					img2 = ImageIO.read(new File("src/GameMain/speed_zombie.png"));
					// System.out.println("CREATE EXPLODER");
					Zombie speeder = new Zombie(new Point2D.Double(col, row),
							new Point2D.Double(img2.getWidth() - 1, img2.getHeight() - 1), -1.0, zombLevelHealth, 1, img2,
							true);
					ZOMBIES.add(speeder);
					numberOfZomb++;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			// Randomly creates SUNRAYS
			if (popControl >= 1 && popControl <= 4) {
				try {
					// Only adds sun past the 100 x position
					// Change to account for expanding col
					if (sunCol > 100) {
						BufferedImage img = ImageIO.read(new File("src/GameMain/sun.png"));
						Sprite sun = new Sprite(new Point2D.Double(sunCol, -50),
								new Point2D.Double(img.getWidth(), img.getHeight()), img, 1);
						SUNRAYS.add(sun);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			Sprite.attack(getSHOTS(), ZOMBIES);

			for (Shot shot : getSHOTS()) {
				shot.move();
			}

			ArrayList<Zombie> newActors = new ArrayList<>();
			// ArrayList<Actor> newActors2 = new ArrayList<>();
			// Update decrements an internal counter for some kind of action.
			for (Zombie z : ZOMBIES) {
				//System.out.println(z.isFrozen);
				if (!z.isFrozen && z.readyForAction()) {
					z.move();
				}
				// Checks to see if the zombie is alive, adds if it is
				//System.out.println(z.getRemove());
				if (z.isAlive()) {
					newActors.add(z);
				}else {
					zombKilled++;
				}
			}
			ZOMBIES = newActors;

			ArrayList<Plant> tempPlants = new ArrayList<>();
			for (Plant plant : PLANTS) {
				if (plant.isAlive()) {
					tempPlants.add(plant);
				}
				// else plant.scheduler.shutdown();

			}

			PLANTS = tempPlants;
			// Removes sun from bottom of screen
			for (Sprite s : SUNRAYS) {
				s.moveVertical();
			}

			// Removes the sun when it leaves the screen!
			ListIterator<Sprite> sunItr = SUNRAYS.listIterator();
			while (sunItr.hasNext()) {
				Sprite s = (Sprite) sunItr.next();
				if (s.getPosition().getY() >= HEIGHT) {
					sunItr.remove();
				}
			}

			Shot.attack(SHOTS, ZOMBIES);
			Zombie.attackPlant(PLANTS, ZOMBIES);
			Zombie.attackHome(h, ZOMBIES);

			ExploderZombie.attackPlant(PLANTS, ZOMBIES);
			ExploderZombie.attackHome(h, ZOMBIES);

			if (h.health <= 0) {
				State = STATE.DONE;
			}

			// Continues to move the Zombie after they are done attacking the plants
			for (Plant plant : PLANTS) {
				
			//System.out.println("sss"+plant.isAlive());
				ListIterator<Zombie> listIterator = ZOMBIES.listIterator();
				while(listIterator.hasNext()) {
					Zombie zomb = (Zombie) listIterator.next();
					if (zomb.isCollidingOther(plant) && !(plant.isAlive()) && zomb instanceof Zombie) {
						zomb.isFrozen = false;
						zomb.setRemove(false);
					} else if (zomb.isCollidingOther(plant) && !(plant.isAlive()) && zomb instanceof ExploderZombie) {
						 System.out.println("Exploder erase");
						listIterator.remove();
					} else if (zomb.getCooldown() <= 0 && zomb.hasBeenFrozen) {
						//System.out.println(zomb.isFrozen);
						zomb.isFrozen = false;
					}
				}

				// Creates a new object shot whenever the cooldown has been reached
				// if (SHOTS.size() >= 1) {
				for (Plant p : PLANTS) {
					if (p.readyForAction()) {
						// System.out.println("New Shot time");
						Shot shot = new Shot(
								new Point2D.Double(p.getPosition().getX() + 20, p.getPosition().getY() + 8),
								new Point2D.Double(Shot.img.getWidth(), Shot.img.getHeight()), 1, 1, p.isFreezer);

						SHOTS.add(shot);
						p.resetCoolDown();
					}
				}

				// End game state
			}
		} else if (State == STATE.BUY || State == STATE.DONE) {
			this.remove(scoreLabel);
		}
		// Redraw the new scene
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keycode = e.getKeyCode();
		// Pauses the game if the escape button is pressed and the state is game
		if (GameRun.State != STATE.START) {
			if (GameRun.State == STATE.GAME) {
				if (keycode == KeyEvent.VK_ESCAPE) {
					System.out.println("GAME PAUSED");
					GameRun.State = STATE.PAUSE;
				} else if (keycode == KeyEvent.VK_B) {
					System.out.println("BUY MENU OPENED");
					GameRun.State = STATE.BUY;
				}
			} else if (GameRun.State != STATE.GAME && GameRun.State != STATE.START && keycode == KeyEvent.VK_ESCAPE
					|| keycode == KeyEvent.VK_B) {
				System.out.println("GAME RESUMED"); // unpause when esc or b is pressed
				GameRun.State = STATE.GAME;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		int clickedX = e.getX() + startX;
		int clickedY = e.getY();
		pt = new Point2D.Double(e.getX(), clickedY);
		// Checks if the play button is pressed
		mouseClicked(clickedX, clickedY);
		if (State == STATE.GAME) {

			this.add(scoreLabel);

			// make sure SUNRAYS has elements to remove
			ArrayList<Sprite> tempSun = new ArrayList<>();

			// Checks if sun is clicked
			for (Sprite s : SUNRAYS) {
				if (s.isCollidingPoint(pt)) {
					System.out.println("sun clicked");
					sunScore += 25;
					// Removes the sun sprite from the SUNRAYS array that is clicked
				} else
					tempSun.add(s);
			}
			SUNRAYS = tempSun;
		}
	}

//	
//	mx >= WIDTH - (buyButtonSize.getX() * 3)
//			&& mx <= WIDTH - (buyButtonSize.getX() * 2)
//	my >= HEIGHT - buyButtonSize.getY() && my <= HEIGHT - buyButtonSize.getY()

	public void mouseClicked(int mx, int my) {

		if (State == STATE.PAUSE || State == STATE.START) {
			// Checks if the mouse clicked the play button
			if (mx >= 0) {
				if (my >= 0) {
					State = STATE.GAME;
				}
			}
		} else if (State == STATE.BUY && (mx >= 25 && mx <= 150 || mx >= 187 && mx <= 312 || mx >= 350 && mx <= 475)) {
			if (my >= 172 && my <= 263) {
				// This checks if the click was on the buy button
				State = STATE.FROZEN;
				point.setLocation((double) mx, (double) my);
				//System.out.println(point);
				this.add(scoreLabel);
				System.out.println("GAME FROZEN - PLACE YOUR PLANT");
			}
		} else if (State == STATE.FROZEN) {
			// Rounds the xPos and yPos to match grid of size spacing
			row = (mx - startX) / spacing;
			row = row * spacing;

			col = (my - startY) / spacing;
			col = col * spacing;

			boolean isClicked = false;
			// If a plant is clicked
			for (Plant p : PLANTS) {
				if (p.isCollidingPoint(pt)) {
					isClicked = true;
					System.out.println("INVALID LOCATION, PLANT ALREADY IN CLICKED LOCATION");
					return;
				}
			}
			//System.out.println(point);
			if (sunScore >= 25 && !isClicked && point.getX() >= 25.0 && point.getX() <= 150.0) {
				try {
					BufferedImage shooterImg = ImageIO.read(new File("src/GameMain/GreenShooter.png"));

					// Creates a plant
					Plant plant = new Plant(new Point2D.Double(row, col),
							new Point2D.Double(shooterImg.getWidth() - 1, shooterImg.getHeight() - 1), 0, 200, 75,
							10, false, shooterImg);
					PLANTS.add(plant);

					Shot shot = new Shot(
							new Point2D.Double(plant.getPosition().getX() + 20, plant.getPosition().getY() + 8),
							new Point2D.Double(Shot.img.getWidth(), Shot.img.getHeight()), 1, 1, plant.isFreezer);
					SHOTS.add(shot);

					sunScore -= 25;
					System.out.println("PLANT PLACED, PRESS ESC OR B TO END PLACEMENT AND RESUME GAME");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (sunScore >= 75 && !isClicked && point.getX() >= 187 && point.getX() <= 312) {
				try {
					BufferedImage freezerImg = ImageIO.read(new File("src/GameMain/Shooter.png"));

					// Creates a plant
					Plant freezer = new Plant(new Point2D.Double(row, col),
							new Point2D.Double(freezerImg.getWidth() - 1, freezerImg.getHeight() - 1), 0, 200, 125,
							10, true, freezerImg);
					PLANTS.add(freezer);

					Shot shot = new Shot(
							new Point2D.Double(freezer.getPosition().getX() + 20, freezer.getPosition().getY() + 8),
							new Point2D.Double(Shot.img.getWidth(), Shot.img.getHeight()), 1, 1, freezer.isFreezer);
					SHOTS.add(shot);

					sunScore -= 75;
					System.out.println("PLANT PLACED, PRESS ESC OR B TO END PLACEMENT AND RESUME GAME");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (sunScore >= 50 && !isClicked && point.getX() >= 350 && point.getX() <= 475) {
				try {
					BufferedImage tankImg = ImageIO.read(new File("src/GameMain/TankShooter.png"));

					// Creates a plant
					Plant tank = new Plant(new Point2D.Double(row, col),
							new Point2D.Double(tankImg.getWidth() - 1, tankImg.getHeight() - 3), 0, 300, 100,
							10, false, tankImg);
					PLANTS.add(tank);

					sunScore -= 50;
					System.out.println("PLANT PLACED, PRESS ESC OR B TO END PLACEMENT AND RESUME GAME");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("NOT ENOUGH SCORE OR INVALID LOCATION");
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public ArrayList<Shot> getSHOTS() {
		return SHOTS;
	}

	public static void setSHOTS(ArrayList<Shot> shot) {
		SHOTS = shot;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	public static void setPLANTS(ArrayList<Plant> tempPlant) {
		PLANTS = tempPlant;
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame app = new JFrame("Whack-a-Zombie");
				app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				// ImageIcon icon = new ImageIcon("src/GameMain/Background.png");
				GameRun panel = new GameRun();
				// panel.setIcon(icon);

				app.setContentPane(panel);
				app.pack();
				app.setVisible(true);
//				app.setResizable(false);
//				app.setMinimumSize(new Dimension(WIDTH,HEIGHT));
//				app.setMaximumSize(new Dimension(WIDTH,HEIGHT));
			}
		});
	}
}

/*
 * // Implement for all grid positions // yPos = startY + row * size // Solve
 * for Row , you know yPos // int row = (yPos - startY)/size // Multiply the row
 * by the size // Do the same math for col : Hint Math.Floor // DONE^^ // //
 * Rounds the position of where PLANTS are being placed on the screen (NOT
 * NEEDED ANYMORE) public Point2D.Double roundedGrid(int y) { Point2D.Double
 * answer = null; boolean stopped = false;
 * 
 * for (int row = 0; row < HEIGHT; row += spacing) { if (y >= row && y <= row +
 * spacing) { answer = new Point2D.Double(0, row); stopped = true; break; } }
 * 
 * if (stopped) return answer; else return new Point2D.Double(0, 0); }
 * 
 * 
 * 
 * // Removes the shot and lowers a ZOMBIES health when hit boolean wasHit =
 * false; ArrayList<Shot> tempShot = new ArrayList<>(); // Checks each shot on
 * each zombie for (Shot s : SHOTS) { s.move(); for (Zombie z : ZOMBIES) { //
 * Checks to see if the shot is colliding with the zombie if
 * (s.isCollidingOther(z) && z.getZombStat()) { z.hit(); wasHit = true; } if
 * (!wasHit) { tempShot.add(s); } wasHit = false; }
 * 
 * } SHOTS = tempShot;
 */