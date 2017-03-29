package de.hpi.bpt.chimera.jcomparser.validation;

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
