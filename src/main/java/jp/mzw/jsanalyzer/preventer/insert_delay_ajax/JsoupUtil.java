package jp.mzw.jsanalyzer.preventer.insert_delay_ajax;

import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * @author Kazuki Nishiura
 */
public class JsoupUtil {
    /**
     * Update given element's data to new data.
     */
    public static Element updateData(Element element, String data) {
        List<DataNode> dataNodes = element.dataNodes();
        for (DataNode dataNode: dataNodes) {
            dataNode.setWholeData("");
        }
        dataNodes.get(0).setWholeData(data);
        return element;
    }
}