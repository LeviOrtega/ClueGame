package clueGame;

public class BadConfigFormatException extends RuntimeException {

	private String message;
	
	public BadConfigFormatException() {
		super("File not formatted correctly");
	}
	
	public BadConfigFormatException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
