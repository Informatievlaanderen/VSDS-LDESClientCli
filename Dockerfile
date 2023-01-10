FROM maven:3.8.5-openjdk-18

# Temporary build VSDS-LDESConnectors until it is distributed via maven central
# Note when a new version is used update the "git reset --hard" command
RUN git clone https://github.com/Informatievlaanderen/VSDS-LDESConnectors.git/
WORKDIR /VSDS-LDESConnectors
RUN git reset --hard c9cb7132887a792579a96cbf92c1fa3879fa70d6
RUN mvn clean install
RUN rm -rf *.db *.db-*

WORKDIR /
COPY . /
RUN mvn -DskipTests -DskipITs net.revelc.code.formatter:formatter-maven-plugin:format install
RUN rm -rf *.db *.db-*

ENTRYPOINT ["java","-jar","target/ldes-client-cli-jar-with-dependencies.jar"]
