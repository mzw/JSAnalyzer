package jp.mzw.jsanalyzer.parser;
import org.jsoup.nodes.Document;

/**
 * Represents HTML source code
 * @author Yuta Maezawa
 *
 */
public class HTMLCode extends Code {
	

	protected HTMLCode(String code, Document doc, String url) {
		super(code, doc, url, Code.Inline);
	}
}
