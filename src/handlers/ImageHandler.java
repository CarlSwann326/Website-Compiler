package handlers;

import main.Coordinator;
import models.HTMLPage;
import models.ImageFile;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import utils.Logger;

import java.io.IOException;

/**
 * Created by CarlSwann on 6/21/16.
 */
public class ImageHandler {

    private static Document doc;

    /**
     * Locates all of the  image tags in an html page
     * and embeds the encoded image string into the
     * source attribute
     *
     * @param page
     */
    public static void hardCodeImages(HTMLPage page) {

        doc = page.getDoc();

        searchPageForImages();

    }


    /**
     * Searches the HTML page
     * for images and encodes
     * them
     */
    private static void searchPageForImages() {

        for (Element imageTag : doc.getElementsByTag("img")) {

            for (ImageFile sourceImageFile : Coordinator.website.getImageFiles()) {

                if (imageTag.attr("src") != null && imageTag.attr("src").contains(sourceImageFile.getName())) { //TODO: Refine this logic to account for images that may match incorrectly due to contains function

                    try {

                        imageTag.attr("src", sourceImageFile.getBase64EncodedString());

                    } catch (IOException e) {

                        Logger.e(sourceImageFile.getFilePath(), e.getMessage());
                    }

                }
            }
        }
    }
}
