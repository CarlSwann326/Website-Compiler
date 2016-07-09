package config;

/**
 * Created by CarlSwann on 10/06/2016.
 * Stores relevant report variables
 */
public class ExecutionConfig {


    private static String OUTPUT_FILE_NAME = "Compiled Website";
    private static String WEBSITE_PATH;
    private static String OUTPUT_PATH;


    public static void setOutputFileName(String fileName){


        OUTPUT_FILE_NAME = fileName.replace(".htm" , "").replace(".html" , "");
    }

    public static String getOutputFileName(){
        return OUTPUT_FILE_NAME;
    }

    public static String getWebsitePath(){
        return WEBSITE_PATH;
    }

    public static void setWebsitePath(String path){

        OUTPUT_PATH = WEBSITE_PATH = path;
    }

    public static String getOutputPath(){
        return OUTPUT_PATH;
    }

    public static void setOutputPath(String path){

        OUTPUT_PATH = path;
    }

}
