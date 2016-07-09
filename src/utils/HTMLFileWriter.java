package utils;

import config.ExecutionConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by CarlSwann on 6/22/16.
 */
public class HTMLFileWriter {

    /**
     * creates a html file from the given
     * source code in the Path defined by
     * the user
     *
     * @param html
     */
    public static void createHTMLFile(String html) {


        PrintWriter writer = null;
        File outputFile = new File(ExecutionConfig.getOutputPath(), ExecutionConfig.getOutputFileName() + ".html");
        outputFile.getParentFile().mkdirs();

        try {

            writer = new PrintWriter(outputFile);
            writer.print(html);
            writer.close();

        } catch (FileNotFoundException e) {
            Logger.e(outputFile.getAbsolutePath(), "Could not write to file");
            //Logger.d(ExceptionUtils.getStackTrace(e));
        }
    }
}
