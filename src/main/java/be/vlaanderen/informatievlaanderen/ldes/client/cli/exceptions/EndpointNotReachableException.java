package be.vlaanderen.informatievlaanderen.ldes.client.cli.exceptions;

public class EndpointNotReachableException extends RuntimeException {

	/** Implements Serializable. */
	private static final long serialVersionUID = 3828492633404449817L;
	
	private final String endpoint;

	public EndpointNotReachableException(String endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public String getMessage() {
		return "Endpoint not reachable: " + endpoint;
	}
}
