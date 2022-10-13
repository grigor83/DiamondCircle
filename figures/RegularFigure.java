package figures;

import java.awt.Color;

import gui.MatrixPanel;
import main.DiamondCircle;
import main.Game;

public class RegularFigure extends Figure {

	public RegularFigure(int name,Color color) {
		super(name,DiamondCircle.PAWN_IMAGE, color);
	}

	@Override
	public void move(Game game,MatrixPanel matrixPanel, int step) {
		chooseNextField(game,matrixPanel, step+diamonds);
	}

}
