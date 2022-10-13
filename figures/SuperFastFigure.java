package figures;

import java.awt.Color;

import gui.MatrixPanel;
import main.DiamondCircle;
import main.Game;

public class SuperFastFigure extends Figure {

	public SuperFastFigure(int name, Color color) {
		super(name, DiamondCircle.RABBIT_IMAGE, color);
	}

	@Override
	public void move(Game game,MatrixPanel matrixPanel, int step) {
		step*=2;
		chooseNextField(game,matrixPanel, step+diamonds);
	}

}
