package jp.mzw.jsanalyzer.preventer.insert_delay_ajax;

import com.google.common.base.Preconditions;

/**
 * JavaScriptの場所を指し示すためのデータクラス
 * JavaScriptは1つのファイルとして存在する(LocationType.INDEPENDENT_FILE)もしくは
 * HTMLの中のscriptタグ中にインラインで書かれている(LocationType.INLINE)とする．
 */

/**
 * @author Kazuki Nishiura
 */
public class JavaScriptLocation {
    /**
     * JavaScriptがどこにあるのかを示すための型
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
     * @return 1つのJavaScriptファイルに対応したインスタンス
     */
    static public JavaScriptLocation jsFile(String pathToJsFile) {
        return new JavaScriptLocation(
                LocationType.INDEPENDENT_FILE, pathToJsFile);
    }

    /**
     * @param pathToHtmlFile JavaScriptを含むHTMLファイルへのパス
     * @param scriptTagIndex 対象とするscriptタグは，HTML中の何番目(0はじまり)の
     *                       scriptタグであるか
     * @return HTML中のインラインJavaScriptに対応したインスタンス
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
     * @return JavaScriptが存在するファイルのパス
     */
    public String getFilePath() {
        return pathToFile;
    }

    /**
     * @return HTML中のインラインJavaScriptに対して，それがHTMLファイル中の何番目の
     * scriptタグの中にあるかを表す整数値(0はじまり)を返す．
     */
    public int getIndexInHtml() {
        return indexInHtml;
    }
}