package utils;

import managers.IFrameManager;
import models.HTMLPage;

/**
 * Created by CarlSwann on 6/27/16.
 */
public class JSHelper {

    /**
     * returns the body of the function necessary to load
     * the html page into the main iframe.
     * @param functionName
     * @param html
     * @return
     */
    public static String getPageLoadingFunction(String functionName , String html, HTMLPage page){

        return "function " + functionName + " {\n"
                + "var iframe = document.getElementById('" + IFrameManager.Id + "');\n"
                + "var html = decodeHtml(\"" + encodeHTMLString(html) + "\");\n"
                +
                "iframe = iframe.contentWindow ? \n" +
                "         iframe.contentWindow : \n" +
                "         (\n" +
                "             iframe.contentDocument.document ?\n" +
                "             iframe.contentDocument.document : \n" +
                "             iframe.contentDocument\n" +
                "         );\n" +
                "iframe.document.open();\n" +
                "iframe.document.write("
                + "html"
                + ");\n" +
                "iframe.document.close();"
                + "\n" +
                "\nlogNavigation('load_"+page.getSourceFile().getName().replace(".html", "").replace(".", "")+"');\n" +
                "}";
    }

    /**
     * Anthony's code will go here
     * use the LZW string compression algorithm to compress
     * the passed string and return the result.
     * Don't forget to modify the function in the method above so
     * that it will be able to decompress the string back into it's
     * original format.
     *
     * @param htmlString
     * @return
     */
    private static String encodeHTMLString(String htmlString) {




        return htmlString;
    }
}
