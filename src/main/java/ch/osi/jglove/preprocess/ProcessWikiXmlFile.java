package ch.osi.jglove.preprocess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessWikiXmlFile {

	private static final Logger LOG = LoggerFactory.getLogger(ProcessWikiXmlFile.class);

	public Stream<WikiPage> process(File wikiFile) {
		Builder<WikiPage> builder = Stream.builder();
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		try {
			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(wikiFile),
					StandardCharsets.UTF_8.name());
			String title = null;
			StringBuilder text = null;
			while (xmlStreamReader.hasNext()) {
				xmlStreamReader.next();
				// XMLEvent xmlEvent = xmlStreamReader.nextEvent();
				if (xmlStreamReader.isStartElement()) {
					switch (xmlStreamReader.getLocalName()) {
					case "page":
						title = null;
						text = null;
						break;
					case "title":
						xmlStreamReader.next();
						title = xmlStreamReader.getText();
						break;
					case "text":
						xmlStreamReader.next();
						if (xmlStreamReader.isCharacters()) {
							text = new StringBuilder();
							do {
								text.append(xmlStreamReader.getText());
								xmlStreamReader.next();
							} while (xmlStreamReader.isCharacters());
						}
						break;
					default:
						continue;
					}
				} else if (xmlStreamReader.isEndElement()) {
					if (xmlStreamReader.getLocalName().equals("page")) {
						if (text == null) {
							LOG.info("No text for Page {}, ignoring it", title);
						} else {
							LOG.debug("Add wiki-page {}", title);
							builder.add(new WikiPage(title, text.toString()));
						}
					}
				}
			}

		} catch (FileNotFoundException | XMLStreamException e) {
			throw new IllegalStateException("Error parsing XML-File '" + wikiFile + "'", e);
		}
		return builder.build();
	}

}
