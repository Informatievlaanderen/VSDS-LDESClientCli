FROM maven:3.8.5-openjdk-18

# Temporary build VSDS-LDESClient4J until it is distributed via maven central
RUN git clone https://github.com/Informatievlaanderen/VSDS-LDESClient4J.git
WORKDIR /VSDS-LDESClient4J
RUN mvn clean install

WORKDIR /
COPY . /
RUN mvn install
ENTRYPOINT ["java","-jar","target/ldes-client-cli-jar-with-dependencies.jar"]
