package gui;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JFrame;

import figures.Figure;
import main.DiamondCircle;

public class FigureFrame extends JFrame {
	private MyButton[] buttons;
	private Figure f;
	
	public FigureFrame(GameFrame gameBoard, Figure f) {
		super("PUTANJA FIGURE");
		this.f=f;
		setLayout(new GridBagLayout());
		addButtons();
		pack();
 		setLocation(gameBoard.getX()-200, gameBoard.getY()+50);
 		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 		setVisible(true);
	}

	private void addButtons() {
		buttons = new MyButton [DiamondCircle.dimensionOfMatrix*DiamondCircle.dimensionOfMatrix];
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		
		MyButton button;
		for (int i=0, k=1; i<DiamondCircle.dimensionOfMatrix; i++)
			for (int j=0; j<DiamondCircle.dimensionOfMatrix; j++) {
				button = new MyButton(""+k);
				button.setEnabled(false);
				gbc.gridx = j;
				gbc.gridy = i;
				add(button, gbc);
				buttons[k++ -1] = button;
			}
		drawPath();
	}

	public void drawPath() {
		for (Integer i : f.getPredjenaPolja()) {
			buttons[i-1].setBackground(f.getColor());
		}
	}
}
