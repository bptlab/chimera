package de.hpi.bpt.chimera.validation.old;

/**
 *
 */
public class InvalidDataTransitionException extends RuntimeException {
	public InvalidDataTransitionException(String message) {
		super(message);
	}

	public InvalidDataTransitionException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
