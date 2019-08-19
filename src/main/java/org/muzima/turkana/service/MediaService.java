package org.muzima.turkana.service;

import org.muzima.turkana.model.MediaMetadata;
import org.muzima.turkana.model.Registration;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Willa aka Baba Imu on 6/20/19.
 */
public interface MediaService {
    MediaMetadata saveMedia(Registration registration, MediaMetadata mediaMetadata, InputStream media) throws IOException;

    /**
     * verifies sender signed metadata before persisting the information (metadata + media file).
     * @param registration
     * @param mediaMetadata
     * @param media
     * @param signature
     * @return persisted metadata.
     * @throws Exception
     */
    MediaMetadata verifyAndSaveMedia(Registration registration, MediaMetadata mediaMetadata, InputStream media, byte[] signature) throws Exception;
}
