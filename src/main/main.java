package main;

import config.ExecutionConfig;
import models.Directory;
import utils.Logger;

/**
 * Created by CarlSwann on 6/21/16.
 *
 * Starting Point of the Program.
 */
public class main {

    private static Directory website;

    /**
     * starting point of the program.
     * @param args command line arguments
     */
    public static void main(String[] args){


        if (!processInput(args)) {printExecutionReport();return;}

        System.out.println("\n\nCopying HTML Files and assets...");
        website = new Directory(ExecutionConfig.getWebsitePath());

        if (!website.hasFiles()) {
            Logger.i("No files found.");
            printExecutionReport();
            return;
        }
        if (!website.hasIndexPage()) {
            Logger.i("Index Page not found. Is the file name not lower case?");
            printExecutionReport();
            return;
        }


        Coordinator.convertToSingleHTMLFile(website);
        printExecutionReport();

    }

    /**
     * processes the command line arguments
     *
     * @param args
     * @return boolean true if input was valid. Otherwise false.
     */
    private static boolean processInput(String[] args) {



            if(args.length > 0){

                ExecutionConfig.setWebsitePath(args[0]);

                if(args[0].equals(".") || args[0].equals("./")){

                    System.out.println("Website Path: (here)");

                } else {

                    System.out.println("Website Path: " + args[0]);

                }


                if(args.length > 1 && !args[1].equals("")) {

                    ExecutionConfig.setOutputPath(args[1]);

                    if (args[1].equals(".") || args[1].equals("./")) {

                        System.out.println("Output Directory: (here)");

                    } else {

                        System.out.println("Output Directory: " + args[1]);

                    }
                }


                if(args.length > 2 && !args[2].equals(""))
                ExecutionConfig.setOutputFileName(args[2]);
            } else {

                printGreeting();
                return false;
            }






        return true;
    }

    /**
     * Obtains the Execution report from
     * the logger and prints it to the
     * console
     */
    private static void printExecutionReport() {

        System.out.println(Logger.getBriefReport());
    }

    private static void printGreeting(){

        String greeting = "\"\n" +
                "\n" +
                "\twcomp.jar\n" +
                "\n" +
                "\t\t The Website Compiler allows one to convert a website contained in a a local directoy into a single html file.\n" +
                "\t\t While also compressing the html pages. Decompression and navigation emulation is handled entirely by Javascript.\n" +
                "\t\t This allows for ease of ditribution amoungst a wide variety of channels.\n" +
                "\n" +
                "\tUse:\n" +
                "\n" +
                "\twcomp.jar [WEBSITE_PATH] [FILE_OUTPUT_PATH] [OUTPUT_FILE_NAME]\n" +
                "\n" +
                "\t\tWEBSITE_PATH  (Mandatory) \n" +
                "\n" +
                "\t\t\tRoot directory of the website\n" +
                "\n" +
                "\t\tFILE_OUTPUT_PATH \n" +
                "\n" +
                "\t\t\tDesired output path for the compiled HTML file\n" +
                "\n" +
                "\t\tOUTPUT_FILE_NAME \n" +
                "\n" +
                "\t\t\tDesired name for the generated file.\n" +
                "\n" +
                "\n" +
                "\n" +
                "\tExample:\n" +
                "\n" +
                "\t\twcomp.jar \"/Users/Me/My Website\"  \"/Users/Me/Desktop/\"  \"Compiled Website\" \n" +
                "\n" +
                "\"";


        System.out.println(greeting);
    }


}
