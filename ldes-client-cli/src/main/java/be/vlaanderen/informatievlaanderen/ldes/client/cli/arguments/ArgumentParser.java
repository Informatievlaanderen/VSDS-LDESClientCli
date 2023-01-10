package be.vlaanderen.informatievlaanderen.ldes.client.cli.arguments;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.springframework.stereotype.Component;

@Component
public class ArgumentParser {

	public CommandlineArguments parseArguments(String... args) {
		CommandlineArguments arguments = new CommandlineArguments();
		JCommander build = JCommander.newBuilder()
				.addObject(arguments)
				.build();
		try {
			build.parse(args);
			return arguments;
		} catch (ParameterException parameterException) {
			build.usage();
			throw parameterException;
		}
	}
}
