package be.vlaanderen.informatievlaanderen.ldes.client.cli.services;

import java.io.PrintStream;
import java.util.concurrent.ExecutorService;

import org.apache.jena.riot.Lang;
import org.springframework.stereotype.Component;

import be.vlaanderen.informatievlaanderen.ldes.client.LdesClientImplFactory;
import be.vlaanderen.informatievlaanderen.ldes.client.cli.model.EndpointBehaviour;
import be.vlaanderen.informatievlaanderen.ldes.client.services.LdesService;

@Component
public class LdesClientCli {
	private final ExecutorService executorService;

	private static final PrintStream OUTPUT_STREAM = System.out;

	public LdesClientCli(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public void start(String fragmentId, Lang dataSourceFormat, Lang dataDestinationFormat, Long expirationInterval,
			Long pollingInterval, EndpointBehaviour endpointBehaviour) {
		UnreachableEndpointStrategy unreachableEndpointStrategy = getUnreachableEndpointStrategy(endpointBehaviour,
				fragmentId, pollingInterval);
		LdesService ldesService = LdesClientImplFactory.getLdesService();
		ldesService.setDataSourceFormat(dataSourceFormat);
		ldesService.setFragmentExpirationInterval(expirationInterval);
		ldesService.queueFragment(fragmentId);
		FragmentProcessor fragmentProcessor = new FragmentProcessor(ldesService, OUTPUT_STREAM, dataDestinationFormat);
		EndpointChecker endpointChecker = new EndpointChecker(fragmentId);
		CliRunner cliRunner = new CliRunner(fragmentProcessor, endpointChecker, unreachableEndpointStrategy);
		executorService.submit(cliRunner);
		executorService.shutdown();
	}

	private UnreachableEndpointStrategy getUnreachableEndpointStrategy(EndpointBehaviour endpointBehaviour,
			String fragmentId, long pollingInterval) {
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
