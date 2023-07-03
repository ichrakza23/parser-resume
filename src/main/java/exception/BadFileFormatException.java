package exception;

public class BadFileFormatException extends Exception {
	private static final long serialVersionUID = 7718828512143293558L;

	public BadFileFormatException(String message) {
		super(message);
	}

}
