package exceptions;

public class NumberOfPlayersException extends Exception {
	private final static String NUMBER_OF_PLAYER_EXCEPTION_MSG = "Minimalan broj igrača je 2, a maksimalan 4!";
	
	public NumberOfPlayersException() {
		super (NUMBER_OF_PLAYER_EXCEPTION_MSG);
	}
	
	public NumberOfPlayersException (String msg) {
		super (msg);
	}

}
