package be.vlaanderen.informatievlaanderen.ldes.client.cli.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

import org.apache.jena.riot.Lang;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import be.vlaanderen.informatievlaanderen.ldes.client.cli.model.EndpointBehaviour;
import be.vlaanderen.informatievlaanderen.ldes.client.services.LdesService;

class LdesClientCliTest {

	ExecutorService executorService = mock(ExecutorService.class);
	LdesClientCli ldesClientCli = new LdesClientCli(executorService);

	@ParameterizedTest
	@ArgumentsSource(EndpointBehavoirArgumentsProvider.class)
	void when_LdesClientCliIsStarted_TaskOfClassCliRunnerIsSubmitted(final EndpointBehaviour endpointBehaviour) {
		ldesClientCli.start("fragmentId", Lang.NQUADS, Lang.TURTLE, 1000L, 20L, endpointBehaviour);

		verify(executorService, times(1)).submit(any(CliRunner.class));
		verify(executorService, times(1)).shutdown();
	}

	static class EndpointBehavoirArgumentsProvider implements ArgumentsProvider {
		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
			return Stream.of(Arguments.of(EndpointBehaviour.STOPPING), Arguments.of(EndpointBehaviour.WAITING));
		}
	}

	@Test
	void whenCliIsReady_thenLdesServiceIsAlso() {
		assertNotNull(ldesClientCli.getLdesService());

		LdesService ldesService = ldesClientCli.getLdesService();
		assertTrue(LdesService.class.isAssignableFrom(ldesService.getClass()));
	}

	@Test
	void whenIllegalEndpointBehaviourIsGiven_thenIllegalArgumentExceptionIsThrown() {
		assertThrows(NullPointerException.class, () -> ldesClientCli.getUnreachableEndpointStrategy(null, null, 0));
		assertThrows(IllegalArgumentException.class,
				() -> ldesClientCli.getUnreachableEndpointStrategy(EndpointBehaviour.TEST, null, 0));
	}
}