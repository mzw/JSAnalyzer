package Analyzer;

import att.grappa.Graph;
import att.grappa.Node;
import att.grappa.Parser;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Util {
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
	public static List<String> grep(String content, String regex) {
		List<String> ret = new LinkedList<String>();
		
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
	public static void write(String dir, String filename, String content) {
		write(new File(dir, filename), content);
	}
	public static void write(String filename, String content) {
		write(new File(filename), content);
	}
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
	public static String getFilenameFromURL(String url, String ext) {
		String[] split = url.split("/");
		return split[split.length-1] + ext;
	}
	public static int exec(String dir, String[] cmd) throws IOException, InterruptedException {
		Process proc = null;
		int proc_result = 0;
		try {
			proc = Runtime.getRuntime().exec(cmd, null, new File(dir));
			proc_result = proc.waitFor();
		} finally {
			if(proc != null) {
				System.out.print(Util.readStdOut(proc));
				System.err.print(Util.readStdErr(proc));
				
				proc.getErrorStream().close();
				proc.getInputStream().close();
				proc.getOutputStream().close();
				proc.destroy();
				
				return proc_result;
			}
		}
		return proc_result;
	}
	
	// print
	public static String readStdOut(Process process) {
		String ret = "";
		try {
			InputStream in = process.getInputStream();
			BufferedReader br_in = new BufferedReader(new InputStreamReader(in));
			String line_in = br_in.readLine();
			while(line_in != null) {
				ret += line_in + "\n";
				line_in = br_in.readLine();
			}
			br_in.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	public static String readStdErr(Process process) {
		String ret = "";
		try {
			InputStream err = process.getErrorStream();
			BufferedReader br_err = new BufferedReader(new InputStreamReader(err));
			String line_err = br_err.readLine();
			while(line_err != null) {
				ret += line_err + "\n";
				line_err = br_err.readLine();
			}
			br_err.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	

	// escape
	public static String esc_xml(String str) {
		String ret = str;
		if(ret != null) {
			ret = ret.replaceAll("&lt;", "<");
			ret = ret.replaceAll("&gt;", ">");
			ret = ret.replaceAll("&quot;", "\"");
			ret = ret.replaceAll("&apos;", "'");
			ret = ret.replaceAll("&amp;", "&");
			
			ret = ret.replaceAll("&", "&amp;");
			ret = ret.replaceAll("<", "&lt;");
			ret = ret.replaceAll(">", "&gt;");
			ret = ret.replaceAll("\"", "&quot;");
			ret = ret.replaceAll("'", "&apos;");
		}
		return ret;
	}
	public static String esc_xml_rev(String str) {
		String ret = str;
		if(ret != null) {
			ret = ret.replaceAll("&apos;", "'");
			ret = ret.replaceAll("&quot;", "\"");
			ret = ret.replaceAll("&gt;", ">");
			ret = ret.replaceAll("&lt;", "<");
			ret = ret.replaceAll("&amp;", "&");
			
			ret = ret.replaceAll("&", "&amp;");
			ret = ret.replaceAll("'", "&apos;");
			ret = ret.replaceAll("\"", "&quot;");
			ret = ret.replaceAll(">", "&gt;");
			ret = ret.replaceAll("<", "&lt;");
			
		}
		return ret;
	}
	public static String esc_uppaal_state(String str) {
		String ret = str;
		if(ret != null) {
			ret = ret.replaceAll("\\.", "__dot__");
		}
		return ret;
	}
	public static String esc_uppaal(String str) {
		String ret = str;
		if(ret != null) {
			ret = ret.replaceAll(" ", "_");
			//ret = ret.replaceAll("[0-9]", "N");
			ret = ret.replaceAll("\\(", "__par_s__"); // parentheses
			ret = ret.replaceAll("\\)", "__par_e__");
		}
		return ret;
	}
	public static String esc_spin(String str) {
		String ret = str;
		if(ret != null) {
			ret = ret.replaceAll(" ", "_");
			ret = ret.replaceAll("\\.", "_");
			ret = ret.replaceAll("\\(", "_"); // parentheses
			ret = ret.replaceAll("\\)", "_");
			ret = ret.replaceAll("init", "_init");
		}
		return ret;
	}
	public static String esc_spec(String str) {
		String ret = str;
		if(ret != null) {
			ret = ret.replaceAll("\n", "");
			ret = ret.replaceAll("\t.", " ");
		}
		return ret;
		
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

	public static String esc_dot_rev(String str) {
		String ret = str;
		if(ret != null) {
			ret = ret.replaceAll("\"", "\\\\\"");
			
			ret = ret.replaceAll("\\\\\"", "\"");			
		}
		return ret;
	}
	public static String removeQuote(String str) {
		String ret = "";
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
	public static String getUrlByHref(String base, String href) {
		if(href.startsWith("http://") || href.startsWith("https://")) {
			return href;
		} else {
			if(href.startsWith("//")) {
				// this.URI is "//domain/path/to/service" -> "http://domain/path/to/service"
				return Util.getProtocol(base) + ":" +  href;
			} else if(href.startsWith("/")) {
				// this.URI is "http://domain/path/to/service"
				return Util.getDomain(base) + href;
			} else {
				return Util.getCurrentPath(base) + "/" + href;
			}
		}
	}
	
	public static String getFilename(String path) {
		if(path == null || "".equals(path)) {
			return null;
		}
		path = "/" + path;
		int index = path.lastIndexOf("/");
		return path.substring(index+1, path.length());
	}
	

	public static HashMap<String, Point> getStateLayout(String dir, String filename) throws Exception {
		Process proc = null;
		String[] cmd = { Config.dot, filename };
		HashMap<String, Point> ret = new HashMap<String, Point>(); 
		try {
			proc = Runtime.getRuntime().exec(cmd, null, new File(dir));
			//int proc_result = proc.waitFor();
			proc.waitFor();
		} finally {
			if(proc != null) {
				String stdout = Util.readStdOut(proc);
				
				ByteArrayInputStream bais = new ByteArrayInputStream(stdout.getBytes("UTF-8"));
				Parser parser = new Parser(bais, System.err);
				parser.parse();
				Graph graph = parser.getGraph();
				Iterator<Node> nodes = graph.nodeElements();
				double max_ypos = Double.MIN_VALUE;
				while(nodes.hasNext()) {
					 Node node = nodes.next();
					 String str_pos = node.getAttribute("pos").getStringValue();
					 String[] _str_pos = str_pos.split(",");
					 double ypos = Double.parseDouble(_str_pos[1]);
					 if(max_ypos < ypos) {
						 max_ypos = ypos;
					 }
				}
				nodes = graph.nodeElements();
				while(nodes.hasNext()) {
					 Node node = nodes.next();
					 
					 String str_pos = node.getAttribute("pos").getStringValue();
					 String[] _str_pos = str_pos.split(",");
					 double xpos = Double.parseDouble(_str_pos[0]);
					 double ypos = Double.parseDouble(_str_pos[1]);
					 
					 Point pos = new Point();
					 pos.setLocation(xpos, max_ypos - ypos);
					 
					 if(node.getAttribute("id") == null) {
						 node.setAttribute("id", node.getName());
					 }
					 ret.put(node.getAttribute("id").getStringValue(), pos);
				}
				proc.getErrorStream().close();
				proc.getInputStream().close();
				proc.getOutputStream().close();
				proc.destroy();
			}
		}
		return ret;
	}
	
	public static List<String> getFilenames(String dir) {
		List<String> ret = new LinkedList<String>();
		for(File file : (new File(dir)).listFiles()) {
			ret.add(file.toString());
		}
		return ret;
	}
	
	public static int calcLoC(String code) {
		int ret = 0;
		for(int i = 0; i < code.length(); i++) {
			int c = code.charAt(i);
			if(c == '\n') {
				++ret;
			}
		}
		return ret;
	}

	public static final String SYSTEM_DIR = System.getProperty("file.separator");
	public static final String SYSTEM_LINE = System.getProperty("line.separator");
}
