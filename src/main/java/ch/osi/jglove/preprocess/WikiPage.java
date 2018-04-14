package ch.osi.jglove.preprocess;

import java.util.Objects;

public class WikiPage {
	private final String title;
	private final String text;

	public WikiPage(String title, String text) {
		this.title = Objects.requireNonNull(title);
		this.text = Objects.requireNonNull(text);
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}
}
