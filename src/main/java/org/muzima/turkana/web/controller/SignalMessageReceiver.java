package org.muzima.turkana.web.controller;

import org.muzima.turkana.data.TurkanaKeyStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.util.KeyHelper;
import org.whispersystems.signalservice.api.SignalServiceAccountManager;
import org.whispersystems.signalservice.api.push.TrustStore;
import org.whispersystems.signalservice.internal.configuration.SignalCdnUrl;
import org.whispersystems.signalservice.internal.configuration.SignalServiceConfiguration;
import org.whispersystems.signalservice.internal.configuration.SignalServiceUrl;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * @uthor Willa Mhawila<a.mhawila@gmail.com> on 7/30/19.
 */
@RestController
@RequestMapping("/api/signal")
public class SignalMessageReceiver {
    @Autowired
    private TurkanaKeyStore store;

    private static final SignalProtocolAddress sProtocolAddress = new SignalProtocolAddress("mbs",1);
    private static final int SIGNED_PREKEY_ID = 1000;

    public static final String SIGNAL_SERVICE_URL = "https://textsecure-service.whispersystems.org";
    public static final String USERNAME = "+255689383495";
    private static final String PASSWORD = "password";

    private SignalServiceAccountManager accountManager;

    @PostConstruct
    public void setupSignal() throws InvalidKeyException {
        IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();
        List<PreKeyRecord> oneTimePrekeys = KeyHelper.generatePreKeys(1,100);
        SignedPreKeyRecord signedPreKeyRecord = KeyHelper.generateSignedPreKey(identityKeyPair, SIGNED_PREKEY_ID);

        // Store this stuff.
        store.saveIdentity(sProtocolAddress, identityKeyPair.getPublicKey());
        for(int i=0; i < oneTimePrekeys.size() ; i++ ) {
            store.storePreKey(i + 1, oneTimePrekeys.get(i));
        }

//        store.storeSignedPreKey(SIGNED_PREKEY_ID, signedPreKeyRecord);

        // Create account manager
        SignalServiceUrl url = new SignalServiceUrl(SIGNAL_SERVICE_URL, new TrustStore() {
            @Override
            public InputStream getKeyStoreInputStream() {
                return null;
            }

            @Override
            public String getKeyStorePassword() {
                return PASSWORD;
            }
        });

        SignalServiceConfiguration configuration = new SignalServiceConfiguration(new SignalServiceUrl[] { url },new SignalCdnUrl[0]);
        accountManager = new SignalServiceAccountManager(configuration, USERNAME, PASSWORD, "java-app");
    }

    @PostMapping(path = "/register")
    public void register() throws IOException {
        accountManager.requestSmsVerificationCode();
    }

    @PostMapping(path = "/verify", consumes = "application/json")
    public void verify(@RequestParam String code) throws IOException {
        accountManager.verifyAccountWithCode(code, UUID.randomUUID().toString(), store.getLocalRegistrationId(), false, "willa");
    }
}
