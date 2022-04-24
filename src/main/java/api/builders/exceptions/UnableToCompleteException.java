package api.builders.exceptions;

import lombok.Getter;

public class UnableToCompleteException extends Exception {
	
	private final @Getter String message;
	
	public UnableToCompleteException(String message) {
		this.message = message;
	}
}
