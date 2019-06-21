package org.muzima.turkana;

import org.junit.Test;
import org.muzima.turkana.model.Registration;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * @author Willa aka Baba Imu on 6/20/19.
 */
public class UtilsTest extends AbstractIntegrationTest {
    @Autowired
    private Utils utils;

    @Test
    public void getMediaFilePath_shouldReturnExpectedPath() throws IOException {
        // Set a custom directory for this test.
        utils.setMediaDirectory("media_directory");

        String deviceId = "my-device";
        String expectedPattern = "^.*" + utils.getMediaDirectory() + deviceId + "/" + "[\\w:.-]+$";

        Registration registration = new Registration();
        registration.setDeviceId(deviceId);
        String filePath = utils.getMediaFilePath(registration, ".jpg");

        // Remove the created directory.
        File created = new File(utils.getMediaDirectory());
        if(created.exists()) cleanUpTestDirectories(created);

        System.out.println("File path: " + filePath);
        System.out.println("Expected pattern: " + expectedPattern);
        assertTrue("Method should return expected file path", filePath.matches(expectedPattern));
    }

    @Test(expected = IllegalStateException.class)
    public void setMediaFilePath_shouldThrowExceptionWhenItFailsToMakeDirectory() {
        utils.setMediaDirectory("/opt/here/no/permission/to/create");
    }

    private void cleanUpTestDirectories(File toBedeleted) {
        File[] contained = toBedeleted.listFiles();
        if(contained.length > 0) {
            for(File f: contained) {
                cleanUpTestDirectories(f);
            }
        }
        toBedeleted.delete();
    }
}
