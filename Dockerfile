#### build layer
FROM maven:3.8.5-openjdk-17-slim AS build
ARG GITHUB_REF=dev-SNAPSHOT
ENV HOME=/app
RUN mkdir -p $HOME
WORKDIR $HOME

ADD pom.xml $HOME
ADD sparql-anything-archive/pom.xml $HOME/sparql-anything-archive/
ADD sparql-anything-bibliography/pom.xml $HOME/sparql-anything-bibliography/
ADD sparql-anything-binary/pom.xml $HOME/sparql-anything-binary/
ADD sparql-anything-cli/pom.xml $HOME/sparql-anything-cli/
ADD sparql-anything-csv/pom.xml $HOME/sparql-anything-csv/
ADD sparql-anything-docs/pom.xml $HOME/sparql-anything-docs/
ADD sparql-anything-engine/pom.xml $HOME/sparql-anything-engine/
ADD sparql-anything-fuseki/pom.xml $HOME/sparql-anything-fuseki/
ADD sparql-anything-html/pom.xml $HOME/sparql-anything-html/
ADD sparql-anything-it/pom.xml $HOME/sparql-anything-it/
ADD sparql-anything-json/pom.xml $HOME/sparql-anything-json/
ADD sparql-anything-markdown/pom.xml $HOME/sparql-anything-markdown/
ADD sparql-anything-metadata/pom.xml $HOME/sparql-anything-metadata/
ADD sparql-anything-model/pom.xml $HOME/sparql-anything-model/
ADD sparql-anything-parser/pom.xml $HOME/sparql-anything-parser/
ADD sparql-anything-rdf/pom.xml $HOME/sparql-anything-rdf/
ADD sparql-anything-spreadsheet/pom.xml $HOME/sparql-anything-spreadsheet/
ADD sparql-anything-testutils/pom.xml $HOME/sparql-anything-testutils/
ADD sparql-anything-text/pom.xml $HOME/sparql-anything-text/
ADD sparql-anything-xml/pom.xml $HOME/sparql-anything-xml/
ADD sparql-anything-yaml/pom.xml $HOME/sparql-anything-yaml/

RUN mvn verify clean --fail-never
ADD . $HOME
RUN mvn clean install -DskipTests -Dgenerate-server-jar=true -Dgenerate-cli-jar=true -Drevision=$GITHUB_REF

# Generate geosparql distribution
RUN mvn install -DskipTests -Dgenerate-cli-jar-geosparql=true -Dgenerate-server-jar-geosparql=true -Drevision=$GITHUB_REF

#### runtime layer
# A plain JRE instead of mcr.microsoft.com/playwright/java (which bundles a JDK, Maven and
# browsers built for a newer Playwright than the 1.17 SPARQL Anything uses, so they are never used).
FROM eclipse-temurin:17-jre-focal
ARG GITHUB_REF=dev-SNAPSHOT
# Browsers for the html.browser option; set to "" to leave them out.
ARG PLAYWRIGHT_BROWSERS="chromium firefox webkit"
LABEL description="SPARQL Anything"

ENV HOME="/app"
ENV PLAYWRIGHT_BROWSERS_PATH=/ms-playwright

WORKDIR $HOME
COPY --from=build --chown=10001:0 $HOME/sparql-anything-fuseki/target/sparql-anything-server-$GITHUB_REF.jar $HOME/artifacts/
COPY --from=build --chown=10001:0 $HOME/sparql-anything-cli/target/sparql-anything-$GITHUB_REF.jar $HOME/artifacts/
COPY --from=build --chown=10001:0 $HOME/sparql-anything-fuseki/target/sparql-anything-server-${GITHUB_REF}-geosparql.jar $HOME/artifacts/
COPY --from=build --chown=10001:0 $HOME/sparql-anything-cli/target/sparql-anything-${GITHUB_REF}-geosparql.jar $HOME/artifacts/
# Link rather than copy, so the server jar is stored only once
RUN ln -s $HOME/artifacts/sparql-anything-server-$GITHUB_REF.jar $HOME/sparql-anything-server.jar && \
    chown -h 10001:0 $HOME/sparql-anything-server.jar && chmod g+rwx $HOME $HOME/artifacts

# Install the browsers matching the bundled Playwright version, plus their system libraries
RUN if [ -n "$PLAYWRIGHT_BROWSERS" ]; then \
      apt-get update && \
      java -cp $HOME/sparql-anything-server.jar com.microsoft.playwright.CLI install-deps $PLAYWRIGHT_BROWSERS && \
      java -cp $HOME/sparql-anything-server.jar com.microsoft.playwright.CLI install $PLAYWRIGHT_BROWSERS && \
      # Playwright re-checks (and registers) the browsers on every start, so the runtime user needs write access
      chown -R 10001:0 $PLAYWRIGHT_BROWSERS_PATH && chmod -R g+rwX $PLAYWRIGHT_BROWSERS_PATH && \
      rm -rf /var/lib/apt/lists/* /tmp/*; \
    fi

USER 10001

VOLUME /data
EXPOSE 3000

CMD ["java", "-jar", "/app/sparql-anything-server.jar"]
