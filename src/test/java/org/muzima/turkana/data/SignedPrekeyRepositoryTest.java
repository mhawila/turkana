package org.muzima.turkana.data;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.muzima.turkana.model.SignedPreKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.net.ssl.KeyManagerFactory;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class SignedPrekeyRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    private SignedPreKey sampleSignedPreKey;

    @Before
    public void setUp() throws Exception {
        sampleSignedPreKey = new SignedPreKey(1,1,"public_key","private_key","signature",1L);
    }

    @Test
    public void mappingTest(){
        SignedPreKey signedPreKey = this.testEntityManager.persistAndFlush(sampleSignedPreKey);
        Assertions.assertThat(signedPreKey).isEqualTo(sampleSignedPreKey);
    }
}