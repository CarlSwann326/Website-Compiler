package models;

import utils.Base64Encoder;

import java.io.File;
import java.io.IOException;

/**
 * Created by CarlSwann on 6/22/16.
 */
public class ImageFile { //TODO: Consider refactoring this class to inherit directly from the File class

    private File sourceImage;

    /**
     * instantiates a new ImageFile object
     * from the given file
     * @param imageFile
     */
    public ImageFile(File imageFile) {

        sourceImage = imageFile;
    }

    /**
     * returns the name of the source image file
     * @return
     */
    public String getName() {
        return sourceImage.getName();
    }

    /**
     * returns the Base64 encoded
     * representation of this image
     * @return
     * @throws IOException
     */
    public String getBase64EncodedString() throws IOException {

        StringBuffer buffer = new StringBuffer();

        buffer.append("data:image/");
        String fileName = sourceImage.getName();
        buffer.append(fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length())); // Appends file type
        buffer.append(";base64, ");
        buffer.append(Base64Encoder.encode(sourceImage));

        return buffer.toString();
    }

    /**
     * returns the absolute file path of the
     * source image file.
     * @return
     */
    public String getFilePath() {
        return sourceImage.getAbsolutePath();
    }
}
