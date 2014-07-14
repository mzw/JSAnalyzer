package jp.mzw.jsanalyzer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implemented for indexing applications, browsers, etc by using Version
 * @author Yuta Maezawa
 */

public class VersionUtils {
	/**
	 * Logger
	 */
	protected static final Logger LOG = LoggerFactory.getLogger(VersionUtils.class);
	
	/**
	 * Represents major version
	 */
	private int major;
	
	/**
	 * Represents minor version
	 */
	private int minor;
	
	/**
	 * Represents revision
	 */
	private int revision;
	
	/**
	 * Constructor
	 * @param major
	 * @param minor
	 * @param revision
	 */
	private VersionUtils(int major, int minor, int revision) {
		this.major = major;
		this.minor = minor;
		this.revision = revision;
	}
	
	/**
	 * Gets major version
	 * @return major version
	 */
	public int getMajor() {
		return this.major;
	}
	
	/**
	 * Gets minor version
	 * @return minor version
	 */
	public int getMinor() {
		return this.minor;
	}
	
	/**
	 * Gets revision
	 * @return
	 */
	public int getRevision() {
		return this.revision;
	}
	
	
	/**
	 * Instantiates Version class
	 * @param major
	 * @param minor
	 * @param revision
	 * @return
	 */
	public static VersionUtils get(int major, int minor, int revision) {
		return new VersionUtils(major, minor, revision);
	}
	
	/**
	 * Determines whether this equals to given version instance
	 * @param ver
	 * @return
	 */
	public boolean equals(VersionUtils ver) {
		if(ver == null) {
			LOG.error("Given version is null");
			return false;
		}
		if(this.major == ver.getMajor() &&
				this.minor == ver.getMinor() &&
				this.revision == ver.getRevision()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Determines whether this equals to given version parameters
	 * @param major
	 * @param minor
	 * @param revision
	 * @return
	 */
	public boolean equals(int major, int minor, int revision) {
		if(major < 0 || minor < 0 || revision < 0) {
			LOG.error("Invalid given version params: {}.{}.{}", major, minor, revision);
			return false;
		}
		if(this.major == major && this.minor == minor && this.revision == revision) {
			return true;
		}
		return false;
	}
	
}
