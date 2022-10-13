package exceptions;

public class DimensionException extends Exception {
	private static final String DIMENSION_EXCEPTION_MSG = "Minimalne dimenzije matrice su 7x7, a maksimalne 10x10!";

	public DimensionException() {
		super(DIMENSION_EXCEPTION_MSG);
	}

	public DimensionException(String message) {
		super(message);
	}
}
