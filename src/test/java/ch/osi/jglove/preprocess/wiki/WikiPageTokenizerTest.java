package ch.osi.jglove.preprocess.wiki;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class WikiPageTokenizerTest {
	private WikiPageTokenizer tokenizer;

	@Before
	public void setup() {
		this.tokenizer = new WikiPageTokenizer();
	}

	@Test
	public void helloWorld() throws Exception {
		String title = "Hello";
		String text = "Hello World!";
		WikiPage page = new WikiPage(title, text);

		WikiPage out = this.tokenizer.apply(page);
		assertThat(out).isNotNull().returns(title, WikiPage::getTitle).returns("Hello World !", WikiPage::getText);
	}

	@Test
	public void doubleSquareBracketsGetTokenized() throws Exception {
		String title = "Hello";
		String text = "[[Hello]] World!";
		WikiPage page = new WikiPage(title, text);

		WikiPage out = this.tokenizer.apply(page);
		assertThat(out).isNotNull().returns(title, WikiPage::getTitle).returns("[ [ Hello ] ] World !",
				WikiPage::getText);
	}

	@Test
	public void newLineGetsIgnored() throws Exception {
		String title = "Hello";
		String text = "Hello\nWorld!";
		WikiPage page = new WikiPage(title, text);

		WikiPage out = this.tokenizer.apply(page);
		assertThat(out).isNotNull().returns(title, WikiPage::getTitle).returns("Hello World !", WikiPage::getText);
	}
}
