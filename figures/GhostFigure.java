package figures;

import java.awt.Color;
import java.util.HashSet;
import java.util.Random;

import javax.management.timer.TimerMBean;
import javax.swing.ImageIcon;

import gui.MatrixPanel;
import gui.MyButton;
import main.DiamondCircle;
import main.Game;

public class GhostFigure extends Figure {
	private HashSet<Integer> diamondsPositions;

	public GhostFigure(int name, MatrixPanel matrixPanel, Color color) {
		super(name,null,null);
		diamondsPositions=new HashSet<>();
	}

	@Override
	public void move(Game game,MatrixPanel matrixPanel, int step) {
		clearDiamonds(matrixPanel);
		//Bira slučajna polja na stazi
		Random rand = new Random();
		int i=0;
		while(i<step) {
			int x=rand.nextInt(matrixPanel.getFields().size());
			if(diamondsPositions.add(x)) {
				MyButton button=matrixPanel.getField(x);
				synchronized (button) {
					button.setIcon(DiamondCircle.DIAMOND_IMAGE);
					button.setHorizontalTextPosition(MyButton.CENTER);
					button.setVerticalTextPosition(MyButton.CENTER);	
					button.setDiamond(true);
					if(!button.getFigures().isEmpty()) {
						Figure f = button.getFigures().get(0);
						f.pickDiamond(button);
					}				
					paintFigure(button);
				}
				
				i++;
			}
		}
		//System.out.println("broj dijamanata je "+diamondsPositions.size());		
		matrixPanel.refreshPanel();
	}

	private void clearDiamonds(MatrixPanel matrixPanel) {
		if (diamondsPositions.isEmpty())
			return;
		
		for (Integer integer : diamondsPositions) {
			MyButton button=matrixPanel.getField(integer);
			synchronized (button) {
				button.setDiamond(false);
				button.setIcon(DiamondCircle.DEFAULT_IMAGE);
				for (Figure f : button.getFigures()) {
					f.paintFigure(button);
				}
			}
		}
		diamondsPositions.clear();
	}
}
