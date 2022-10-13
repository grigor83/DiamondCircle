package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import figures.Figure;
import figures.Fly;
import figures.GhostFigure;
import gui.Card;
import gui.MatrixPanel;
import gui.MyButton;
import gui.NorthPanel;
import gui.RightPanel;

public class Game implements Runnable {
	private MatrixPanel matrixPanel;
	private RightPanel rightPanel;
	private NorthPanel northPanel;
	private ArrayList<Player> players;
	private ArrayList<Card> cards;
	private Timer timerTime;
	private String descriptionOfMove;
	private int time;
	private Thread threadDrawingCards;
	private Object lock;
	private boolean wait;
	private GhostFigure ghost;
	
	public Game(MatrixPanel panel,RightPanel rightPanel, NorthPanel northPanel,
					ArrayList<Player> players,ArrayList<Card> cards) {	
		matrixPanel=panel;
		this.rightPanel=rightPanel;
		this.northPanel=northPanel;
		this.players=players;
		this.cards=cards;
		this.lock=northPanel.getLock();
		ghost=new GhostFigure(0,matrixPanel, null);
		startTimerTime();
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (true) {
			matrixPanel.clearLastField();
			if (checkPlayers()) {
				//System.out.println("Igra je zavrsena!");
				matrixPanel.getDescriptionField().setText("IGRA JE ZAVRŠENA!");
				timerTime.cancel();
				saveGame();
				northPanel.getStopButton().setText("NOVA IGRA");
				northPanel.setEnded(true);
				break;
			}
			
			for (Player player : players) {	
				if(wait)
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							DiamondCircle.loger.log(Level.WARNING, e.fillInStackTrace().toString());
							JOptionPane.showMessageDialog(null, e);							
						}
					}
				//Nit će se pauzirati na 1 sekundu u play() metodu
				player.play(this);
			}
		}
	}
	
	public boolean checkPlayers() {
		boolean END_GAME=false;
		for (Player player : players)
			if (!(END_GAME=player.isFinished()))
					return END_GAME;
		
		return END_GAME;
	}
	
	public int drawCard(Player player) {
		Card card=cards.remove(0);
		cards.add(card);
		rightPanel.setButton(card);
		rightPanel.revalidate();
		rightPanel.repaint();
		if(card.getNumber()==0) {
			descriptionOfMove=" Igrač "+player.getName()+" je izvukao specijalnu kartu! ";
			matrixPanel.getDescriptionField().setText(descriptionOfMove);
			openHoles();
		}
		
		return card.getNumber();
	}
	
	private void openHoles() {
		//Ovaj metod samo crta rup(crne krugove) na slučajno izabranim poljima. Nakon toga poziva metod closeHoles()
		HashSet<Integer> holePositions=new HashSet<>();
		Random rand = new Random();
		int i=0;
		while(i<DiamondCircle.numberOfHoles) {
			int x=rand.nextInt(matrixPanel.getFields().size());
			if(holePositions.add(x)) {
				MyButton button=matrixPanel.getField(x);		
				synchronized (button) {
					ImageIcon icon=(ImageIcon) button.getIcon();
					Image image = icon.getImage();  
					 // Create a buffered image with transparency
				    BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				    // Crta image dobijen sa dugmeta na prazni, transparentni bufferedimage
				    Graphics2D bGr = bimage.createGraphics();
				    bGr.drawImage(image, 0, 0, null);
				    button.setIcon(new ImageIcon(bimage));
				    bGr.setColor(Color.black);   	
		    	    bGr.fillOval(image.getWidth(null)-45,image.getHeight(null)-45,20,20);
				    bGr.dispose();
				    button.setIcon(new ImageIcon(bimage));
				    button.setHole(true);
				}
				
				i++;
			}
		}
		
		try {
			Thread.sleep(DiamondCircle.SLEEP_TIME);
		} catch (InterruptedException e) {
			DiamondCircle.loger.log(Level.WARNING, e.fillInStackTrace().toString());
			JOptionPane.showMessageDialog(null, e);
		}
		if(isWait()) {
			synchronized (lock) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					DiamondCircle.loger.log(Level.WARNING, e.fillInStackTrace().toString());
					JOptionPane.showMessageDialog(null, e);						}
			}
		}

		closeHoles(holePositions);
	}

	private void closeHoles(HashSet<Integer> holePositions) {
		BufferedImage temp;
		Iterator<Integer> iter=holePositions.iterator();
		while(iter.hasNext()) {
			int i=iter.next();
			MyButton button=matrixPanel.getField(i);
			synchronized (button) {
				button.setHole(false);
				if(!(i==MatrixPanel.FINISH_FIELD)) {
					Iterator<Figure> iterFigures=button.getFigures().iterator();
					while(iterFigures.hasNext()) {
						Figure f=iterFigures.next();
						if (!(f instanceof Fly)) {
							f.setFell(this, true);
							//Da bih izbjegao grešku prilikom pristupa kolekciji i uklanjanja elementa iz nje, prvo ću obrisati figuru iz kolekcije
							//pomoću iteratora. Tako poziv remove() u metodu freeCurrentField neće izbaciti izuzetak i neće imati efekta jer je 
							//figura već izbrisana
							iterFigures.remove();
							f.freeCurrentField(this,matrixPanel);
							matrixPanel.getDescriptionField().setText(descriptionOfMove);
						}
					}
				}
				
				if(button.hasDiamond()) {
					button.setIcon(DiamondCircle.DIAMOND_IMAGE);
				}
				else {
					button.setIcon(DiamondCircle.DEFAULT_IMAGE);
				}
				if(!button.getFigures().isEmpty())
					button.getFigures().get(0).paintFigure(button);
			}			
				
		    iter.remove();
		}
		matrixPanel.refreshPanel();
		
		try {
			Thread.sleep(DiamondCircle.SLEEP_TIME);
		} catch (InterruptedException e) {
			DiamondCircle.loger.log(Level.WARNING, e.fillInStackTrace().toString());
			JOptionPane.showMessageDialog(null, e);
		}
		
		if(isWait()) {
			synchronized (lock) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					DiamondCircle.loger.log(Level.WARNING, e.fillInStackTrace().toString());
					JOptionPane.showMessageDialog(null, e);						}
			}
		}
		
	}

	public MatrixPanel getPanelButtons() {
		return matrixPanel;
	}
	
	private void saveGame() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("HH_mm_ss");
		String fileName="IGRA_"+formatter.format(date)+".txt";
		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter(new File(DiamondCircle.dir, fileName)));
			for(int i=0;i<players.size();i++) {
				bw.write("Igrač "+(i+1)+" - ");
				bw.write(players.get(i).toString());
			}
			bw.write("Ukupno vrijeme trajanja igre: "+rightPanel.getTimeField().getText());
			bw.close();
		} catch (IOException e) {
			DiamondCircle.loger.log(Level.WARNING, e.fillInStackTrace().toString());
			JOptionPane.showMessageDialog(null, e);
		}
	}

	public String getDescriptionOfMove() {
		return descriptionOfMove;
	}

	public void setDescriptionOfMove(String descriptionOfMove) {
		this.descriptionOfMove = descriptionOfMove;
	}
	
	public void setThreadDrawingCards(Thread t) {
		threadDrawingCards=t;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public Object getLock() {
		return lock;
	}
	
	public void startTimerTime() {
		Object lock=getLock();
		timerTime=new Timer();
		timerTime.schedule(new TimerTask() {
			@Override
			public void run() {
				if(wait)				
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							DiamondCircle.loger.log(Level.WARNING, e.fillInStackTrace().toString());
							JOptionPane.showMessageDialog(null, e);							
						}
					}
				if (time%5==0) {
					Random rand=new Random();
					ghost.move(null, matrixPanel, rand.nextInt(DiamondCircle.dimensionOfMatrix-1)+2);
				}
				
				rightPanel.setTimeField("Vrijeme trajanja igre: "+time++ +" s");
			}
		}, 0, 1000);
	}

	public boolean isWait() {
		return wait;
	}

	public void setWait(boolean wait) {
		this.wait = wait;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}
