package figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;

import gui.MatrixPanel;
import gui.MyButton;
import main.DiamondCircle;
import main.Game;

public abstract class Figure {
	private Color color;
	private String colorName;
	private int name, position;
	protected int diamonds, movingTime;
	private BufferedImage image;
	private boolean fell;
	private StringBuilder predjeniPut;
	private ArrayList<Integer> predjenaPolja;
	private Timer timer;
	public static final ArrayList<Coordinate> coordinates=new ArrayList<>();
	static {
		coordinates.add(new Coordinate(7, 7));
		coordinates.add(new Coordinate(40, 7));
		coordinates.add(new Coordinate(7, 30));
		coordinates.add(new Coordinate(40, 30));
	}
	
	public Figure(int name, BufferedImage image,Color color) {
		this.name=name;
		this.image=image;
		this.color=color;
		setColorName();
		position=MatrixPanel.START_FIELD;
		predjenaPolja=new ArrayList<>();
		timer=new Timer();
	}
	
	public abstract void move(Game game, MatrixPanel matrixPanel, int step);
	
	public void chooseNextField(Game game,MatrixPanel matrixPanel, int step) {
		while(true) {		
			//trazi se redni broj iduceg polja na putanji
			int newPosition = getPosition() + step;
			if (newPosition>=MatrixPanel.FINISH_FIELD || matrixPanel.getField(newPosition).getName().equals("free")) {
				takeField(game,matrixPanel,newPosition);
				break;
			}
			else
				step+=1;
		}
	}
	
	public void takeField(Game game, MatrixPanel matrixPanel, int newPosition) {		
		if(newPosition>=MatrixPanel.FINISH_FIELD) {
			newPosition=MatrixPanel.FINISH_FIELD;
			//System.out.println("Figura je stigla do kraja");
		}

		position=newPosition;
		MyButton button=matrixPanel.getField(position);
		synchronized (button) {
			if(predjeniPut==null) {
				predjeniPut=new StringBuilder();
				predjeniPut.append("Pređeni put: (").append(matrixPanel.getField(0).getText()).
							append("-").append(button.getText());
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						movingTime++;
					}
				}, 0, 1000);
				predjenaPolja.add(Integer.parseInt(matrixPanel.getField(0).getText()));
				synchronized (predjenaPolja) {
					predjenaPolja.add(Integer.parseInt(button.getText()));
					predjenaPolja.notifyAll();
				}
			}
			else {
				predjeniPut.append("-").append(button.getText());
				synchronized (predjenaPolja) {
					predjenaPolja.add(Integer.parseInt(button.getText()));
					predjenaPolja.notifyAll();
				}
			}
			if(position==MatrixPanel.FINISH_FIELD) {
				predjeniPut.append(")").append(" - Vrijeme kretanja: "+movingTime+" s");
				timer.cancel();
			}
			String s=game.getDescriptionOfMove();
			s+="na poziciju "+button.getText()+". ";
			game.setDescriptionOfMove(s);
			button.addFigure(this);   
			button.setName("buisy"); 
			if(button.hasDiamond()) 
				pickDiamond(button);
			paintFigure(button);
			//System.out.println(game.getDescriptionOfMove());
		}
		matrixPanel.refreshPanel();
	}

	public synchronized void paintFigure(MyButton button) {	
		synchronized (button) {
			ImageIcon icon=(ImageIcon) button.getIcon();
			Image imageOnButton = icon.getImage();  
			
			 // Create a buffered image with transparency
		    BufferedImage bimage = new BufferedImage(imageOnButton.getWidth(null), imageOnButton.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		    // Crta image dobijen sa dugmeta na prazni, transparentni bufferedimage
		    Graphics2D bGr = bimage.createGraphics();
		    bGr.drawImage(imageOnButton, 0, 0, null);
		    if (!button.getFigures().isEmpty()) {
		    	int i=0;
		    	Coordinate c;
		    	for (Figure f : button.getFigures()) {
		    		c=coordinates.get(i++);
		    		bGr.setColor(f.color);
		    		bGr.fillRect(c.x,c.y,20,30);
		    		bGr.drawImage(f.image, c.x,c.y,null);
				}
		    }
		    if(button.isHole()) {
		    	bGr.setColor(Color.black);   	
	    	    bGr.fillOval(imageOnButton.getWidth(null)-45,imageOnButton.getHeight(null)-45,20,20);
		    }
		    bGr.dispose();
		    button.setIcon(new ImageIcon(bimage));
		}
	}
	
	public void freeCurrentField(Game game, MatrixPanel matrixPanel) {
		MyButton button=matrixPanel.getField(position);
		synchronized (button) {
			if(!fell) {
				String s = game.getDescriptionOfMove();
				s+=button.getText()+" ";
				game.setDescriptionOfMove(s);
			}
			button.setName("free");
			button.getFigures().remove(this);
			button.setIcon(DiamondCircle.DEFAULT_IMAGE);
			matrixPanel.refreshPanel();
		}
	}
	
	public void pickDiamond(MyButton button) {
		diamonds++;
		button.setDiamond(false);
		//System.out.println("Pokupio sam dijamant");
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Color getColor() {
		return color;
	}

	public boolean isFell() {
		return fell;
	}

	public void setFell(Game game, boolean fell) {
		this.fell = fell;
		predjeniPut.append(")").append(" - Vrijeme kretanja: "+movingTime+" s");
		timer.cancel();
		String s=game.getDescriptionOfMove();
		s+="\n Figura "+name+" je upala u rupu! ";
		game.setDescriptionOfMove(s);
		//System.out.println(game.getDescriptionOfMove());
	}
	
	public Timer getTimer() {
		return timer;
	}
	
	public StringBuilder getPredjeniPut() {
		return predjeniPut;
	}
	
	private void setColorName() {
		if(color==null)
			return;
		int red=color.getRed();
		int green=color.getGreen();
		int blue=color.getBlue();
		if(red==0 && green==0 && blue==255)
			colorName="plava";
		else if(red==255 && green==0 && blue==0)
			colorName="crvena";
		else if(red==0 && green==255 && blue==0)
			colorName="zelena";
		else
			colorName="žuta";
	}
	
	public int getName() {
		return name;
	}

	public int getVrijemeKretanja() {
		return movingTime;
	}

	public ArrayList<Integer> getPredjenaPolja() {
		return predjenaPolja;
	}
	
	public String getColorName() {
		return colorName;
	}

	@Override
	public String toString() {
		return "	Figura "+name+" (Tip: "+getClass().getSimpleName()+", Boja: "+colorName+") - "
				+predjeniPut+" - Stigla do cilja: "+!isFell()+"\n";
	}

}
