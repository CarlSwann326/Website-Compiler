package managers;

import models.Directory;
import utils.Logger;

import java.io.*;
import java.util.List;

/**
 * Created by CarlSwann on 6/22/16.
 */
public class CSSManager {

    private static List<File> cssFiles;

    /**
     * receives a reference to the main website
     * directory object and returns the compiled
     * css files contained within.
     * @param websiteDirectory
     * @return
     */
    public static String getCompiledCSS(Directory websiteDirectory) {

        System.out.println("Compiling Styles from CSS Files...");

        cssFiles = websiteDirectory.getCSSFiles();
        return getStylesFromFiles();
    }

    /**
     * traverses the CSS files contained in
     * the directory object and returns the contents
     * formatted into style tags
     * @return
     */
    private static String getStylesFromFiles() {

        StringBuffer cssFileBuffer = new StringBuffer();

        for (File cssFile : cssFiles) {

            cssFileBuffer.append("<style>");

            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader br = null;

            String thisLine = null;
            try {

                is = new FileInputStream(cssFile);
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);


                while ((thisLine = br.readLine()) != null) {
                    cssFileBuffer.append(thisLine);
                }
            } catch (IOException e) {

                Logger.e(cssFile.getAbsolutePath(), "Error reading from this file : " + e.getMessage());

            } finally {

                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        Logger.e(cssFile.getAbsolutePath(), "Error closing InputStream");
                    }
                }
                if (isr != null) {
                    try {
                        isr.close();
                    } catch (IOException e) {
                        Logger.e(cssFile.getAbsolutePath(), "Error closing InputStreamReader");
                    }
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        Logger.e(cssFile.getAbsolutePath(), "Error closing BufferedReader");
                    }
                }
            }

            cssFileBuffer.append("</style>");
        }


        return cssFileBuffer.toString();
    }


}
