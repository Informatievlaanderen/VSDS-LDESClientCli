package be.vlaanderen.informatievlaanderen.ldes.client.cli.state;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.jena.riot.Lang;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import be.vlaanderen.informatievlaanderen.ldes.client.LdesClientImplFactory;
import be.vlaanderen.informatievlaanderen.ldes.client.cli.services.CliRunner;
import be.vlaanderen.informatievlaanderen.ldes.client.cli.services.EndpointChecker;
import be.vlaanderen.informatievlaanderen.ldes.client.cli.services.FragmentProcessor;
import be.vlaanderen.informatievlaanderen.ldes.client.cli.services.WaitingStrategy;
import be.vlaanderen.informatievlaanderen.ldes.client.services.LdesService;
import be.vlaanderen.informatievlaanderen.ldes.client.state.LdesStateManager;

@WireMockTest(httpPort = 10101)
class LdesClientCliStateTest {

	private final String fragment3 = "http://localhost:10101/exampleData?generatedAtTime=2022-05-03T00:00:00.000Z";
	private final String fragment4 = "http://localhost:10101/exampleData?generatedAtTime=2022-05-04T00:00:00.000Z";
	private final String fragment5 = "http://localhost:10101/exampleData?generatedAtTime=2022-05-05T00:00:00.000Z";

	ExecutorService executorService;
	LdesService ldesService;
	LdesStateManager stateManager;

	@BeforeEach
	void setup() {
		ldesService = LdesClientImplFactory.getLdesService();
		stateManager = ldesService.getStateManager();

		ldesService.setDataSourceFormat(Lang.JSONLD);
		ldesService.setFragmentExpirationInterval(1000L);

		stateManager.clearState();

		ldesService.queueFragment(fragment3);
	}

	@AfterEach
	void tearDown() {
		stateManager.destroyState();
	}

	@Test
	void whenLdesClientCliHasReplicated_thenNoFragmentsRemainInTheQueue() throws Exception {
		FragmentProcessor fragmentProcessor = new FragmentProcessor(ldesService, System.out, Lang.TURTLE);
		EndpointChecker endpointChecker = new EndpointChecker(fragment3);
		CliRunner cliRunner = new CliRunner(fragmentProcessor, endpointChecker, new WaitingStrategy(20L));

		executorService = Executors.newSingleThreadExecutor();
		executorService.submit(cliRunner);
		await().atMost(1, TimeUnit.MINUTES).until(stateManager::countQueuedFragments, equalTo(0L));

		assertEquals(0, stateManager.countQueuedFragments());
		assertEquals(3, stateManager.countProcessedImmutableFragments());
		assertEquals(6, stateManager.countProcessedMembers());
	}

	@Test
	void whenLdesClientCliResumes_thenCliResumesAtLastMutableFragment() {
		FragmentProcessor fragmentProcessor = new FragmentProcessor(ldesService, System.out, Lang.TURTLE);
		EndpointChecker endpointChecker = new EndpointChecker(fragment3);
		CliRunner cliRunner = new CliRunner(fragmentProcessor, endpointChecker, new WaitingStrategy(20L));

		executorService = Executors.newSingleThreadExecutor();
		executorService.submit(cliRunner);
		await().atMost(1, TimeUnit.MINUTES).until(stateManager::countProcessedImmutableFragments, equalTo(1L));
		executorService.shutdown();

		assertEquals(fragment4, stateManager.next());

		executorService = Executors.newSingleThreadExecutor();
		executorService.submit(cliRunner);
		await().atMost(1, TimeUnit.MINUTES).until(stateManager::countProcessedImmutableFragments, equalTo(2L));
		executorService.shutdown();

		assertEquals(fragment5, stateManager.next());
	}
}
