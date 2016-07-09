package handlers;

import main.Coordinator;
import models.HTMLPage;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/**
 * Created by CarlSwann on 6/21/16.
 */
public class LinkHandler {

    private static Document doc;

    /**
     * Coordinates the manipulation
     * of inline html references
     * @param page HTMLPage Object
     */
    public static void processReferences(HTMLPage page) {

        doc = page.getDoc();
        processSourceAttributeHolders();
        processHrefAttributeHolders();
        removeLinkTags();

    }

    /**
     * removes source attributes replacing them
     * with the appropriate javascript function calls
     * to load the referenced page
     */
    private static void processSourceAttributeHolders() {

        for (Element srcAttributeHolder : doc.getElementsByAttribute("src")) {

            for (HTMLPage page : Coordinator.website.getHTMLPages()) {

                if (srcAttributeHolder.attr("src") != null && srcAttributeHolder.attr("src").contains(page.getSourceFile().getName())) {

                    String javaScriptCall = "parent.load_" + page.getSourceFile().getName().replace(".html", "").replace(".", "") + "();";
                    srcAttributeHolder.removeAttr("src");
                    srcAttributeHolder.attr("onload", javaScriptCall);
                }
            }
        }
    }

    /**
     * replace href attributes with the
     * appropriate javascript function calls
     * to load the intended page
     */
    private static void processHrefAttributeHolders() {

        for (Element hrefAttributeHolder : doc.getElementsByAttribute("href")) {

            for (HTMLPage page : Coordinator.website.getHTMLPages()) {

                if (hrefAttributeHolder.attr("href").equals("javascript:history.back()")) {
                    hrefAttributeHolder.attr("href", "javascript:parent.goBack();");
                }

                if (hrefAttributeHolder.attr("href") != null && hrefAttributeHolder.attr("href").contains(page.getSourceFile().getName())) {

                    String javaScriptCall = "javascript:parent.load_" + page.getSourceFile().getName().replace(".html", "") + "();";
                    hrefAttributeHolder.attr("href", javaScriptCall);
                }
            }
        }
    }

    /**
     * removes link tags except for those containing
     * a link to  an online resource.
     */
    private static void removeLinkTags() {

        for (Element linkTag : doc.getElementsByTag("link")) {
            if (linkTag.attr("href") != null && !linkTag.attr("href").contains("http")) {

                linkTag.remove();
            }
        }
    }
}
