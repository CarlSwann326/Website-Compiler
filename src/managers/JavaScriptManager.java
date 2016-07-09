package managers;

import models.Directory;
import models.HTMLPage;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import utils.JSHelper;
import utils.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CarlSwann on 6/21/16.
 * <p>
 * Handles all JavaScript tag creation
 * and accumulation
 */
public class JavaScriptManager {

    public static HashMap<String, String> functions;
    public static String nameDivId = "nameDiv";

    private static Document doc;
    private static StringBuffer mainBuffer;
    private static List<File> jsFiles;
    private static List<HTMLPage> htmlPages;


    /**
     * Coordinates the accumulation of Javascript dependencies
     * and returns the the website's Javascript dependencies
     * compiled into script tags.
     * @param websiteDirectory
     * @return
     */
    public static String getCompiledJavaScript(Directory websiteDirectory) {

        mainBuffer = new StringBuffer();
        jsFiles = websiteDirectory.getJSFiles();
        htmlPages = websiteDirectory.getHTMLPages();


        System.out.println("Compiling JavaScript..."); //TODO: Update to notify the user of ongoing compression once the feature has been implemented.

        mainBuffer.append(getBackButtonScripts());
        mainBuffer.append(getScriptsFromFiles());

        mainBuffer.append("\n<script>\n" +
                "\t\t\n" +
                "\t$(window).load(function() { \t\n" +
                "   \t\tload_index();\n" +
                "  \t});\n" +
                "function decodeHtml(html) {\n" +
                "    var txt = document.createElement(\"textarea\");\n" +
                "    txt.innerHTML = html;\n" +
                "    return txt.value;\n" +
                "}\n" +
                "\t</script>");


        functions = new HashMap<>();
        populateFunctionList(websiteDirectory);

        for (HTMLPage page : htmlPages) {
            removeOldScriptTags(page);
            mainBuffer.append(getPageGenerationScript(page));
        }

        return mainBuffer.toString();
    }

    /**
     * iterates through all script tags contained
     * in the passed HTMLPage object removing
     * those that reference js files contained in
     * the website directory.
     * @param page
     */
    private static void removeOldScriptTags(HTMLPage page) {

        for (Element scriptTag : page.getDoc().getElementsByTag("script")) {

            for (File jsFile : jsFiles) {

                if (scriptTag.attr("src") != null && scriptTag.attr("src").contains(jsFile.getName())) {

                    scriptTag.remove();
                }
            }
        }
    }

    /**
     * returns the Javascript contained
     * in the local files of the website directory
     * formatted into script tags
     * @return
     */
    private static String getScriptsFromFiles() {

        StringBuffer mainBuffer = new StringBuffer();

        for (File jsFile : jsFiles) {

            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            StringBuffer jsFileBuffer = new StringBuffer();

            String thisLine = null;
            jsFileBuffer.append("\n<script>\n");
            try {

                is = new FileInputStream(jsFile);
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);


                while ((thisLine = br.readLine()) != null) {

                    if (thisLine.contains("//")) { //If this line contains an inline comment do not append the comment
                        thisLine = thisLine.substring(0, thisLine.indexOf("//"));
                    }

                    jsFileBuffer.append(thisLine + "\n");
                }
            } catch (IOException e) {

                Logger.e(jsFile.getAbsolutePath(), "Error reading from this file : " + e.getMessage());

            } finally {

                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        Logger.e(jsFile.getAbsolutePath(), "Error closing InputStream");
                    }
                }
                if (isr != null) {
                    try {
                        isr.close();
                    } catch (IOException e) {
                        Logger.e(jsFile.getAbsolutePath(), "Error closing InputStreamReader");
                    }
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        Logger.e(jsFile.getAbsolutePath(), "Error closing BufferedReader");
                    }
                }
            }

            String script = jsFileBuffer.toString().replace("document", "iframeDoc");
            jsFileBuffer = new StringBuffer();
            jsFileBuffer.append(script);

            jsFileBuffer.insert(9, "\n\nvar iframe = document.getElementById('" + IFrameManager.Id + "');\n" +
                    "var iframeDoc = iframe.contentDocument || iframe.contentWindow.document;\n\n");
            jsFileBuffer.append("</script>");

            mainBuffer.append(jsFileBuffer.toString());

        }


        return mainBuffer.toString();
    }

    /**
     * populates the functions list variable to
     * later be used in the correcting of 'onload'
     * function calls
     * @param website
     */
    private static void populateFunctionList(Directory website) {

        for (HTMLPage page : website.getHTMLPages()) {

            String functionName = "load_" + page.getSourceFile().getName().replace(".html", "").replace(".", "") + "();";
            String function = "function " + functionName + " {"
                    + "var iframe = document.getElementById('" + IFrameManager.Id + "');\n" +
                    "iframe = iframe.contentWindow ? \n" +
                    "         iframe.contentWindow : \n" +
                    "         (\n" +
                    "             iframe.contentDocument.document ?\n" +
                    "             iframe.contentDocument.document : \n" +
                    "             iframe.contentDocument\n" +
                    "         );\n" +
                    "iframe.document.open();\n" +
                    "iframe.document.write(\"" + cleanHTMLString(addFunctionBodyToOnLoadFunctionCalls(page)) + "\");\n" +
                    "iframe.document.close();"
                    + "}";

            functions.put(functionName, function);
        }

    }

    /**
     * returns the Javascript tag necessary to load the
     * passed HTMLPage Object into the main iframe
     * @param page
     * @return
     */
    private static String getPageGenerationScript(HTMLPage page) {

        cleanAttributes(page);

        StringBuffer generationScript = new StringBuffer();
        generationScript.append("\n<script>");

        String encodedPage =  cleanHTMLString(addFunctionBodyToOnLoadFunctionCalls(page));

        String functionName = "load_" + page.getSourceFile().getName().replace(".html", "").replace(".", "") + "()";
        String function = JSHelper.getPageLoadingFunction(functionName , encodedPage, page);

        generationScript.append(function);
        generationScript.append("</script>");


        return generationScript.toString();
    }

    /**
     * finds 'onload' attributes that have been defined
     * earlier by the link manager and populates the function
     * body of the call
     * @param page
     * @return
     */
    private static String addFunctionBodyToOnLoadFunctionCalls(HTMLPage page) {

        if (page.getDoc().getElementsByAttribute("onload") != null) {

            for (Element element : page.getDoc().getElementsByAttribute("onload")) {

                String functionCall = element.attr("onload");

                for (Map.Entry entry : functions.entrySet()) {

                    if (functionCall.contains((String) entry.getKey())) {

                        element.attr("onload", "parent." + entry.getKey());
                    }
                }
            }
        }

        return cleanHTMLString(page.getDoc().toString());
    }

    /**
     * cleans the passed html string of
     * characters that would otherwise cause
     * the browser to incorrectly parse the HTML
     * @param htmlString
     * @return
     */
    private static String cleanHTMLString(String htmlString) {


        return htmlString
                .replace("\"", "'")
                .replace("</script", "</scr\\ipt")
                .replace("\t", " ")
                .replace("\n", " ")
                .replace("\r", " ")
                .replace("\r\n", " ")
                ;
    }

    /**
     * encodes instances of single quotes every attribute value contained
     * in the page except for style and href tags and updates javascript
     * function calls to reference scripts contained in the parent of the
     * iframe. Fixes issues with inline javascript not functioning correctly
     * @param page
     */
    private static void cleanAttributes(HTMLPage page) {

        Document doc = page.getDoc();

        for (Element e : doc.select("*")) {

            Attributes attributes = e.attributes();

            for (Attribute attribute : attributes) {
                if (attribute.getKey().equals("style") || attribute.getKey().equals("href")) {
                    continue;
                }

                String value = attribute.getValue();
                attribute.setValue(value.replace("'", "&#39;"));

                for (int i = 0; i < attribute.getValue().length(); i++) {

                    if (attribute.getValue().charAt(i) == '(') {

                        int jumpDistance = 7;
                        for (int x = i; x >= 0; x--) {

                            if (attribute.getValue().charAt(x) == ' ' || attribute.getValue().charAt(x) == ';' || x == 0) {

                                if (attribute.getValue().indexOf("parent.", x) == x) {
                                    i += jumpDistance;
                                    break;
                                }

                                int offset = 0;
                                if (x != 0) offset++;

                                attribute.setValue(new StringBuilder(attribute.getValue()).insert(x + offset, "parent.").toString());
                                i += jumpDistance;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }


    private static String getBackButtonScripts(){

        return "<script>" +
                "function logNavigation(functionName){\n" +
                "\n" +
                "  if(typeof(Storage) !== \"undefined\") {\n" +
                "\n" +
                "\n" +
                "        if (sessionStorage.getItem('loadStack') == null) {\n" +
                "\n" +
                "        \tvar newStack = new Array(functionName);\n" +
                "\t\t    sessionStorage.setItem('loadStack', JSON.stringify(newStack));\n" +
                "          \n" +
                "\n" +
                "        } else {\n" +
                "\n" +
                "        \tvar currentStack = JSON.parse(sessionStorage.getItem('loadStack'));\n" +
                "        \tcurrentStack.push(functionName);\n" +
                "        \tsessionStorage.setItem('loadStack', JSON.stringify(currentStack));\n" +
                "\n" +
                "        }    \n" +
                "\n" +
                "    }     \n" +
                "}\n" +
                "\n" +
                "function goBack(){\n" +
                "\n" +
                " if(typeof(Storage) !== \"undefined\") {\n" +
                "\n" +
                "\t\tvar loadStack = JSON.parse(sessionStorage.getItem('loadStack'));\n" +
                "\n" +
                "\t\tvar fn = window[loadStack[loadStack.length - 2]];\n" +
                "\n" + "if (typeof fn === \"function\"){ fn(); \nloadStack.pop();\n" +
                "}" +
                "\nsessionStorage.setItem('loadStack', JSON.stringify(loadStack));" +
                "}\n" +
                "}"+
                "</script>";
    }








}
