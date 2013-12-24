package jp.mzw.jsanalyzer.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class StringUtils {

	
	/**
	 * Gets a URL from given path using given base URL
	 * @param base Given base URL
	 * @param href Given path
	 * @return A URL of given path
	 * @throws MalformedURLException Cannot create URL instances
	 */
	public static String getUrlByHref(String base, String href) throws MalformedURLException {
		URL _base = new URL(base);
		URL _href = new URL(_base, href);
		return _href.toString();
	}
	
	/**
	 * Gets a filename from given URL
	 * @param url Given URL
	 * @return A filename
	 */
	public static String getFilename(String url) {
		File _url = new File(url);
		return _url.getName();
	}
	
	/**
	 * Gets a host from given URL
	 * @param url Given URL
	 * @return a host name
	 * @throws URISyntaxException
	 */
	public static String getHostName(String url) throws URISyntaxException {
		URI uri = new URI(url);
		return uri.getHost();
	}
	
	/**
	 * Utility for print an error message
	 * @param obj For including a place where an error occurred
	 * @param message An error message
	 */
	public static void printError(Object obj, String message) {
		System.err.println(obj.getClass().getName() + ": " + message);
	}
	
	/**
	 * Utility for print an error message with a debugging clue
	 * @param obj For including a place where an error occurred
	 * @param message An error message
	 * @param value A debugging clue
	 */
	public static void printError(Object obj, String message, String value) {
		System.err.println(obj.getClass().getName() + ": " + message + ", value is \"" + value + "\"");
	}
	
	
	public static String esc_dot(String str) {
		String ret = str;
		if(ret != null) {
			ret = ret.replaceAll("\\\\\"", "\"");			
			
			ret = ret.replaceAll("\"", "\\\\\"");
			ret = ret.replaceAll("\\.", "\\\\.");
			ret = ret.replaceAll("\n", " ");
		}
		return ret;
	}
	

	/*
	public static String esc_xml(String str) {
		String escaped = str;
		if(escaped != null) {
			escaped = escaped.replaceAll("&lt;", "<");
			escaped = escaped.replaceAll("&gt;", ">");
			escaped = escaped.replaceAll("&quot;", "\"");
			escaped = escaped.replaceAll("&apos;", "'");
			escaped = escaped.replaceAll("&amp;", "&");
			
			escaped = escaped.replaceAll("&", "&amp;");
			escaped = escaped.replaceAll("<", "&lt;");
			escaped = escaped.replaceAll(">", "&gt;");
			escaped = escaped.replaceAll("\"", "&quot;");
			escaped = escaped.replaceAll("'", "&apos;");
		}
		return escaped;
	}
	public static String esc_xml_rev(String str) {
		String xml = str;
		if(xml != null) {
			xml = xml.replaceAll("&apos;", "'");
			xml = xml.replaceAll("&quot;", "\"");
			xml = xml.replaceAll("&gt;", ">");
			xml = xml.replaceAll("&lt;", "<");
			xml = xml.replaceAll("&amp;", "&");
			
			xml = xml.replaceAll("&", "&amp;");
			xml = xml.replaceAll("'", "&apos;");
			xml = xml.replaceAll("\"", "&quot;");
			xml = xml.replaceAll(">", "&gt;");
			xml = xml.replaceAll("<", "&lt;");
		}
		return xml;
	}

	public static String removeQuote(String str) {
		String ret = str;
		if(str == null || "".equals(str)) {
			return "";
		}
		if(str.charAt(0) == '"' || str.charAt(0) == '\'') {
			ret = str.substring(1, str.length() - 1);
		}
		return ret;
	}
	public static String removeHtmlComment(String str) {
		if(str == null) {
			return "";
		}
		String ret = str;
		// remove <!-- and -->
		ret = ret.replaceAll("<!--", "");
		ret = ret.replaceAll("-->", "");
		// remove //<![CDATA[ and //]]>
		ret = ret.replaceAll("//<!\\[CDATA\\[", "");
		ret = ret.replaceAll("//\\]\\]>", "");
		return ret;
	}

	public static String getProtocol(String url) {
		if(url == null || !url.contains("//")) {
			System.err.println("Not URL: " + url);
		}
		int index = url.indexOf("//");
		return url.substring(0, index - 1);
	}
	public static String getDomain(String url) {
		if(url == null || !url.contains("//")) {
			System.err.println("Not URL: " + url);
		}
		int p_index = url.indexOf("//");
		String protocol = url.substring(0, p_index - 1);
		
		String domain = url.substring(p_index + 2);
		int d_index = domain.indexOf("/");
		if(0 < d_index) {
			domain = domain.substring(0, d_index);
		}
		return protocol + "://" + domain;
	}
	public static String getCurrentPath(String url) {
		if(url == null) {
			System.err.println("Not URL: " + url);
		}
		if(url.endsWith("/")) {
			return url;
		} else {
			int index = url.lastIndexOf("/");
			String cur = url.substring(0, index);
			return cur;
		}
	}
	*/
}
