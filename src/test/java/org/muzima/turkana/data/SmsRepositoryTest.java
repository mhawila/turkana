package org.muzima.turkana.data;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.muzima.turkana.model.Sms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class SmsRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(SmsRepositoryTest.class);

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private SmsRepository smsRepository;

    private Sms sampleSmsMessage;

    private List<Sms> sampleSmsMessages;

    @Before
    public void setUp() throws Exception {
        logger.info("SmsRepositoryTest set up");

        sampleSmsMessage = new Sms("DIABETES ENCOUNTER APPLICATION", UUID.randomUUID().toString(),"text-message_type");
        sampleSmsMessages = Collections.singletonList(sampleSmsMessage);
    }

    @Test
    public void mappingTest(){

        logger.info("Testing Sms Repository jpa mapping");

        Sms sms = this.testEntityManager.persistAndFlush(sampleSmsMessage);
        Assertions.assertThat(sms.getUuid()).isEqualTo(sampleSmsMessage.getUuid());
        Assertions.assertThat(sms.getType()).isEqualTo(sampleSmsMessage.getType());
        Assertions.assertThat(sms.getSubject()).isEqualTo("DIABETES ENCOUNTER APPLICATION");
    }


    public void tearDown() throws Exception {
    }
}