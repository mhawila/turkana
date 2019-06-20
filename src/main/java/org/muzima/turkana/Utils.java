package org.muzima.turkana;

import org.muzima.turkana.model.Message;
import org.muzima.turkana.model.Registration;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author Willa aka Baba Imu on 5/17/19.
 */
// TODO: Make this a spring bean to enable testing
public class Utils {
    private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());
    public static final String DEFAULT_MEDIA_DIR = "/opt/media_data";
    public static final String MEDIA_DIR_PROP_NAME = "media.directory";

    private static final String APPLICATION_PROPERTIES_FILE = "application.properties";

    private static String mediaDirectory = null;

    static {
        Properties appProps = null;
        try {
            appProps = PropertiesLoaderUtils.loadAllProperties(APPLICATION_PROPERTIES_FILE);
        } catch (IOException e) {
            LOGGER.severe("Could not load " + APPLICATION_PROPERTIES_FILE);
            e.printStackTrace();
        } finally {
            if(appProps != null) {
                mediaDirectory = appProps.getProperty(MEDIA_DIR_PROP_NAME, DEFAULT_MEDIA_DIR);
            }

            if(mediaDirectory == null) {
                //use default.
                mediaDirectory = DEFAULT_MEDIA_DIR;
            }

            if(!mediaDirectory.endsWith("/")) {
                mediaDirectory += "/";
            }

            LOGGER.info("Using media directory: " + mediaDirectory);

            File mediaDirFile = new File(mediaDirectory);
            if(!mediaDirFile.exists()) {
                LOGGER.info("Media directory doesn't exists, attemping to create it...");
                if(!mediaDirFile.mkdirs()) {
                    throw new IllegalStateException("Could not create media directory, please ensure the media directory is defined and accessible");
                }
            } else if(!mediaDirFile.canWrite()) {
                throw new IllegalStateException("The selected media directory " + mediaDirectory + " is not writable");
            }
        }
    }

    public static boolean verifySignature(final String signature, final String publicKey, Message message) {
        // TODO: Do verification.
//        Cipher cipher = Cipher.getInstance()
        return false;
    }

    public static String getMediaDirectory() {
        return mediaDirectory;
    }


    public static String getMediaFilePath(final Registration registration, final String extension) {
        String directory = mediaDirectory + registration.getDeviceId();
        File clientMediaDir = new File(directory);

        if(!clientMediaDir.exists()) {
            if(!clientMediaDir.mkdirs()) {
                LOGGER.severe("Could not create client directory " + clientMediaDir.getName());
            }
        }

        return new StringBuilder(directory).append("/").append(UUID.randomUUID().toString())
                .append("_")
                .append(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .append(extension)
                .toString();
    }
}
