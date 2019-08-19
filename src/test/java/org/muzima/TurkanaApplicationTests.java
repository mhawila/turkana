package org.muzima;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.muzima.turkana.UtilsTest;
import org.muzima.turkana.data.RegistrationQueryIntegrationTest;
import org.muzima.turkana.service.MediaServiceIntegrationTest;
import org.muzima.turkana.service.SmsServiceIntegrationTest;
import org.muzima.turkana.utils.CryptoUtilsTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		UtilsTest.class,
		CryptoUtilsTest.class,
		MediaServiceIntegrationTest.class,
		RegistrationQueryIntegrationTest.class,
		SmsServiceIntegrationTest.class,
		MediaServiceIntegrationTest.class
})
public class TurkanaApplicationTests {

	@Test
	public void contextLoads() {

	}

}
