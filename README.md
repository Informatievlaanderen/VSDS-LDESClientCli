# VSDS-LDESClientCli
Command Line Interface that uses ldes-client to follow and synchronize an LDES

<!-- TOC -->

- [VSDS-LDESClientCli](#vsds-ldesclientcli)
    - [Intro](#intro)
    - [Usage](#usage)
        - [Mandatory arguments](#mandatory-arguments)
        - [Optional arguments](#optional-arguments)
    - [Docker](#docker)

<!-- /TOC -->

## Intro

A command-line interface is available that can be started with the base url of the LDES as the single argument.
The stream will be followed and LDES members will be outputted to the console (only once).

## Usage

When called without arguments (or with the `-?` flag), the client CLI will print a usage statement.

```bash
java -jar target\ldes-client-cli-jar-with-dependencies.jar [ARGUMENTS]
```
This can also be done through the docker image as follows
```bash
docker run -ti ghcr.io/informatievlaanderen/ldes-cli [ARGUMENTS]
```

The client accepts arguments for:

### Mandatory arguments

  - **Input url (--url)**  
    The base fragment url of the LDES.

### Optional arguments 

- **Input format (-if or --input-format)**

  The MIME type of the data source. This value has to be [supported by Apache Jena](https://www.javadoc.io/doc/org.apache.jena/jena-arq/4.6.0/org/apache/jena/riot/Lang.html).  
  **Default Value:** application/ld+json

- **Output format (-of or --output-format)**

  The desired output MIME type. This value has to be [supported by Apache Jena](https://www.javadoc.io/doc/org.apache.jena/jena-arq/4.6.0/org/apache/jena/riot/Lang.html).  
  **Default Value:** application/n-quads

- **Expiration interval (-ei or --expiration)**

  This is the number of seconds to add to the current time before a fragment is considered expired. Only used when the HTTP request that contains the fragment does not have a max-age element in the Cache-control header.   
  **Default Value:** 604800

- **Polling interval (-pi or --polling)**

  This is the number of seconds to wait before polling the client again. When the client does not have any mutable fragments left in the queue, the CLI will wait this amount of seconds before calling the client again.   
  **Default Value:** 60

- **Endpoint Behaviour (-eb or --endpoint-behaviour)**

  The desired behaviour the Client should follow when the endpoint is not available.   
  Possible values: 
  - WAITING: When the endpoint is not available, the Client will keep polling until the available.
  - STOPPING: When the endpoint is not available, the Client will shut down.

  **Default Value:** STOPPING


If invalid values are given or required values are missing (negative or invalid numbers for the intervals or a language identifier that Jena doesn't recognize), the CLI will return with the missing or invalid value, print a help message and exit.

**Example:**

```bash
java -jar ldes-client-1.0-SNAPSHOT-jar-with-dependencies.jar -o turtle http://localhost:10101/ldes-test
```

## Docker

The CLI is available as a docker container. This removes the need to have a local java environment set up.
Internally, the docker containers calls the CLI with all provided arguments passed on.

**To run:**

See the [CLI usage](#usage) for available arguments.

**To build:**

```bash
docker build . -t ldes-cli
```
