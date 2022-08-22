package be.vlaanderen.informatievlaanderen.ldes.client.cli.arguments;

import com.beust.jcommander.Parameter;
import org.apache.jena.riot.Lang;

import static be.vlaanderen.informatievlaanderen.ldes.client.cli.constants.Constants.*;

public class CommandlineArguments {
    @Parameter(
            names = "--url",
            description = "The base fragment url of the LDES",
            required = true, order = 0
    )
    private String url;
    @Parameter(
            names = {"--input-format", "-if"},
            description = "Input format of the LDES (n-quads, json-ld, ...)",
            converter = LangConverter.class,
            order = 1
    )
    private Lang sourceFormat;
    @Parameter(
            names = {"--output-format", "-sf"},
            description = "Output format of the members (n-quads, json-ld, ...)",
            converter = LangConverter.class,
            order = 2
    )
    private Lang outputFormat;
    @Parameter(
            names = {"--expiration-interval", "-ei"},
            description = "Expiration interval",
            order = 3
    )
    private Long expirationInterval;
    @Parameter(
            names = {"--polling-interval", "-pi"},
            description = "Polling interval",
            order = 4
    )
    private Long pollingInterval;

    @Parameter(names = "--help", description = "Enabling displays usage of cli", help = true, order = 5)
    private boolean help;

    public String getUrl() {
        return url;
    }

    public Lang getSourceFormat() {
        return sourceFormat == null ? DEFAULT_SOURCE_FORMAT : sourceFormat;
    }

    public Lang getOutputFormat() {
        return outputFormat == null ? DEFAULT_DESTINATION_FORMAT : outputFormat;
    }

    public Long getExpirationInterval() {
        return expirationInterval == null ? DEFAULT_EXPIRATION_INTERVAL : expirationInterval;
    }

    public Long getPollingInterval() {
        return pollingInterval == null ? DEFAULT_POLLING_INTERVAL : pollingInterval;
    }

    public boolean isHelp() {
        return help;
    }
}