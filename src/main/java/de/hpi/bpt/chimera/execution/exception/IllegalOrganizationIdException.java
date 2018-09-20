package de.hpi.bpt.chimera.execution.exception;

@SuppressWarnings("serial")
public class IllegalOrganizationIdException extends IllegalIdentifierException {
	private static final String ERROR_MESSAGE = "The organization id: %s is not assigned";

	public IllegalOrganizationIdException(String orgId) {
		super(String.format(ERROR_MESSAGE, orgId));
	}
}
