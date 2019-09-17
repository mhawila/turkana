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
import static org.junit.Assert.assertTrue;

/**
 * @author Willa aka Baba Imu on 6/20/19.
 */
public class MediaServiceIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private MediaMetadataRepository metadataRepository;

    @Autowired
    private MediaService mediaService;

    private Resource testMediaFileResource;

    @Before
    public void setup() {
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

        List<MediaMetadata> metadatas = metadataRepository.findAll();
        assertTrue(metadatas.isEmpty());

        mediaService.saveMedia(registration, metadata, testMediaFileResource.getInputStream());

        assertTrue("The media file should be saved", mediaFile.exists());
        assertFalse("There should be at least one instance of media metadata", metadataRepository.findAll().isEmpty());

        // Remove the file.
        mediaFile.delete();
    }

    @Test(expected = Exception.class)
    public void saveMedia_shouldThrowExceptionIfPathIsInvalid() throws IOException {
        MediaMetadata metadata = new MediaMetadata();
        metadata.setServerFilePath("/this/path/does/not/exist");

        mediaService.saveMedia(new Registration(), metadata, testMediaFileResource.getInputStream());
    }
 }
