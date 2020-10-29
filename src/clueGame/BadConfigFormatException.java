/*
 * Exception to be thrown when setup files for game arent formatted correctly
 */

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
