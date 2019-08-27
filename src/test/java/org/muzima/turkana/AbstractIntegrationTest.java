package org.muzima.turkana;

import org.junit.runner.RunWith;
import org.muzima.TurkanaApplication;
import org.springframework.boot.test..SpringBootTest;
import org.springframework.test..ActiveProfiles;
import org.springframework.test..junit4.SpringRunner;

/**
 * Provides Spring  and database initialization for integration tests.
 * @author  Willa aka Baba Imu on 6/20/19.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TurkanaApplication.class, H2TestConfig.class })
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {
}
