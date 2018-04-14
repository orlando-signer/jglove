package ch.osi.jglove.preprocess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class ProcessWikiXmlFile {

	public Stream<WikiPage> process(String wikiFile) {
		Builder<WikiPage> builder = Stream.builder();
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		try {
			XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(wikiFile));
			String title = null;
			String text = null;
			while (xmlEventReader.hasNext()) {
				XMLEvent xmlEvent = xmlEventReader.nextEvent();
				if (xmlEvent.isStartElement()) {
					StartElement startElement = xmlEvent.asStartElement();
					String eventName = startElement.getName().getLocalPart();
					switch (eventName) {
					case "page":
						title = null;
						text = null;
						break;
					case "title":
						title = xmlEventReader.nextEvent().asCharacters().getData();
						break;
					case "text":
						text = xmlEventReader.nextEvent().asCharacters().getData();
						break;
					default:
						continue;
					}
				}
				if (xmlEvent.isEndElement()) {
					EndElement endElement = xmlEvent.asEndElement();
					if (endElement.getName().getLocalPart().equals("page")) {
						builder.add(new WikiPage(title, text));
					}
				}
			}

		} catch (FileNotFoundException | XMLStreamException e) {
			throw new IllegalStateException("Error parsing XML-File '" + wikiFile + "'", e);
		}
		return builder.build();
	}

}
