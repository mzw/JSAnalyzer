package jp.mzw.jsanalyzer.preventer.insert_delay_ajax;

import com.google.common.base.Preconditions;

/**
 * Data class for indicating JavaScript locations
 * LocationType.INDEPENDENT_FILE: JavaScript is located at an external file. Otherwise,
 * LocationType.INLINE: JavaScript is located in an inline of HTML script
 */

/**
 * @author Kazuki Nishiura
 */
public class JavaScriptLocation {
    /**
     * Types for indicating JavaScript locations
     */
    public enum LocationType {INDEPENDENT_FILE, INLINE};

    private final LocationType locationType;
    private final String pathToFile;
    private int indexInHtml;

    private JavaScriptLocation(LocationType locationType, String pathToFile) {
        Preconditions.checkNotNull(locationType);
        Preconditions.checkNotNull(pathToFile);
        this.locationType = locationType;
        this.pathToFile = pathToFile;
    }

    /**
     * @return An instance corresponding to an external JavaScript file
     */
    static public JavaScriptLocation jsFile(String pathToJsFile) {
        return new JavaScriptLocation(
                LocationType.INDEPENDENT_FILE, pathToJsFile);
    }

    /**
     * @param pathToHtmlFile A path to a HTML file containing JavaScript
     * @param scriptTagIndex Represents an index order of the target script tag from all scripts in HTML
     * @return An instance corresponding to an HTML-script-inline JavaScript
     */
    static public JavaScriptLocation inHtmlFile(
            String pathToHtmlFile, int scriptTagIndex) {
        JavaScriptLocation instance = new JavaScriptLocation(
                LocationType.INLINE, pathToHtmlFile);
        instance.indexInHtml = scriptTagIndex;
        return instance;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    /**
     * @return A path to existing JavaScript
     */
    public String getFilePath() {
        return pathToFile;
    }

    /**
     * @return An index order of this JavaScript from all HTML-inline JavaScripts
     */
    public int getIndexInHtml() {
        return indexInHtml;
    }
}