package main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Level;
import javax.swing.JOptionPane;

import exceptions.AbortGameException;
import exceptions.NotUniqueNameException;
import figures.Figure;
import figures.FlyingFigure;
import figures.RegularFigure;
import figures.SuperFastFigure;
import gui.GameFrame;
import gui.MatrixPanel;
import gui.LeftPanel;

public class Player {
	//Na nivou klase Player biće ukupno 4 boje, smanjivaće se za 1 svaki put kad se kreira Player
	/*
	private static List<Color> colors = new LinkedList<Color> ();
	static {
		colors.add(Color.RED); colors.add(Color.GREEN); 
		colors.add(Color.BLUE); colors.add(Color.YELLOW);
	}
	*/	
	private static HashSet<String> names = new HashSet<String> ();
	private String name;
	private Color color;
	private ArrayList<Figure> figures;
	private Figure activeFigure;
	private int step;
	private boolean finished;
	private LeftPanel westPanel;
	
	public Player(LeftPanel westPanel) {
		finished=false;
		loadName();
		chooseColor();
		createFigures();
		this.westPanel=westPanel;
	}

	private void loadName() {
		while(true) {
			String name = JOptionPane.showInputDialog("Unesite ime igrača:");
			try {
				if (name==null)
					throw new AbortGameException();
				if (names.contains(name)) {
					throw new NotUniqueNameException();
				}
				this.name = name;
				names.add(name);
				break;
			}
			catch(AbortGameException a) {
				DiamondCircle.loger.log(Level.WARNING, a.fillInStackTrace().toString());
				JOptionPane.showMessageDialog(null, a);
				System.exit(1);
			}
			catch(NotUniqueNameException e) {
				DiamondCircle.loger.log(Level.WARNING, e.fillInStackTrace().toString());
				JOptionPane.showMessageDialog(null, e);
			}
		}
	}

	private void chooseColor() {
		Random rand=new Random();
		int i = rand.nextInt(GameFrame.COLORS.size());
		color=GameFrame.COLORS.get(i);
		GameFrame.COLORS.remove(i);
	}
	
	private void createFigures() {
		figures=new ArrayList<>();
		for (int j=0; j<4; j++) {
			figures.add (chooseType((j+1),color));
		}
	}

	private Figure chooseType(int name, Color color) {
		Random rand=new Random();
		int i = rand.nextInt(3);
		switch(i) {
			case 0 : return	new RegularFigure(name,color);
			case 1 : return new SuperFastFigure(name,color);
			default : return new FlyingFigure(name,color);
		}
	}
	
	public void play(Game game) {
		if(finished)
			return;
		
		Thread thread = new Thread() {
			public void run() {
				if(game.isWait()) {
					synchronized (game.getLock()) {
						try {
							game.getLock().wait();
						} catch (InterruptedException e) {
							DiamondCircle.loger.log(Level.WARNING, e.fillInStackTrace().toString());
							JOptionPane.showMessageDialog(null, e);						}
					}
				}
				//Vratiće figuru samo u slučaju da još nije stigla do kraja igre i da nije upala u rupu
				//KARTA SE IZVLAČI SAMO U SLUČAJU DA IGRAČ IMA SLOBODNIH FIGURA ZA IGRANJE
				activeFigure= chooseFigureToPlay(game);
				synchronized(this) {
				    this.notify();
				}
			}
		};
		game.setThreadDrawingCards(thread);
		thread.start();

		synchronized (thread) {
			try {
				thread.wait();
			} catch (InterruptedException e) {
				DiamondCircle.loger.log(Level.WARNING, e.fillInStackTrace().toString());
				JOptionPane.showMessageDialog(null, e);
			}
		}
		if (activeFigure==null)
			return;		
		if(activeFigure.getPosition()==MatrixPanel.START_FIELD)
			westPanel.addFigure(activeFigure);
		game.setDescriptionOfMove(" Na potezu je igrač "+name+". "
				+ " Figura "+activeFigure.getName()+" ("+activeFigure.getColorName()+")" 
				+" prelazi "+step+" polja, pomjera se sa pozicije ");
		activeFigure.freeCurrentField(game, game.getPanelButtons());
		activeFigure.move(game, game.getPanelButtons(),step);
		game.getPanelButtons().getDescriptionField().setText(game.getDescriptionOfMove());
		try {
			Thread.sleep(DiamondCircle.SLEEP_TIME);
		} catch (InterruptedException e) {
			DiamondCircle.loger.log(Level.WARNING, e.fillInStackTrace().toString());
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	private Figure chooseFigureToPlay(Game game) {
		for (Figure f : figures) {
			if(!f.isFell() && f.getPosition()<MatrixPanel.FINISH_FIELD) {
				while(true) {
					//Izvlačim kartu sve dok figura ne upadne, tad se poziva break i traži nova figura. Ili, while se prekida
					//ako figura nije upala i step je veći od nula
					step=game.drawCard(this);
					if(!f.isFell() && step>0)
						return f;
					//Ako je figura upala,prekida se izvlačenje karte za ovu figuru i uzima se nova raspoloživa figura, ako je igrač ima
					if(f.isFell())
						break;					
				}
			}
		}
		
		finished=true;
		//System.out.println("Igrač "+name+" je završio igru!");
		return null;
	}
	
	public boolean isFinished() {
		return finished;
	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public String toString() {
		String s=name+"\n";
		for (Figure f : figures) {
			s+=f;
		}
		return s;
	}
	
	public static void clearNames() {
		names=new HashSet<String>();
	}
	
}
