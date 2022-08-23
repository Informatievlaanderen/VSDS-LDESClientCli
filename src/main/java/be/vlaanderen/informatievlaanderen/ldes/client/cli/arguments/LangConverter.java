package be.vlaanderen.informatievlaanderen.ldes.client.cli.arguments;

import com.beust.jcommander.IStringConverter;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFLanguages;

public class LangConverter implements IStringConverter<Lang> {

	@Override
	public Lang convert(String value) {
		return RDFLanguages.nameToLang(value);
	}
}
