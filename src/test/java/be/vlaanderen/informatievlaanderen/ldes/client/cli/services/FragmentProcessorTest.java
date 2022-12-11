package be.vlaanderen.informatievlaanderen.ldes.client.cli.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParserBuilder;
import org.junit.jupiter.api.Test;

import be.vlaanderen.informatievlaanderen.ldes.client.exceptions.FragmentFetcherException;
import be.vlaanderen.informatievlaanderen.ldes.client.exceptions.UnparseableFragmentException;
import be.vlaanderen.informatievlaanderen.ldes.client.services.LdesService;
import be.vlaanderen.informatievlaanderen.ldes.client.valueobjects.LdesFragment;
import be.vlaanderen.informatievlaanderen.ldes.client.valueobjects.LdesMember;

class FragmentProcessorTest {
	LdesService ldesService = mock(LdesService.class);

	@Test
	void when_LdesServerHasFragments_TheyAreConvertedAndPrintedOut() throws IOException, URISyntaxException {
		when(ldesService.hasFragmentsToProcess()).thenReturn(true);
		LdesFragment ldesFragment = new LdesFragment();
		LdesMember ldesMember = readLdesMemberFromFile(getClass().getClassLoader(), "member1.txt");
		ldesFragment.addMember(ldesMember);
		when(ldesService.processNextFragment()).thenReturn(ldesFragment);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);

		FragmentProcessor fragmentProcessor = new FragmentProcessor(ldesService, printStream, Lang.NQUADS);
		fragmentProcessor.processLdesFragments();

		byteArrayOutputStream.flush();
		String actualOutput = byteArrayOutputStream.toString();
		assertTrue(ldesMember.getMemberModel().isIsomorphicWith(convertToModel(actualOutput)));
	}

	@Test
	void when_LdesServerHasNoFragments_NothingIsPrinted() throws IOException {
		when(ldesService.hasFragmentsToProcess()).thenReturn(false);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);

		FragmentProcessor fragmentProcessor = new FragmentProcessor(ldesService, printStream, null);
		fragmentProcessor.processLdesFragments();

		byteArrayOutputStream.flush();
		String actualOutput = byteArrayOutputStream.toString();
		assertEquals("", actualOutput);
	}

	@Test
	void when_LdesServerThrowsUnparseableFragmentException_UnparseableFragmentExceptionIsRethrown() {
		when(ldesService.hasFragmentsToProcess())
				.thenThrow(new UnparseableFragmentException("fragmentURL", new RuntimeException()));
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);

		FragmentProcessor fragmentProcessor = new FragmentProcessor(ldesService, printStream, null);
		UnparseableFragmentException unparseableFragmentException = assertThrows(UnparseableFragmentException.class,
				fragmentProcessor::processLdesFragments);

		assertEquals("LDES Client: fragmentURL", unparseableFragmentException.getMessage());
	}

	@Test
	void when_LdesServerThrowsFragmentFetcherException_FragmentFetcherExceptionIsRethrown() {
		when(ldesService.hasFragmentsToProcess())
				.thenThrow(new FragmentFetcherException("fragmentURL", new RuntimeException()));
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);

		FragmentProcessor fragmentProcessor = new FragmentProcessor(ldesService, printStream, null);
		FragmentFetcherException fragmentFetcherException = assertThrows(FragmentFetcherException.class,
				fragmentProcessor::processLdesFragments);

		assertEquals("fragmentURL", fragmentFetcherException.getMessage());
	}

	private LdesMember readLdesMemberFromFile(ClassLoader classLoader, String fileName)
			throws URISyntaxException, IOException {
		File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).toURI());
		String memberString = Files.lines(Paths.get(file.toURI())).collect(Collectors.joining());
		Model outputModel = convertToModel(memberString);

		return new LdesMember("https://private-api.gipod.beta-vlaanderen.be/api/v1/mobility-hindrances/10810464/1",
				outputModel);
	}

	private Model convertToModel(String memberString) {
		return RDFParserBuilder.create().fromString(memberString).lang(Lang.NQUADS).toModel();
	}

}