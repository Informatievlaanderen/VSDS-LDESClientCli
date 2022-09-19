package be.vlaanderen.informatievlaanderen.ldes.client.cli.services;

import be.vlaanderen.informatievlaanderen.ldes.client.cli.model.EndpointBehaviour;
import org.apache.jena.riot.Lang;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.*;

class LdesClientCliTest {

	ExecutorService executorService = mock(ExecutorService.class);
	LdesClientCli ldesClientCli = new LdesClientCli(executorService);

	@Test
	void when_LdesClientCliIsStarted_TaskOfClassCliRunnerIsSubmitted() {
		ldesClientCli.start("fragmentId", Lang.NQUADS, Lang.TURTLE, 1000L, 20L, EndpointBehaviour.STOPPING);

		verify(executorService, times(1)).submit(any(CliRunner.class));
		verify(executorService, times(1)).shutdown();
	}

}