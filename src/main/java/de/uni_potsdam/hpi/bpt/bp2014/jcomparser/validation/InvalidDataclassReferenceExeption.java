package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.validation;

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
