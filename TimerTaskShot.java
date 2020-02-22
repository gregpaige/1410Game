//package GameMain;
//
//import java.awt.geom.Point2D;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import javax.imageio.ImageIO;
//
//public class TimerTaskShot extends TimerTask{
//	private Timer shotTimer;
//	BufferedImage img;
//
//	private ArrayList<Shot> shots;
//	public TimerTaskShot() {
//
//		shotTimer = new Timer();
//		shotTimer.scheduleAtFixedRate(new TimerTaskShot(), 0, 3*1000);
//	}
//	
//	@Override
//	public void run() {
//		// Creates a shot when adding a new plant shooter
//
//		try {
//			System.out.println("this is running");
//			img = ImageIO.read(new File("src/GameMain/Spit.png"));
//			Shot shot = new Shot(new Point2D.Double(row + 35, col + 9),
//			new Point2D.Double(img.getWidth(), img.getHeight()), img, 1);
//			GameRun.SHOTS.add(shot);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//}
