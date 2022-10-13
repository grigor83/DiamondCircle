package exceptions;

public class NotUniqueNameException extends Exception {
	private final static String NOT_UNIQUE_NAME_EXCEPTION_MSG = "Ime igrača mora biti jedinstveno! Unesite drugo ime!";
	
	public NotUniqueNameException() {
		super (NOT_UNIQUE_NAME_EXCEPTION_MSG);
	}
	
	public NotUniqueNameException (String msg) {
		super (msg);
	}
}
