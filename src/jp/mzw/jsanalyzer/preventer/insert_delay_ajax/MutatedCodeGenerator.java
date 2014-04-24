package jp.mzw.jsanalyzer.preventer.insert_delay_ajax;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;
import com.google.common.io.Resources;
//import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import jp.mzw.jsanalyzer.util.TextFileUtils;

/**
 * @author Kazuki Nishiura
 */
public class MutatedCodeGenerator {

    private static final String JS_LIBARY_PATH = "res/delay/delayedRequest.js";
    private static final String JS_PATH_LOAD_JS_LITERAL_WITH_DELAY
            = "/loadJsLiteralWithDelay.js";
    private static final String JS_PATH_LOAD_JS_SRC_WITH_DELAY
            = "/loadJsSrcWithDelay.js";
    private static final String DELAYED_REQUEST_OBJECT_CONSTRUCTOR
            = "new DelayedRequest(%d)";
    // Functions that have corresponding implementation in our script
    private static final Map<String, String> FUNCTION_MAP = ImmutableMap.of(
            "new Ajax.Request", "prototypeJs_Ajax_Request");
    // Functions that can be dealt with our default delayed function.
    private static final Set<String> FUNCTIONS_WITH_DEFAULT = ImmutableSet.of(
            "$.ajax", "$.get", "$.post");
    private static final String DEFAULT_DELAYED_APPLY = "applyFunction";
    private static final String LIBRARY_HEADER
            = "//----- beginning of auto inserted library code ---";
    @VisibleForTesting
    protected static final String LIBRARY_FOOTER
            = "//----- end of auto inserted library code ---";
    private static final String ATTRIBUTE_FOR_INSERTING_DELAY
            = "data-annotation-insert-delay";
    private static final String JS_ANNOTATION_FOR_INSERTING_DELAY
            = "/* auto-generated comment (do not delete), @delayWillBeInserted here when testing */";

    /**
     * Given JavaScript and position information of the target function, this
     * method 'mutate' the function to insert a artificial delay.
     *
     * @param javaScriptLocation location of the JavaScript file
     * @param absoluteStartPosition start position of the mutation point from
     *                              the beginning of code block.
     * @param delayInMillis delay in milliseconds.
     * @return mutated code (either JavaScript or HTML including script tag).
     */
    public String insertDelayedRequestObject(
            JavaScriptLocation javaScriptLocation, int absoluteStartPosition,
            int delayInMillis) {
        String contentsOfFile = readFile(javaScriptLocation.getFilePath());
        switch (javaScriptLocation.getLocationType()) {
            case INDEPENDENT_FILE: {
                OutputStrings output = insertDelayedRequestObjectIntoJavaScript(
                        contentsOfFile, absoluteStartPosition, delayInMillis);

                // Insert annotation to indicate the point to insert delay.
                writeToFile(output.contentsForDebug, javaScriptLocation.getFilePath());
                return output.contentsForTest;
            }
            case INLINE: {
                Document document = Jsoup.parse(contentsOfFile);
                List<Element> scriptTags = document.getElementsByTag("script");
                if (scriptTags.size() <= javaScriptLocation.getIndexInHtml()) {
                    throw new IllegalArgumentException(
                            javaScriptLocation.getIndexInHtml() + "' s script tag is not exist.");
                }
                Element scriptTag = scriptTags.get(javaScriptLocation.getIndexInHtml());
                OutputStrings output = insertDelayedRequestObjectIntoJavaScript(
                        scriptTag.data(), absoluteStartPosition, delayInMillis);

                // Insert annotation to indicate the point to insert delay.
                JsoupUtil.updateData(scriptTag, output.contentsForDebug);
                writeToFile(document.toString(), javaScriptLocation.getFilePath());

                JsoupUtil.updateData(scriptTag, output.contentsForTest);
                return document.toString();
            }
            default:
                throw new IllegalStateException("Unknown location type "
                        + javaScriptLocation.getLocationType());
        }
    }

    /**
     * Modify part of the HTML to emulate the situation where content of
     * certain script tag is loaded with delay.
     *
     * @param pathToHtml path to the HTML file
     * @param scriptTagIndex the index of the target script tag in the HTML
     * @param delayInMillis delay in milliseconds
     * @return mutated HTML code
     */
    public String modifyScriptTagForDelayedLoad(
            String pathToHtml, int scriptTagIndex, int delayInMillis) {
        String html = readFile(pathToHtml);
        Document document = Jsoup.parse(html);
        List<Element> scriptTags = document.getElementsByTag("script");
        Element scriptTag = Iterables.get(scriptTags, scriptTagIndex);

        // Insert extra attribute to indicate the point to insert delay.
        scriptTag.attr(ATTRIBUTE_FOR_INSERTING_DELAY, "true");
        writeToFile(document.toString(), pathToHtml);

                StringBuilder newScriptBuilder = new StringBuilder();
        newScriptBuilder.append("<script type='text/javascript'>")
                .append(System.getProperty("line.separator"));
        String scriptSrc = scriptTag.attr("src");
        if (scriptSrc != null && scriptSrc.length() > 0) {
            newScriptBuilder.append(readResource(JS_PATH_LOAD_JS_SRC_WITH_DELAY))
                    .append(System.getProperty("line.separator"))
                    .append("setTimeout(")
                    .append("loadSrcWithDelay.bind(this, '")
                    .append(scriptSrc)
                    .append("'), ")
                    .append(delayInMillis)
                    .append(");");
        } else {
            String escapedSource
                    = StringEscapeUtils.escapeEcmaScript(scriptTag.data());
            newScriptBuilder.append(readResource(JS_PATH_LOAD_JS_LITERAL_WITH_DELAY))
                    .append(System.getProperty("line.separator"))
                    .append("setTimeout(")
                    .append("loadJsLiteralWithDelay.bind(this, '")
                    .append(escapedSource)
                    .append("'), ")
                    .append(delayInMillis)
                    .append(");");
        }
        newScriptBuilder.append(System.getProperty("line.separator")).append("</script>");
        scriptTag.after(newScriptBuilder.toString());
        scriptTag.remove();

        return document.toString();
    }

    static public void cleanupOurAnnotationFromJs(String jsFilePath) {
        String content = readFile(jsFilePath);
        writeToFile(content.replace(JS_ANNOTATION_FOR_INSERTING_DELAY, ""), jsFilePath);
    }

    static public void cleanupOurDataAttrFromHtml(String htmlPath) {
        String content = readFile(htmlPath);
        Document doc = Jsoup.parse(content);
        Elements scripts = doc.getElementsByTag("script");
        for (Element script: scripts) {
            script.removeAttr(JS_ANNOTATION_FOR_INSERTING_DELAY);
        }
        writeToFile(doc.toString(), htmlPath);
    }

    static public void cleanupOurDataAnnotationFromInlineJs(String htmlPath) {
        String content = readFile(htmlPath);
        Document doc = Jsoup.parse(content);
        Elements scripts = doc.getElementsByTag("script");
        for (Element script: scripts) {
            String data = script.data();
            if (data.length() == 0) {
                continue;
            }
            data = data.replace(JS_ANNOTATION_FOR_INSERTING_DELAY, "");
            JsoupUtil.updateData(script, data);
        }
        writeToFile(doc.toString(), htmlPath);
    }

    private OutputStrings insertDelayedRequestObjectIntoJavaScript(
            String javaScript, int absoluteStartPosition, int delayInMillis) {
        OutputStrings output = new OutputStrings();
        Replacement replacement = searchForMatch(javaScript, absoluteStartPosition);
        if (replacement == null) {
            throw new IllegalStateException(
                    "Cannot find target at position " + absoluteStartPosition);
        }
        String modified = String.format(DELAYED_REQUEST_OBJECT_CONSTRUCTOR, delayInMillis)
                + "." + replacement.replaced;

        output.contentsForDebug
                = javaScript.substring(0, absoluteStartPosition)
                + JS_ANNOTATION_FOR_INSERTING_DELAY
                + javaScript.substring(absoluteStartPosition);
        
        output.contentsForTest = libraryJsToString()
                + javaScript.substring(0, absoluteStartPosition)
                + modified
                + javaScript.substring(absoluteStartPosition + replacement.original.length());
        return output;
    }

    private String insertDelayedRequestObjectIntoInlineJavaScript(
            String html, int scriptTagIndex, int absoluteStartPosition,
            int delayInMillis) {
        Document document = Jsoup.parse(html);
        List<Element> scriptTags = document.getElementsByTag("script");
        if (scriptTags.size() <= scriptTagIndex) {
            throw new IllegalArgumentException(
                    scriptTagIndex + "' s script tag is not exist.");
        }
        Element scriptTag = scriptTags.get(scriptTagIndex);
        OutputStrings output = insertDelayedRequestObjectIntoJavaScript(
                scriptTag.data(), absoluteStartPosition, delayInMillis);

        JsoupUtil.updateData(scriptTag, output.contentsForDebug);
        return document.toString();
    }

    private Replacement searchForMatch(String javaScript, int absoluteStartPosition) {
        Set<String> matchTargets = new HashSet<String>(FUNCTION_MAP.keySet());
        StringBuilder subStringBuilder = new StringBuilder();
        for (int i = absoluteStartPosition; i < javaScript.length(); i++) {
            subStringBuilder.append(javaScript.charAt(i));
            String subString = subStringBuilder.toString();
            for (String matchTarget: matchTargets) {
                if (matchTarget.equals(subString)) {
                    return new Replacement(matchTarget, FUNCTION_MAP.get(matchTarget));
                }
            }
            for (String matchTarget: FUNCTIONS_WITH_DEFAULT) {
                if (matchTarget.equals(subString)
                        && (i < javaScript.length() - 1) && '(' == javaScript.charAt(i + 1)) {
                    return new Replacement(matchTarget + '(',
                            DEFAULT_DELAYED_APPLY + '(' + matchTarget + ", ");
                }
            }
        }
        return null;
    }

    private static void writeToFile(CharSequence content, String path) throws IllegalArgumentException {
        try {
            Files.write(content, new File(path), Charset.defaultCharset());
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot write file '" + path + "'", e);
        }
    }

//    @NotNull
    private static String readFile(String path) throws IllegalArgumentException {
        try {
            File targetFile = new File(path);
            return Files.toString(targetFile, Charset.defaultCharset());
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot read file '" + path + "'", e);
        }
    }

    private String readResource(String resourcePath) throws IllegalArgumentException {
        try {
            return Resources.toString(
                    this.getClass().getResource(resourcePath),
                    Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Cannot read resource '" + resourcePath + "'", e);
        }
    }

    private String libraryJsToString() {
        return LIBRARY_HEADER
                + System.getProperty("line.separator")
//                + readResource(JS_LIBARY_PATH)
                + TextFileUtils.cat(JS_LIBARY_PATH)
                + System.getProperty("line.separator")
                + LIBRARY_FOOTER
                + System.getProperty("line.separator");
    }

    /**
     * Class that represents output for inserting delay method.
     */
    private class OutputStrings {
        // content that contain annotation, but semantically same as original program, used for
        // debugging
        private String contentsForDebug;
        // content that include artificial delay so that test can expose faults.
        private String contentsForTest;
    }

    /**
     * Class that represent replacement.
     */
    public class Replacement {
        private final String original;
        private final String replaced;
        public Replacement(String original, String replaced) {
            this.original = original;
            this.replaced = replaced;
        }
    }
}