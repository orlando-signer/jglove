package ch.osi.jglove.preprocess;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class ProcessWikiXmlFileTest {

	@Test
	public void processDeWiki37Articles() {
		File file = getFile("dewiki-37-articles.xml");
		ProcessWikiXmlFile processor = new ProcessWikiXmlFile();

		Stream<WikiPage> output = processor.process(file);
		assertThat(output).isNotNull();
		List<WikiPage> pages = output.collect(Collectors.toList());

		assertThat(pages).size().isEqualTo(36);
		WikiPage first = pages.get(0);
		assertThat(first.getTitle()).isEqualTo("Alan Smithee");
		assertThat(first.getText()).startsWith("'''Alan Smithee'''").endsWith("[[Kategorie:Werk von Alan Smithee]]");
		assertThat(first.getText().length()).isGreaterThan(1000);
	}

	@Test
	public void processNoTextArticle() {
		File file = getFile("dewiki-no-text-article.xml");
		ProcessWikiXmlFile processor = new ProcessWikiXmlFile();

		Stream<WikiPage> output = processor.process(file);
		assertThat(output).isNotNull();
		List<WikiPage> pages = output.collect(Collectors.toList());

		assertThat(pages).isEmpty();
	}

	@Test
	@Ignore
	public void procesFullWiki() {
		File file = Paths.get("F:", "wiki", "dewiki-20180401-pages-articles.xml", "dewiki-20180401-pages-articles.xml")
				.toFile();
		ProcessWikiXmlFile processor = new ProcessWikiXmlFile();

		Stream<WikiPage> output = processor.process(file);
		assertThat(output).isNotNull();
		List<WikiPage> pages = output.collect(Collectors.toList());

		assertThat(pages.size()).isGreaterThan(10000);
		WikiPage first = pages.get(0);
		assertThat(first.getTitle()).isEqualTo("Alan Smithee");
	}

	private File getFile(String filename) {
		return Paths.get("src", "test", "resources", "ch", "osi", "jglove", "preprocess", filename).toFile();
	}
}
