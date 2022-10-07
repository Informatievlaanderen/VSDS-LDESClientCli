# VSDS-LDESClientCli
Command Line Interface that uses ldes-client to follow and synchronize an LDES


## Client CLI

A command-line interface is available that can be started with the base url of the LDES as the single argument.
The stream will be followed and LDES members will be outputted to the console (only once).


### CLI usage

When called without arguments (or with the `-?` flag), the client CLI will print a usage statement.

```bash
java -jar ldes-client-1.0-SNAPSHOT-jar-with-dependencies.jar
```

The client accepts arguments for:
- **Input format (-i or --input-format)**

  Passed on to the client as the [org.apache.jena.riot.Lang](https://jena.apache.org/documentation/javadoc/arq/org/apache/jena/riot/Lang.html) that we expect the LDES data source to be formatted in.
  
- **Output format (-o or --output-format)**

  The desired [org.apache.jena.riot.Lang](https://jena.apache.org/documentation/javadoc/arq/org/apache/jena/riot/Lang.html) for member output. Is passed on to Jena and therefore supports all the formats that Jena supports. Jena's name parser accepts variants on the official RDF format names (e.g. n-quads = nquads)

- **Expiration interval (-e or --expiration)**

  This is the number of seconds to add to the current time before a fragment is considered expired. Only used when the HTTP request that contains the fragment does not have a max-age element in the Cache-control header.
  
- **Polling interval (-p or --polling)**

  This is the number of seconds to wait before polling the client again. When the client does not have any mutable fragments left in the queue, the CLI will wait this amount of seconds before calling the client again.


If invalid values are given or required values are missing (negative or invalid numbers for the intervals or a language identifier that Jena doesn't recognize), the CLI will return with the missing or invalid value, print a help message and exit.

**Example:**

```bash
java -jar ldes-client-1.0-SNAPSHOT-jar-with-dependencies.jar -o turtle http://localhost:10101/ldes-test
```

### CLI configuration

When no commandline arguments are given to the client, defaults from the [properties file](src/main/resources/ldesclientcli.properties) will be used.

```properties
ldes.client.cli.polling.interval=30
ldes.client.cli.fragment.expiration.interval=604800
ldes.client.cli.data.source.format=JSON-LD
ldes.client.cli.data.destination.format=nquads
```
 
When there is no properties file or not all values are set, then the client SDK will use the defaults from [LdesClientDefaults](src/main/java/be/vlaanderen/informatievlaanderen/ldes/client/LdesClientDefaults.java), as explained [previously](#sdk-configuration)

### CLI docker

The CLI is available as a docker container. This removes the need to have a local java environment set up.
Internally, the docker containers calls the CLI with all provided arguments passed on.

**To run:**

```bash
docker run -ti ldes.client [OPTIONS] <FRAGMENT URI>
```

See the [CLI usage](#cli-usage) for available arguments.

**Example**

```bash
docker run -ti ldes.client -o turtle http://localhost:10101
```

**To build:**

```bash
cd ldes-client
docker build -t ldes.client .
```
