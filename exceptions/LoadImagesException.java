package exceptions;

public class LoadImagesException extends Exception {
	private static final String LOAD_IMAGES_EXCEPTION_MSG = "Slike nisu učitane!";

	public LoadImagesException() {
		super(LOAD_IMAGES_EXCEPTION_MSG);
	}

	public LoadImagesException(String message) {
		super(message);
	}
}
