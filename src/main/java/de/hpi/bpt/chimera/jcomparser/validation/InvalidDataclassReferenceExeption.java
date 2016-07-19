package de.hpi.bpt.chimera.jcomparser.validation;

/**
 *
 */
public class InvalidDataclassReferenceExeption extends RuntimeException {
    public InvalidDataclassReferenceExeption(String message) {
        super(message);
    }

    public InvalidDataclassReferenceExeption(String message, Throwable throwable) {
        super(message, throwable);
    }
}
