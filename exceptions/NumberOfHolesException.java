package exceptions;

import gui.MatrixPanel;
import main.DiamondCircle;

public class NumberOfHolesException extends Exception {
	private static final String NUMBER_OF_HOLES_EXCEPTION_MSG = "Broj rupa ne smije biti negativan ili veći od broja polja na igrici: "
								+MatrixPanel.FINISH_FIELD+"!";

	public NumberOfHolesException() {
		super(NUMBER_OF_HOLES_EXCEPTION_MSG);
	}

	public NumberOfHolesException(String message) {
		super(message);
	}
}
