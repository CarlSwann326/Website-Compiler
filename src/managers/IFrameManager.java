package managers;

/**
 * Created by CarlSwann on 6/21/16.
 * Manages the main iframe(the id of the iframe is 'fabio')
 */
public class IFrameManager {

    public static final String Id = "fabioIframe"; //arbitrary id for iframe.

    /**
     * compiles the received Javascript and
     * CSS into a single block of HTML.
     * @param compiledJavaScript
     * @param compiledCSS
     * @return
     */
    public static String getIFrame(String compiledJavaScript, String compiledCSS) {

        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">" +
                "\n<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js\"></script>\n" +
                "\n" + compiledCSS +
                "\n</head>\n" +
                "\n" +
                "\n<body>" +
                " <iframe id=\""+Id+"\"  style=\"width: 100%; height : 100%; position: absolute; border : none;\"></iframe> \t\n" +
                compiledJavaScript +
                "\n</body>" +
                "\n" +
                "</html>";
    }
}
