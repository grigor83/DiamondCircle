package exceptions;

public class AbortGameException extends Exception {
	private static final String ABORT_GAME_EXCEPTION_MSG = "Igrica je prekinuta!";

	public AbortGameException() {
		super(ABORT_GAME_EXCEPTION_MSG);
	}

	public AbortGameException(String message) {
		super(message);
	}

}
