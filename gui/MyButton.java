package gui;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JButton;

import figures.Figure;

public class MyButton extends JButton {
	private ArrayList<Figure> figures;
	private boolean diamond, hole;
	
	public MyButton(String text) {
		super(text);
		
		figures=new ArrayList<>();
		diamond=false;
		setName("free");
		setPreferredSize(new Dimension(60,60));
	}
	
	public void addFigure(Figure f) {
		figures.add(f);
	}
	
	public synchronized ArrayList<Figure> getFigures(){
		return figures;
	}

	public synchronized boolean hasDiamond() {
		return diamond;
	}

	public void setDiamond(boolean diamond) {
		this.diamond = diamond;
	}

	public boolean isHole() {
		return hole;
	}

	public void setHole(boolean hole) {
		this.hole = hole;
	}
	
}
