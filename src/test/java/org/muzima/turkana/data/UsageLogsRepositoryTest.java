package org.muzima.turkana.data;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.muzima.turkana.model.UsageLogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UsageLogsRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(UsageLogsRepositoryTest.class);

    @Autowired
    private TestEntityManager testEntityManager;


    private UsageLogs sampleUsageLogs;
    private List<UsageLogs> sampleUsageLogsList;

    @Before
    public void setUp(){

        logger.info("Running UsageLogsRepositoryTest : setUp()");

        sampleUsageLogs = new UsageLogs();
        sampleUsageLogsList = Collections.singletonList(sampleUsageLogs);
    }

    @Test
    public void mappingTest(){

        logger.info("Running: UsageLogsRepositoryTest : mappingTest");

        UsageLogs usageLogs = this.testEntityManager.persistAndFlush(sampleUsageLogs);
        List<UsageLogs> usageLogsList = this.testEntityManager.persistAndFlush(sampleUsageLogsList);

        Assertions.assertThat(usageLogs).isEqualTo(sampleUsageLogs);
        Assertions.assertThat(usageLogsList).isEqualTo(sampleUsageLogsList);
    }

}