package models;

import handlers.ImageHandler;
import handlers.LinkHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utils.Logger;

import java.io.File;
import java.io.IOException;



/**
 * Created by CarlSwann on 08/06/2016.
 */
public class HTMLPage {

    private Document doc;
    private File sourceFile;

    /**
     * receives raw HTML file and instantiates
     * Jsoup Document instance variable
     * @param htmlFile
     */
    public HTMLPage(File htmlFile){

        sourceFile = htmlFile;

        try {
            this.doc = Jsoup.parse(sourceFile, "UTF-8");
        } catch (IOException e) {
            Logger.e(this.getSourceFile().getAbsolutePath(), "Could not parse html file");
//            Logger.d(ExceptionUtils.getStackTrace(e));
        }

    }

    /**
     * Coordinates the hard coding of
     * image assets and links into the
     * body of the html page
     */
    public void hardCodeAssets(){

        LinkHandler.processReferences(this);
        ImageHandler.hardCodeImages(this);

    }


    /**
     * returns the source file of this
     * html page
     * @return file Source file
     */
    public File getSourceFile(){
        return sourceFile;
    }

    /**
     * returns the jsoup Document
     * object representing this page
     * @return document Jsoup Document
     */
    public Document getDoc(){return doc;}



}
