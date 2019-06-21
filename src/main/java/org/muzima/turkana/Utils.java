package org.muzima.turkana;

import org.muzima.turkana.model.Message;
import org.muzima.turkana.model.Registration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author Willa aka Baba Imu on 5/17/19.
 */
@Component
@Singleton
public class Utils {
    private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());

    @Value("${media.directory:/opt/media_data}")
    private String mediaDirectory;

    @PostConstruct
    public void postConstruct() {
        setMediaDirectory(mediaDirectory);
    }

    public static boolean verifySignature(final String signature, final String publicKey, Message message) {
        // TODO: Do verification.
//        Cipher cipher = Cipher.getInstance()
        return false;
    }

    public String getMediaDirectory() {
        return mediaDirectory;
    }

    public void setMediaDirectory(String mediaDirectory) {
        LOGGER.info("Using media directory: " + mediaDirectory);

        if(!mediaDirectory.endsWith("/")) mediaDirectory += "/";

        if(!mediaDirectory.equals(this.mediaDirectory)) {
            this.mediaDirectory = mediaDirectory;

            File mediaDirFile = new File(mediaDirectory);
            if (!mediaDirFile.exists()) {
                LOGGER.info("Media directory doesn't exists, attempting to create it...");
                if (!mediaDirFile.mkdirs()) {
                    throw new IllegalStateException("Could not create media directory, check directory permission");
                }
            } else if (!mediaDirFile.canWrite()) {
                throw new IllegalStateException("The selected media directory " + mediaDirectory + " is not writable, check directory permissions");
            }
        }
    }

    public String getMediaFilePath(final Registration registration, final String extension) {
        String directory = mediaDirectory + registration.getDeviceId();

        File clientMediaDir = new File(directory);

        if(!clientMediaDir.exists()) {
            if(!clientMediaDir.mkdirs()) {
                LOGGER.severe("Could not create client directory " + clientMediaDir.getName());
            }
        }

        String filePath = new StringBuilder(directory).append("/").append(UUID.randomUUID().toString())
                .append("_")
                .append(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .append(extension)
                .toString();

        return new File(filePath).getAbsolutePath();
    }
}
