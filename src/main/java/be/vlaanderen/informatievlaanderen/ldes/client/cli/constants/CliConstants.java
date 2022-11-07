package be.vlaanderen.informatievlaanderen.ldes.client.cli.constants;

import org.apache.jena.riot.Lang;

public class CliConstants {

	private CliConstants() {
	}

	/**
	 * The expected RDF format of the LDES data source
	 */
	public static final Lang DEFAULT_SOURCE_FORMAT = Lang.JSONLD;
	/**
	 * The desired RDF format for output
	 */
	public static final Lang DEFAULT_DESTINATION_FORMAT = Lang.NQUADS;
	/**
	 * The number of seconds to add to the current time before a fragment is
	 * considered expired.
	 * <p>
	 * Only used when the HTTP request that contains the fragment does not have a
	 * max-age element in the Cache-control header.
	 */
	public static final Long DEFAULT_EXPIRATION_INTERVAL = 604800L;
	/**
	 * The amount of time to wait to call the LdesService when the queue has no
	 * mutable fragments left or when the mutable fragments have not yet expired.
	 */
	public static final Long DEFAULT_POLLING_INTERVAL = 60L;
}
