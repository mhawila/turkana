package org.muzima.turkana.service.impl;

import org.muzima.turkana.Utils;
import org.muzima.turkana.data.MediaMetadataRepository;
import org.muzima.turkana.model.MediaMetadata;
import org.muzima.turkana.model.Registration;
import org.muzima.turkana.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Willa aka Baba Imu on 6/20/19.
 */
@Service
public class MediaServiceImpl implements MediaService {

    @Autowired
    private MediaMetadataRepository metadataRepository;

    @Autowired
    private Utils utils;

    @Override
    @Transactional
    public void saveMedia(final Registration registration, final MediaMetadata mediaMetadata, final InputStream media) throws IOException {
        if(mediaMetadata.getFilePath() == null) {
            mediaMetadata.setFilePath(utils.getMediaFilePath(registration, mediaMetadata.getExtension()));
        }

        metadataRepository.save(mediaMetadata);
        System.out.println(mediaMetadata.getFilePath());
        try(OutputStream os = new FileOutputStream(new File(mediaMetadata.getFilePath()))) {
            int byt3;
            while((byt3 = media.read()) != -1) {
                os.write(byt3);
            }
        }
    }
}
