package jp.mzw.jsanalyzer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandLineUtils {

	public static final long Timeout = 3000;
	public static class TimeoutThread extends Thread {
		private Thread mParent;
		public TimeoutThread(Thread parent) {
			mParent = parent;
		}
		public void run() {
			try {
				Thread.sleep(Timeout);
				mParent.interrupt();
			} catch (InterruptedException e) {
				// NOP
			}
		}
	}
	
	public static int exec(String dir, String[] cmd) throws IOException, InterruptedException {
		Thread timeout = new TimeoutThread(Thread.currentThread());
		timeout.start();
		
		Process proc = null;
		int proc_result = 0;
		try {
			proc = Runtime.getRuntime().exec(cmd, null, new File(dir));
			
			try {
				proc_result = proc.waitFor();
				timeout.interrupt();
			} catch (InterruptedException e) {
				// NOP
			}
		} finally {
			if(proc != null) {
				System.out.print(readStdOut(proc));
				System.err.print(readStdErr(proc));
				
				proc.getErrorStream().close();
				proc.getInputStream().close();
				proc.getOutputStream().close();
				proc.destroy();
				
				return proc_result;
			}
		}
		return proc_result;
	}
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
}
