package main;

import managers.CSSManager;
import managers.IFrameManager;
import managers.JavaScriptManager;
import models.Directory;
import models.HTMLPage;
import utils.HTMLFileWriter;


/**
 * Created by CarlSwann on 6/21/16.
 *
 * Ensures that sub-processes
 * are performed in logical order
 * Also stores a public reference
 * to the website directory object
 */
public class Coordinator {

    public static Directory website; //TODO: In a later revision refine this logic of using a static Directory Object. A bit  clunky.


    /**
     * recieves the website Directory from the main method
     * and coordinates the conversion process
     * at the highest level
     * @param websiteDirectory
     */
    public static void convertToSingleHTMLFile(Directory websiteDirectory) {

        website = websiteDirectory;

        System.out.println("Hard Coding Assets Into Pages...");
        for (HTMLPage page : websiteDirectory.getHTMLPages()) {
            page.hardCodeAssets();
        }

        String compiledJavaScript = JavaScriptManager.getCompiledJavaScript(websiteDirectory);

        String compiledCSS = CSSManager.getCompiledCSS(websiteDirectory);

        System.out.println("Compiling HTML Page...");

        String rawCompiledHTML = IFrameManager.getIFrame(compiledJavaScript, compiledCSS);

        System.out.println("Creating HTML File...");

        HTMLFileWriter.createHTMLFile(rawCompiledHTML);


    }



}
