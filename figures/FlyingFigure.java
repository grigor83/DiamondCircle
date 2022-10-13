package figures;

import java.awt.Color;

import gui.MatrixPanel;
import main.DiamondCircle;
import main.Game;

public class FlyingFigure extends Figure implements Fly {

	public FlyingFigure(int name,Color color) {
		super(name, DiamondCircle.PLANE_IMAGE, color);
	}

	@Override
	public void move(Game game, MatrixPanel matrixPanel, int step) {
		chooseNextField(game,matrixPanel, step+diamonds);
	}

}
