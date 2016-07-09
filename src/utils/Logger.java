package utils;

import config.ExecutionConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CarlSwann on 10/06/2016.
 */
public class Logger {

    private static List<String> infoLogs;
    private static List<String> warningLogs;
    private static List<String> errorLogs;
    private static List<String> debugLogs;
    private static String briefReport = "";


    static{
        infoLogs = new ArrayList<>();
        warningLogs = new ArrayList<>();
        errorLogs = new ArrayList<>();
        debugLogs = new ArrayList<>();
    }

    /**
     * logs debugging messages
     * @param message message to be logged
     */
    public static void d(String message){

        debugLogs.add(message);

    }


    /**
     * logs info messages
     * @param message message to be logged
     */
    public static void i(String message){

        infoLogs.add(message);

    }

    /**
     * logs warning messages
     * @param message message to be logged
     */
    public static void w(String filePath, String message){

        warningLogs.add(message +" : " + filePath);
    }

    /**
     * logs error messages
     * @param message message to be logged
     */
    public static void e(String filePath, String message){

        errorLogs.add(message +" : " + filePath);

    }

    /**
     * conpiles and returns a brief report of
     * the current Execution.
     * @return Execution Report
     */
    public static String getBriefReport(){

        StringBuffer result = new StringBuffer();

        for(String message : infoLogs){result.append("\nInfo : " + message);}

        result.append("\n\nExecution completed ");

        if(errorLogs.size() == 0){

            result.append("successfully ");
        }

        result.append("with " + warningLogs.size() + " warnings and " + errorLogs.size() + " errors.");
        for(String message : errorLogs){result.append("\nError : " + message);}


        briefReport = result.toString();
        result.append("\nCheck the log file for more information.");

        writeExecutionReportToFile();
        return result.toString();
    }


    /**
     * compiles and writes the full Execution Report
     * to a text file int the root directory
     * of the Visual Paradigm Export
     */
    public static void writeExecutionReportToFile(){

        ZonedDateTime zdt = ZonedDateTime.now();

        File log = new File(ExecutionConfig.getOutputPath(), "log.txt");

        StringBuffer fullReport = new StringBuffer();
        fullReport.append(briefReport);

        for(String message : debugLogs){fullReport.append("\n" + message);}

        try {

            FileWriter  writer = new FileWriter(log);
            writer.write(
                    zdt.format(DateTimeFormatter.ofPattern("yyyy MM dd :: h:mm"))
                    +fullReport.toString());

            writer.close();

        } catch (IOException e) {
            Logger.i(e.getMessage());
        }
    }
}
