package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import exceptions.DimensionException;
import exceptions.LoadImagesException;
import exceptions.NumberOfHolesException;
import gui.GameFrame;

public class DiamondCircle {
	public static int dimensionOfMatrix, numberOfHoles=5, SLEEP_TIME=1000;
	public static Logger loger;
	public static ImageIcon DEFAULT_IMAGE, DIAMOND_IMAGE, CARD_IMAGE, SPECIAL_CARD_IMAGE;
	public static BufferedImage PAWN_IMAGE, PLANE_IMAGE, RABBIT_IMAGE;
	public static final String defaultImage = "resource"+File.separator+"default.png", diamondImage="resource"+File.separator+"diamond.png", 
								pawnImage="resource"+File.separator+ "pawn.png", planeImage="resource"+File.separator+"plane.png", 
								rabbitImage="resource"+File.separator+"rabbit.png",cardImage="resource"+File.separator+"card.png", 
								specialCard="resource"+File.separator+"specialCard.png";
	public static final String DIR_NAME="RESULTS";
	public static File dir;

	public static void main(String[] args) {

		DiamondCircle.setupLogger();
		DiamondCircle.loadImages();
		createDir();
		startNewGame();
	}
	
	private static void setupLogger() {
		loger = Logger.getLogger(DiamondCircle.class.getName());
		
		try {
			// argument true oznacava da ce se novi logovi dodavati u fajl bez brisanja starih
			FileHandler handler = new FileHandler ("diamondCircle.log", true);
			handler.setLevel(Level.WARNING);
			handler.setFormatter(new SimpleFormatter());
			LogManager.getLogManager().reset();
			loger.addHandler(handler);
		} catch (IOException e) {
			loger.log(Level.SEVERE, "File logger not working!", e);
			e.printStackTrace();
		}
	}

	private static void loadImages() {		
		DEFAULT_IMAGE=new ImageIcon(defaultImage);
		DIAMOND_IMAGE=new ImageIcon(diamondImage);
		CARD_IMAGE=new ImageIcon(cardImage);
		SPECIAL_CARD_IMAGE=new ImageIcon(specialCard);
		
		try {
			if (DEFAULT_IMAGE==null)
				throw new LoadImagesException();
			try {
				PAWN_IMAGE=ImageIO.read(new File(pawnImage));
				PLANE_IMAGE=ImageIO.read(new File(planeImage));
				RABBIT_IMAGE=ImageIO.read(new File(rabbitImage));
			}
			catch(IOException e) {
				throw new LoadImagesException();
			}
		}
		catch(LoadImagesException ex) {
			loger.log(Level.WARNING, ex.fillInStackTrace().toString());
			JOptionPane.showMessageDialog(null, ex);
			System.exit(2);;
		}
	}
	
	private static void createDir() {
		dir=new File(DIR_NAME);
		if(!dir.exists())
			dir.mkdir();
	}
	
	public static void startNewGame() {
		GameFrame gameBoard;

		while (true) {
			try {
				gameBoard = new GameFrame("DiamondCircle");
				gameBoard.startGame();
				break;
			}
			catch (DimensionException  e) {
				loger.log(Level.WARNING, e.fillInStackTrace().toString());
				JOptionPane.showMessageDialog(null, e);
			}
			catch(NumberOfHolesException e2) {
				Player.clearNames();
				loger.log(Level.WARNING, e2.fillInStackTrace().toString());
				JOptionPane.showMessageDialog(null, e2);
			}
			
			catch (NumberFormatException ex) {
				loger.log(Level.WARNING, ex.fillInStackTrace().toString());
				JOptionPane.showMessageDialog(null, ex);
				break;
			} 
		}
	}
}
