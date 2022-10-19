package be.vlaanderen.informatievlaanderen.ldes.client.cli.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.vlaanderen.informatievlaanderen.ldes.client.cli.exceptions.EndpointNotReachableException;

public class StoppingStrategy implements UnreachableEndpointStrategy {
	private static final Logger LOGGER = LoggerFactory.getLogger(StoppingStrategy.class);
	private final String endpoint;

	public StoppingStrategy(String endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public void handleUnreachableEndpoint() {
		LOGGER.info("endpoint not available");
		throw new EndpointNotReachableException(endpoint);
	}
}
