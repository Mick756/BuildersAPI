package api.builders.exceptions;

import lombok.Getter;

public class ChestNotFoundException extends Exception {
	
	private final @Getter String message;
	
	public ChestNotFoundException(String message) {
		this.message = message;
	}
}
