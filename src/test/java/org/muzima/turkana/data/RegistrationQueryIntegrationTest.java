package org.muzima.turkana.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.muzima.TurkanaApplication;
import org.muzima.turkana.H2TestConfig;
import org.muzima.turkana.model.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test..SpringBootTest;
import org.springframework.test..ActiveProfiles;
import org.springframework.test..jdbc.Sql;
import org.springframework.test..junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Willa aka Baba Imu on 5/20/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TurkanaApplication.class, H2TestConfig.class })
@ActiveProfiles("test")
public class RegistrationQueryIntegrationTest {

    @Autowired
    private RegistrationQuery registrationQuery;

    @Test
    @Sql("/registration.sql")
    public void shouldReturnTheCorrectRegistrationGivenPhoneNumberOrDeviceId() {
        Registration registration = registrationQuery.getActiveRegistrationByPhoneNumber("333");

        assertEquals("333", registration.getPhoneNumber());
        assertEquals("public-key-2", registration.getPublicKey());

        registration = registrationQuery.getActiveRegistrationByDeviceId("1000-3");
        assertEquals("1000-3", registration.getDeviceId());

        registration = registrationQuery.getActiveRegistrationByDeviceId("non-existent");
        assertNull(registration);
    }
}
