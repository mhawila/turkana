package org.muzima.turkana.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.muzima.turkana.Utils;
import org.muzima.turkana.data.MediaMetadataRepository;
import org.muzima.turkana.model.MediaMetadata;
import org.muzima.turkana.model.Registration;
import org.muzima.turkana.service.MediaService;
import org.muzima.turkana.service.MediaVerificationException;
import org.muzima.turkana.utils.CryptoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Signature;

import static org.muzima.turkana.utils.Constants.SECURITY.SIGNATURE_ALGORITHM;

/**
 * @author Willa aka Baba Imu on 6/20/19.
 */
@Service
public class MediaServiceImpl implements MediaService {
    private static final Log LOGGER = LogFactory.getLog(MediaServiceImpl.class);

    @Autowired
    private MediaMetadataRepository metadataRepository;

    @Autowired
    private Utils utils;

    @Override
    @Transactional
    public MediaMetadata saveMedia(final Registration registration, final MediaMetadata mediaMetadata, final InputStream media) throws IOException {
        if(mediaMetadata.getServerFilePath() == null) {
            mediaMetadata.setServerFilePath(utils.getMediaFilePath(registration, mediaMetadata.getExtension()));
        }

        LOGGER.debug("Media file path: " + mediaMetadata.getServerFilePath());
        MediaMetadata saved = metadataRepository.save(mediaMetadata);
        try(OutputStream os = new FileOutputStream(new File(saved.getServerFilePath()))) {
            int byt3;
            while((byt3 = media.read()) != -1) {
                os.write(byt3);
            }
        }

        return saved;
    }

    @Override
    public MediaMetadata verifyAndSaveMedia(final Registration registration, final MediaMetadata mediaMetadata, final InputStream media,
                                            final byte[] signature) throws Exception {
        String concat = mediaMetadata.getSenderPhoneNumber() + mediaMetadata.getSize() + mediaMetadata.getMediaType();
        Signature rsaSignatureInstance = Signature.getInstance(SIGNATURE_ALGORITHM);
        rsaSignatureInstance.initVerify(CryptoUtils.getPublicKeyFromText(registration.getPublicKey()));
        rsaSignatureInstance.update(concat.getBytes());

        if(rsaSignatureInstance.verify(signature)) {
            LOGGER.info("Signature verified successfully for media: " + mediaMetadata.getFilename());
            return saveMedia(registration, mediaMetadata, media);
        } else {
            LOGGER.error("Signature verification failed, media not saved");
            throw new MediaVerificationException(mediaMetadata, signature, registration.getPublicKey());
        }
    }
}
