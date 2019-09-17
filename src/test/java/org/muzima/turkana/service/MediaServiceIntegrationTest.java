package org.muzima.turkana.service;

import org.junit.Before;
import org.junit.Test;
import org.muzima.turkana.AbstractIntegrationTest;
import org.muzima.turkana.data.MediaMetadataRepository;
import org.muzima.turkana.model.MediaMetadata;
import org.muzima.turkana.model.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Willa aka Baba Imu on 6/20/19.
 */
public class MediaServiceIntegrationTest extends BaseSecurityVerificationTest {
    @Autowired
    private MediaMetadataRepository metadataRepository;

    @Autowired
    private MediaService mediaService;

    private Resource testMediaFileResource;

    @Before
    public void setupTest() {
        testMediaFileResource = new ClassPathResource("media_dir/muzima.jpg");
    }

    @Test
    public void saveMedia_shouldSaveMediaFileAndMetadata() throws IOException {
        MediaMetadata metadata = new MediaMetadata();
        Registration registration = new Registration();

        String randomFilename = UUID.randomUUID().toString();
        String mediaPath = testMediaFileResource.getFile().getParentFile().getAbsolutePath() + "/" + randomFilename + ".jpg";

        File mediaFile = new File(mediaPath);

        assertFalse(mediaFile.exists());

        metadata.setServerFilePath(mediaPath);
        metadata.setExtension(".jpg");
        metadata.setDateReceived(LocalDateTime.now());

        assertNull(metadata.getId());

        mediaService.saveMedia(registration, metadata, testMediaFileResource.getInputStream());

        assertTrue("The media file should be saved", mediaFile.exists());
        assertNotNull("Metadata should have been persisted", metadata.getId());

        // Remove the file.
        mediaFile.delete();
    }

    @Test(expected = Exception.class)
    public void saveMedia_shouldThrowExceptionIfPathIsInvalid() throws IOException {
        MediaMetadata metadata = new MediaMetadata();
        metadata.setServerFilePath("/this/path/does/not/exist");

        mediaService.saveMedia(new Registration(), metadata, testMediaFileResource.getInputStream());
    }

    @Test
    public void verifyAndSaveMediaShouldDoExactlyThatBuddy() throws Exception {
        MediaMetadata metadata = new MediaMetadata();
        Registration registration = new Registration();
        registration.setPublicKey(getBase64PublicKeyFromKeyPair());

        // Get the directory
        String randomFilename = UUID.randomUUID().toString();
        String mediaPath = testMediaFileResource.getFile().getParentFile().getAbsolutePath() + "/" + randomFilename + ".jpg";

        File mediaFile = new File(mediaPath);

        assertFalse(mediaFile.exists());

        metadata.setServerFilePath(mediaPath);
        metadata.setExtension(".jpg");
        metadata.setDateReceived(LocalDateTime.now());
        metadata.setSize(200);
        metadata.setSenderPhoneNumber("33445000");
        metadata.setMediaType("image/jpg");

        // Calculate signature.
        String concat = metadata.getSenderPhoneNumber() + metadata.getSize() + metadata.getMediaType();
        signatureAlgorithm.update(concat.getBytes());
        byte[] signature = signatureAlgorithm.sign();


        assertNull(metadata.getId());

        metadata =  mediaService.verifyAndSaveMedia(registration, metadata, testMediaFileResource.getInputStream(), signature);

        assertTrue("The media file should be saved", mediaFile.exists());
        assertNotNull("Metadata should have an ID set", metadata.getId());

        // Remove the file.
        mediaFile.delete();
    }

    @Test(expected = MediaVerificationException.class)
    public void verifyAndSaveMediaShouldThrow() throws Exception {
        MediaMetadata metadata = new MediaMetadata();
        Registration registration = new Registration();
        registration.setPublicKey(getBase64PublicKeyFromKeyPair());

        // Get the directory
        String randomFilename = UUID.randomUUID().toString();
        String mediaPath = testMediaFileResource.getFile().getParentFile().getAbsolutePath() + "/" + randomFilename + ".jpg";

        File mediaFile = new File(mediaPath);

        assertFalse(mediaFile.exists());

        metadata.setServerFilePath(mediaPath);
        metadata.setExtension(".jpg");
        metadata.setDateReceived(LocalDateTime.now());
        metadata.setSize(200);
        metadata.setSenderPhoneNumber("33445000");
        metadata.setMediaType("image/jpg");

        // Calculate signature.
        String concat = metadata.getSenderPhoneNumber() + metadata.getSize() + metadata.getMediaType();
        signatureAlgorithm.update(concat.getBytes());
        byte[] signature = signatureAlgorithm.sign();

        // change phone number
        metadata.setSenderPhoneNumber("55889000");
        mediaService.verifyAndSaveMedia(registration, metadata, testMediaFileResource.getInputStream(), signature);
    }
 }
