package org.muzima.turkana.service;

import org.junit.Before;
import org.junit.Test;
import org.muzima.turkana.model.Registration;
import org.muzima.turkana.model.Sms;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @uthor Willa Mhawila<a.mhawila@gmail.com> on 8/16/19.
 */
public class SmsServiceIntegrationTest extends BaseSecurityVerificationTest {
    private Registration registration;

    @Autowired
    private SmsService smsService;

    @Before
    public void setupInstance() throws NoSuchAlgorithmException, InvalidKeyException {
        registration = new Registration();
        registration.setPublicKey(getBase64PublicKeyFromKeyPair());
    }

    @Test
    public void verifyMessageAndSaveShouldSaveIfSignatureIsVerified() throws Exception {
        Sms sms = new Sms();
        String message = "Helloo, habari yako daktari? Samahani naomba nikutumie ripoti yangu ya maabara";
        sms.setBody(message);

        signatureAlgorithm.update(message.getBytes());
        byte[] signature = signatureAlgorithm.sign();

        assertNull(sms.getId());
        sms = smsService.verifySmsAndSave(sms, signature, registration);

        assertNotNull(sms.getId());
    }

    @Test(expected = MessageVerificationException.class)
    public void verifyMessageAndSaveShouldThrowIfMessageNotVerified() throws Exception {
        Sms sms = new Sms();
        String message = "Helloo, habari yako daktari?";
        sms.setBody(message + " Samahani naomba nikutumie ripoti yangu ya maabara");

        signatureAlgorithm.update(message.getBytes());
        byte[] signature = signatureAlgorithm.sign();

        smsService.verifySmsAndSave(sms, signature, registration);
    }

    @Test
    public void verifyMultipleSmsAndSaveShouldDoExactlyThat() throws Exception {
        List<Sms> list = new ArrayList<>();
        Sms sms = new Sms();
        sms.setBody("Hello Moto");
        list.add(sms);

        sms = new Sms();
        sms.setBody("Habari za siku mingi?");
        list.add(sms);

        // Concatenate bodies
        String concatMessages = list.get(0).getBody() + list.get(1).getBody();

        signatureAlgorithm.update(messageDigest.digest(concatMessages.getBytes()));
        byte[] signature = signatureAlgorithm.sign();

        assertNull(list.get(0).getId());
        assertNull(list.get(1).getId());
        list = smsService.verifyMultipleSmsAndSave(list, signature, registration);

        assertNotNull(list.get(0).getId());
        assertNotNull(list.get(1).getId());
    }

    @Test(expected = MessageVerificationException.class)
    public void verifyMultipleSmsAndSaveShouldThrowIfVerificationFailed() throws Exception {
        List<Sms> list = new ArrayList<>();
        Sms sms = new Sms();
        sms.setBody("Hello Moto");
        list.add(sms);

        sms = new Sms();
        sms.setBody("Habari za siku mingi?");
        list.add(sms);

        // Concatenate bodies
        String concatMessages = list.get(0).getBody() + list.get(1).getBody() + "some junk shit";

        signatureAlgorithm.update(messageDigest.digest(concatMessages.getBytes()));
        byte[] signature = signatureAlgorithm.sign();

        smsService.verifyMultipleSmsAndSave(list, signature, registration);
    }
}
