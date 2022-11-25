package be.vlaanderen.informatievlaanderen.ldes.client.cli;

import be.vlaanderen.informatievlaanderen.ldes.client.cli.services.LdesClientCli;
import org.apache.jena.riot.Lang;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static be.vlaanderen.informatievlaanderen.ldes.client.cli.model.EndpointBehaviour.STOPPING;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(args = { "--url", "ldes-url" })
class ApplicationTest {

	@MockBean
	private LdesClientCli ldesClientCli;

	@Test
	void test() {
		verify(ldesClientCli, times(1)).start("ldes-url", Lang.JSONLD, Lang.NQUADS, 604800L, 60L, STOPPING);
	}

}