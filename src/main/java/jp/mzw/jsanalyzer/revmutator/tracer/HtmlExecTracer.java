package jp.mzw.jsanalyzer.revmutator.tracer;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.crawljax.util.DomUtils;

public class HtmlExecTracer extends ExecTracer {
	
	public static Document parse(String html) {
		return Jsoup.parse(html);
	}

}
