package org.muzima.turkana;

import org.junit.runner.RunWith;
import org.muzima.TurkanaApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Provides Spring context and database initialization for integration tests.
 * @author  Willa aka Baba Imu on 6/20/19.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TurkanaApplication.class, H2TestConfig.class })
@ActiveProfiles("test")
public class AbstractIntegrationTest {
}
