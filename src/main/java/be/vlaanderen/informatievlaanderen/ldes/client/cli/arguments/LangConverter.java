package be.vlaanderen.informatievlaanderen.ldes.client.cli.arguments;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFLanguages;

import com.beust.jcommander.IStringConverter;

public class LangConverter implements IStringConverter<Lang> {

	@Override
	public Lang convert(String value) {
		return RDFLanguages.nameToLang(value);
	}
}