package org.muzima;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.muzima.turkana.UtilsTest;
import org.muzima.turkana.data.RegistrationQueryIntegrationTest;
import org.muzima.turkana.service.MediaServiceIntegrationTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		UtilsTest.class,
		MediaServiceIntegrationTest.class,
		RegistrationQueryIntegrationTest.class
})
public class TurkanaApplicationTests {

	@Test
	public void contextLoads() {

	}

}
