package utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by CarlSwann on 6/22/16.
 */
public class Base64Encoder {

    /**
     * Takes in a file and returns
     * the base64 encoded String
     * represntation of that file
     *
     * @param file
     * @return
     */
    public static String encode(File file) throws IOException {


        return Base64.encodeBase64String(FileUtils.readFileToByteArray(file));

    }
}
