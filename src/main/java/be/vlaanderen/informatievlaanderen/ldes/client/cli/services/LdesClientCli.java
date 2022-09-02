package be.vlaanderen.informatievlaanderen.ldes.client.cli.services;

import be.vlaanderen.informatievlaanderen.ldes.client.LdesClientImplFactory;
import be.vlaanderen.informatievlaanderen.ldes.client.cli.model.EndpointBehaviour;
import be.vlaanderen.informatievlaanderen.ldes.client.services.LdesService;
import org.apache.jena.riot.Lang;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class LdesClientCli {
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

	private static final PrintStream OUTPUT_STREAM = System.out;

	private LdesClientCli() {
	}

	public void start(String fragmentId, Lang dataSourceFormat, Lang dataDestinationFormat, Long expirationInterval,
			Long pollingInterval, EndpointBehaviour endpointBehaviour) {
		UnreachableEndpointStrategy unreachableEndpointStrategy = getUnreachableEndpointStrategy(endpointBehaviour,
				fragmentId, pollingInterval);
		LdesService ldesService = LdesClientImplFactory.getLdesService(dataSourceFormat, expirationInterval);
		ldesService.queueFragment(fragmentId);
		FragmentProcessor fragmentProcessor = new FragmentProcessor(ldesService, OUTPUT_STREAM, dataDestinationFormat);
		EndpointChecker endpointChecker = new EndpointChecker(fragmentId);
		CliRunner cliRunner = new CliRunner(fragmentProcessor, endpointChecker, unreachableEndpointStrategy);
		EXECUTOR_SERVICE.submit(cliRunner);
		EXECUTOR_SERVICE.shutdown();
	}

	UnreachableEndpointStrategy getUnreachableEndpointStrategy(EndpointBehaviour endpointBehaviour, String fragmentId,
			long pollingInterval) {
		switch (endpointBehaviour) {
			case STOPPING -> {
				return new StoppingStrategy(fragmentId);
			}
			case WAITING -> {
				return new WaitingStrategy(pollingInterval);
			}
			default -> throw new IllegalArgumentException();
		}
	}

}
