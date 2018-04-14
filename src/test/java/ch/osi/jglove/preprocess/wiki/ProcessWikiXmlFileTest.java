package ch.osi.jglove.preprocess.wiki;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;

import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;

public class ProcessWikiXmlFileTest {

	@Test
	public void processDeWiki37Articles() {
		File file = getFile("dewiki-37-articles.xml");
		TestSubscriber<WikiPage> testSubscriber = Flowable.fromIterable(new ProcessWikiXmlFile(file)).test();

		List<WikiPage> pages = testSubscriber.awaitDone(10, TimeUnit.SECONDS).values();
		assertThat(pages).size().isEqualTo(36);
		WikiPage first = pages.get(0);
		assertThat(first.getTitle()).isEqualTo("Alan Smithee");
		assertThat(first.getText()).startsWith("'''Alan Smithee'''").endsWith("[[Kategorie:Werk von Alan Smithee]]");
		assertThat(first.getText().length()).isGreaterThan(1000);
	}

	@Test
	public void processNoTextArticle() {
		File file = getFile("dewiki-no-text-article.xml");
		TestSubscriber<WikiPage> testSubscriber = Flowable.fromIterable(new ProcessWikiXmlFile(file)).test();

		List<WikiPage> pages = testSubscriber.awaitDone(10, TimeUnit.SECONDS).values();
		assertThat(pages).isEmpty();
	}

	@Test
	@Ignore
	public void procesFullWiki() throws Exception {
		System.setProperty("jdk.xml.totalEntitySizeLimit", "" + 0);
		File file = Paths.get("F:", "wiki", "dewiki-20180401-pages-articles.xml", "dewiki-20180401-pages-articles.xml")
				.toFile();
		Long count = Flowable.fromIterable(new ProcessWikiXmlFile(file)).count().toFuture().get(10, TimeUnit.MINUTES);

		assertThat(count).isCloseTo(4227932L, within(100L));
	}

	private File getFile(String filename) {
		return Paths.get("src", "test", "resources", "ch", "osi", "jglove", "preprocess", "wiki", filename).toFile();
	}

}
