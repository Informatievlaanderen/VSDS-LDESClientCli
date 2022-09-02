package be.vlaanderen.informatievlaanderen.ldes.client.cli.exception;

public class EndpointNotReachableException extends RuntimeException {

	private final String endpoint;

	public EndpointNotReachableException(String endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public String getMessage() {
		return "Endpoint not reachable: " + endpoint;
	}
}
