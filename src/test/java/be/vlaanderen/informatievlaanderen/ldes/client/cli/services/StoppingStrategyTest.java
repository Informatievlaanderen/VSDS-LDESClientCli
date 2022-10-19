package be.vlaanderen.informatievlaanderen.ldes.client.cli.services;

import org.junit.jupiter.api.Test;

import be.vlaanderen.informatievlaanderen.ldes.client.cli.exceptions.EndpointNotReachableException;

import static org.junit.jupiter.api.Assertions.*;

class StoppingStrategyTest {

	UnreachableEndpointStrategy unreachableEndpointStrategy = new StoppingStrategy("endpoint");

	@Test
	void when_EndpointIsNotReachable_EndpointNotReachableExceptionIsThrown() {
		EndpointNotReachableException endpointNotReachableException = assertThrows(EndpointNotReachableException.class,
				() -> unreachableEndpointStrategy.handleUnreachableEndpoint());
		assertEquals("Endpoint not reachable: endpoint", endpointNotReachableException.getMessage());
	}

}