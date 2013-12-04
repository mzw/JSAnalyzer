package jp.mzw.jsanalyzer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import jp.mzw.jsanalyzer.config.FilePath;

public class TextFileUtils {
	/**
	 * Retrieves target document via Internet
	 * @param url is a URL of target document
	 * @return Target document
	 */
	public static String wget(String url) {
		String ret = "";
		try {
			URL target = new URL(url);
			InputStreamReader isr = new InputStreamReader(target.openStream());
			BufferedReader br = new BufferedReader(isr);
			String tmp_str = "";
			while((tmp_str = br.readLine()) != null) {
				ret += tmp_str + "\n";
			}
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return ret;
	}
	
	/**
	 * Retrieves target document via local host
	 * @param filename is a file path of target document
	 * @return Target document
	 */
	public static String cat(String filename) {
		String src = "";
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String tmp_str = "";
			while((tmp_str = br.readLine()) != null) {
				src += tmp_str + "\n";
			}
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}	
		return src;
	}
	
	/**
	 * Retrieves lines which matches given regular expression
	 * @param content is a text which may contains the lines 
	 * @param regex is the regular expression
	 * @return Matched lines
	 */
	public static ArrayList<String> grep(String content, String regex) {
		ArrayList<String> ret = new ArrayList<String>();
		
		String tmp_line = "";
		for(int i = 0; i < content.length(); i++) {
			int n = content.indexOf('\n');
			if(n != -1) {
				tmp_line = content.substring(0, n);
				content = content.substring(n+1, content.length()-1);
				if(tmp_line.matches(regex)) {
					ret.add(tmp_line);
				}
			}
		}
		return ret;
	}
	
	/**
	 * Writes a text in file system
	 * @param dir is a parent directory
	 * @param filename is a name of the text file
	 * @param content is a content to be written
	 */
	public static void write(String dir, String filename, String content) {
		File _dir = new File(dir);
		_dir.mkdirs();
		write(new File(dir, filename), content);
	}
	
	/**
	 * Writes a text in file system
	 * @param filename is a file path
	 * @param content is a content to be written
	 */
	public static void write(String filename, String content) {
		write(new File(filename), content);
	}
	
	/**
	 * Writes a text in file system
	 * @param file is a File instance
	 * @param content is a content to be written
	 */
	public static void write(File file, String content) {
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(content);
			fw.close();
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * Stores the source code in this project directory
	 * @param codeUrl A URL of the source code file
	 * @param projDir A project directory
	 * @param code The source code
	 * @throws URISyntaxException Cannot resolve give URL
	 */
	public static void storeRawCode(String codeUrl, String projDir, String code) throws URISyntaxException {
		URI uri = new URI(codeUrl);
		File file = new File(projDir + FilePath.RawSrcDir + uri.getPath());
		TextFileUtils.write(file.getParent().toString(), file.getName(), code);
	}

}