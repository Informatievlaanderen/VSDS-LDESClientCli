package be.vlaanderen.informatievlaanderen.ldes.client.cli.arguments;

import org.apache.jena.riot.Lang;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LangConverterTest {
   private final LangConverter langConverter = new LangConverter();

    @ParameterizedTest
    @ArgumentsSource(LangArgumentsProvider.class)
    void when_LangStringExists_RealLangIsReturned(String langString, Lang expectedLang){
        Lang actualLang = langConverter.convert(langString);
        assertNotNull(actualLang);
        assertEquals(expectedLang, actualLang);
    }

    @Test
    void when_LangStringDoesNotExist_NullIsReturned(){
        Lang actualLang = langConverter.convert("unexisting_lang");
        assertNull(actualLang);
    }

    static class LangArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("JSON-LD", Lang.JSONLD),
                    Arguments.of("N-Quads", Lang.NQUADS),
                    Arguments.of("N-Triples", Lang.NTRIPLES),
                    Arguments.of("Turtle", Lang.TURTLE));
        }
    }

}