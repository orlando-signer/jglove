package ch.osi.jglove.preprocess.wiki;

import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import io.reactivex.functions.Function;

public class WikiPageTokenizer implements Function<WikiPage, WikiPage> {

	@Override
	public WikiPage apply(WikiPage t) throws Exception {
		return new WikiPage(readToWords(t.getTitle()), readToWords(t.getText()));
	}

	private String readToWords(String value) {
		DocumentPreprocessor dp = new DocumentPreprocessor(new StringReader(value));
		TokenizerFactory<CoreLabel> tf = PTBTokenizer.coreLabelFactory();
		tf.setOptions("ptb3Escaping=false");
		dp.setTokenizerFactory(tf);
		return StreamSupport.stream(dp.spliterator(), false).flatMap(List::stream).map(HasWord::word)
				.collect(Collectors.joining(" "));
	}

}
