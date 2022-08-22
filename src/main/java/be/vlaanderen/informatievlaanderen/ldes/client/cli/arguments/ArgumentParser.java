package be.vlaanderen.informatievlaanderen.ldes.client.cli.arguments;

import com.beust.jcommander.JCommander;
import org.springframework.stereotype.Component;

@Component
public class ArgumentParser {

    public CommandlineArguments parseArguments(String ... args){
        CommandlineArguments arguments = new CommandlineArguments();
        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(args);
        return arguments;
    }
}
