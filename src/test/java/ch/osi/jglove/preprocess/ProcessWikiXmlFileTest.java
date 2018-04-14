package ch.osi.jglove.preprocess;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class ProcessWikiXmlFileTest {

	@Test
	public void processDeWiki37Articles() {
		String file = "src/test/resources/ch/osi/jglove/preprocess/dewiki-37-articles.xml";
		ProcessWikiXmlFile processor = new ProcessWikiXmlFile();

		Stream<WikiPage> output = processor.process(file);
		assertNotNull(output);
		List<WikiPage> pages = output.collect(Collectors.toList());

		assertThat(pages.size(), is(36));
		WikiPage first = pages.get(0);
		assertThat(first.getTitle(), is("Alan Smithee"));
	}
}
