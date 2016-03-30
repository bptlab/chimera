package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.validation;

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
