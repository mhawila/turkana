package org.muzima.turkana;

import org.junit.Test;
import org.muzima.turkana.model.Registration;

import static org.junit.Assert.assertTrue;

/**
 * @author Willa aka Baba Imu on 6/20/19.
 */

public class UtilsTest {
    @Test
    public void getMediaFilePath_shouldReturnExpectedPath() {
        String deviceId = "my-device";
        String expectedPattern = "/opt/media_data/" + deviceId + "/" + "[\\w\\-.]+";

        Registration registration = new Registration();
        registration.setDeviceId(deviceId);

        String filePath = Utils.getMediaFilePath(registration, ".jpg");

        assertTrue("Method should return expected file path", filePath.matches(expectedPattern));
    }
}
