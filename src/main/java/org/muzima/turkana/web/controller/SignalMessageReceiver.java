package org.muzima.turkana.web.controller;

import org.muzima.turkana.data.TurkanaKeyStore;
import org.spongycastle.jce.provider.BouncyCastleProvider;
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
import java.io.*;
import java.security.Provider;
import java.security.Security;
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

    private static final SignalProtocolAddress sProtocolAddress = new SignalProtocolAddress("mbs", 1);
    private static final int SIGNED_PREKEY_ID = 1000;

    public static final String SIGNAL_SERVICE_URL = "https://textsecure-service.whispersystems.org";
    public static final String USERNAME = "+254706906138";
    private static final String PASSWORD = "whisper";

    private SignalServiceAccountManager accountManager;

    @PostConstruct
    public void setupSignal() throws InvalidKeyException {

        installJCAProvider();

        IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();
        List<PreKeyRecord> oneTimePrekeys = KeyHelper.generatePreKeys(1, 100);
        SignedPreKeyRecord signedPreKeyRecord = KeyHelper.generateSignedPreKey(identityKeyPair, SIGNED_PREKEY_ID);

        // Store this stuff.
        store.saveIdentity(sProtocolAddress, identityKeyPair.getPublicKey());
        for (int i = 0; i < oneTimePrekeys.size(); i++) {
            store.storePreKey(i + 1, oneTimePrekeys.get(i));
        }

//        store.storeSignedPreKey(SIGNED_PREKEY_ID, signedPreKeyRecord);

        // Create account manager
        SignalServiceUrl url = new SignalServiceUrl(SIGNAL_SERVICE_URL, new TrustStore() {
            @Override
            public InputStream getKeyStoreInputStream() {
                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(
                        new File(getClass().getClassLoader().getResource("whisper.store").getFile()
                    ));

                    return inputStream;

                } catch (FileNotFoundException e) {
                    throw new AssertionError("Keystore file not found.");
                }

            }

            @Override
            public String getKeyStorePassword() {
                return PASSWORD;
            }
        });

        SignalServiceConfiguration configuration = new SignalServiceConfiguration(new SignalServiceUrl[]{url}, new SignalCdnUrl[0]);
        accountManager = new SignalServiceAccountManager(configuration, USERNAME, PASSWORD, "java-app");
    }

    private void installJCAProvider() {
        BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();

     //   System.err.println("Bouncy Catle Provider Version " + bouncyCastleProvider.getVersion() + " \nName " + bouncyCastleProvider.getName() + " | \n" + bouncyCastleProvider.getInfo());


       // Security.addProvider(bouncyCastleProvider);

        Provider[] providers = Security.getProviders();

        for (Provider provider : providers) {
            System.err.println("Installed security providers" + provider.getInfo() + "\n");
        }
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
