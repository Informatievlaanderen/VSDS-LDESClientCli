FROM maven:3.8.5-openjdk-18

# Temporary build VSDS-LDESClient4J until it is distributed via maven central
# Note when a new version is used update the "git reset --hard" command
RUN git clone https://github.com/Informatievlaanderen/VSDS-LDESClient4J.git/
WORKDIR /VSDS-LDESClient4J
RUN git reset --hard d9e8be5eb151d3eef1143f9c9b8252854d3b2e84
RUN mvn clean install
RUN rm -rf *.db *.db-*

WORKDIR /
COPY . /
RUN mvn -DskipTests -DskipITs net.revelc.code.formatter:formatter-maven-plugin:format install
RUN rm -rf *.db *.db-*

ENTRYPOINT ["java","-jar","target/ldes-client-cli-jar-with-dependencies.jar"]
