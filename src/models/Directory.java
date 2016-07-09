package models;

import config.ExecutionConfig;
import utils.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CarlSwann on 10/06/2016.
 */
public class Directory { //TODO: notify the logger of events especially noting if the directory is empty and if the directory contains no html AllFiles

    boolean hasHTMLFiles = false;
    boolean hasIndexPage = false;
    private List<File> AllFiles;
    private List<File> jsFiles;
    private List<File> cssFiles;
    private List<ImageFile> imageFiles;
    private List<HTMLPage> htmlPages;


    /**
     * receives absolute path of  a website directory and
     * instantiates an object representing it
     * @param givenDirectory
     */
    public Directory(String givenDirectory){

        File dir = new File(givenDirectory);
        File[] directoryListing = dir.listFiles();
        AllFiles = new ArrayList<>();

        if (directoryListing != null) {

            AllFiles.addAll(this.getFiles(directoryListing, false));
            populateInstanceLists();

        } else {

            if (!dir.exists()) {
                Logger.i("The directory entered does not exist");
            }
            if (!dir.canWrite()) {
                Logger.i("Unable to modify this directory.");
            }
        }

    }


    /**
     * This method returns the
     * HTML files contained in this
     * directory as HTMLPage objects
     * @return List<HTMLPage> a list of html Page objects
     */
    public List<HTMLPage> getHTMLPages() {

        return this.htmlPages;
    }


    /**
     * This method returns the
     * JavaScript files contained in this
     * directory
     * @return List<File> a list of JS File objects
     */
    public List<File> getJSFiles() {
        return this.jsFiles;
    }


    /**
     * This method returns the
     * CSS files contained in this
     * directory
     *
     * @return List<File> a list of CSS File objects
     */
    public List<File> getCSSFiles() {

        return this.cssFiles;
    }


    /**
     * This method returns the
     * Image files contained in this
     * directory
     * @return List<File> a list of CSS File objects
     */
    public List<ImageFile> getImageFiles() {
        return this.imageFiles;
    }


    /**
     * returns true if the website directory has
     * contains HTML files
     *
     * @return
     */
    public boolean hasFiles() {
        return this.hasHTMLFiles;
    }


    /**
     * returns true if the website
     * contains an index page
     *
     * @return
     */
    public boolean hasIndexPage() {
        return hasIndexPage;
    }


    /**
     * returns all of the files contained in this directory
     *
     * @param directoryListing array of AllFiles and folders
     * @param isRecursiveInvocation is this call a recursive one
     * @return
     */
    private List<File> getFiles(File[] directoryListing, boolean isRecursiveInvocation){

        List<File> files = new ArrayList<>();

        for (File child : directoryListing) {// Loop through all the AllFiles and folders in the directory

            Logger.d("Processing: " + child.getAbsolutePath());

            if (child.isHidden()) continue; //Skip Hidden AllFiles.
            if (child.listFiles() != null) { // checks if the current file is actually a directory.

                files.addAll(getFiles(child.listFiles(), true)); //If a directory is found, use recursion to traverse its AllFiles.
                continue; // After traversing child directory, continue search in main.main.main.main directory
            }

            String fileName = child.getName();
            if (!fileName.contains(".")) continue; // Skip the file if it doesn't have an extention
            files.add(child);
        }

        return files;
    }


    /**
     * populates the instance list objects
     * as well as raising the flags
     * 'hasHTMLFiles' and 'hasIndexPage'
     * if appropriate
     */
    private void populateInstanceLists() {

        this.htmlPages = new ArrayList<>();
        this.jsFiles = new ArrayList<>();
        this.cssFiles = new ArrayList<>();
        this.imageFiles = new ArrayList<>();

        for (File file : this.AllFiles) {

            String fileName = file.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf("."), fileName.length());

            switch (fileExtension) {

                case ".html":
                    if (file.getName().equals(ExecutionConfig.getOutputFileName() + ".html"))
                        break; //Skip the file in the event that it is a previous compilation. (This logic could be refined)

                    this.htmlPages.add(new HTMLPage(file));
                    this.hasHTMLFiles = true;
                    if (fileName.contains("index")) {
                        this.hasIndexPage = true;
                    } //TODO: Address the possibility that the index page may not be named with lowercase characters
                    break;

                case ".js":
                    this.jsFiles.add(file);
                    break;

                case ".css":
                    this.cssFiles.add(file);
                    break;

                case ".jpeg":
                case ".gif":
                case ".jpg":
                case ".png":
                    this.imageFiles.add(new ImageFile(file));
                    break;
                default:
                    Logger.i("Found " + file.getName() + " in the directory. Did nothing with it.");

            }
        }
    }
}
