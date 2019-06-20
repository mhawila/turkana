package org.muzima.turkana.service;

import org.muzima.turkana.model.MediaMetadata;
import org.muzima.turkana.model.Registration;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Willa aka Baba Imu on 6/20/19.
 */
public interface MediaService {
    void saveMedia(Registration registration, MediaMetadata mediaMetadata, InputStream media) throws IOException;
}
