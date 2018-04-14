package ch.osi.jglove.preprocess.wiki;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessWikiXmlFile implements Iterable<WikiPage> {

	private static final Logger LOG = LoggerFactory.getLogger(ProcessWikiXmlFile.class);

	private XMLStreamReader reader;
	private WikiPage next;

	public ProcessWikiXmlFile(File wikiFile) {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		try {
			this.reader = xmlInputFactory.createXMLStreamReader(new FileInputStream(wikiFile),
					StandardCharsets.UTF_8.name());
			this.next = getNext();
		} catch (FileNotFoundException | XMLStreamException e) {
			throw new IllegalStateException("Error parsing XML-File '" + wikiFile + "'", e);
		}
	}

	private WikiPage getNext() throws XMLStreamException {
		String title = null;
		StringBuilder text = null;
		while (reader.hasNext()) {
			reader.next();
			if (reader.isStartElement()) {
				switch (reader.getLocalName()) {
				case "page":
					title = null;
					text = null;
					break;
				case "title":
					reader.next();
					title = reader.getText();
					break;
				case "text":
					reader.next();
					if (reader.isCharacters()) {
						text = new StringBuilder();
						do {
							text.append(reader.getText());
							reader.next();
						} while (reader.isCharacters());
					}
					break;
				default:
					continue;
				}
			} else if (reader.isEndElement() && reader.getLocalName().equals("page")) {
				if (text == null) {
					LOG.info("No text for Page {}, ignoring it", title);
				} else {
					return new WikiPage(title, text.toString());
				}
			}
		}
		return null;
	}

	@Override
	public Iterator<WikiPage> iterator() {
		return new Iterator<WikiPage>() {

			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public WikiPage next() {
				WikiPage cur = next;
				try {
					next = getNext();
				} catch (XMLStreamException e) {
					throw new IllegalStateException("Error parsing XML-File", e);
				}
				return cur;
			}
		};
	}

}
