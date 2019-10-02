package org.muzima.turkana.service;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.TlsVersion;
import org.muzima.turkana.data.signal.DomainFrontingTrustStore;
import org.muzima.turkana.data.RegistrationRepository;
import org.muzima.turkana.data.signal.SignalProtocolStoreImpl;
import org.muzima.turkana.data.signal.SignalServiceTrustStore;
import org.muzima.turkana.utils.BuildConfig;
import org.muzima.turkana.utils.DynamicCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.whispersystems.libsignal.*;
import org.whispersystems.libsignal.state.SignalProtocolStore;
import org.whispersystems.signalservice.api.SignalServiceMessageReceiver;
import org.whispersystems.signalservice.api.crypto.SignalServiceCipher;
import org.whispersystems.signalservice.api.messages.SignalServiceContent;
import org.whispersystems.signalservice.api.messages.SignalServiceEnvelope;
import org.whispersystems.signalservice.api.push.SignalServiceAddress;
import org.whispersystems.signalservice.api.push.TrustStore;
import org.whispersystems.signalservice.api.websocket.ConnectivityListener;
import org.whispersystems.signalservice.internal.configuration.SignalCdnUrl;
import org.whispersystems.signalservice.internal.configuration.SignalServiceConfiguration;
import org.whispersystems.signalservice.internal.configuration.SignalServiceUrl;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class RetrieveMessagesService {

    private static Logger logger = LoggerFactory.getLogger(RetrieveMessagesService.class);

    @Autowired
    private static RegistrationRepository registrationRepository;

    @Value("${turkana.phonenumber}")
    public static String LOCAL_PHONE_NUMBER;

    private static final String COUNTRY_CODE_EGYPT = "+20";
    private static final String COUNTRY_CODE_UAE = "+971";
    private static final String COUNTRY_CODE_OMAN = "+968";
    private static final String COUNTRY_CODE_QATAR = "+974";

    private static final String SERVICE_REFLECTOR_HOST = "europe-west1-signal-cdn-reflector.cloudfunctions.net";

    private static final ConnectionSpec GMAPS_CONNECTION_SPEC = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
        .tlsVersions(TlsVersion.TLS_1_2)
        .cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,
            CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
            CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384,
            CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA)
        .supportsTlsExtensions(true)
        .build();

    private static final ConnectionSpec GMAIL_CONNECTION_SPEC = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
        .tlsVersions(TlsVersion.TLS_1_2)
        .cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
            CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA)
        .supportsTlsExtensions(true)
        .build();

    private static final ConnectionSpec PLAY_CONNECTION_SPEC = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
        .tlsVersions(TlsVersion.TLS_1_2)
        .cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
            CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA)
        .supportsTlsExtensions(true)
        .build();


    private static String[] censoredCountries;
    private static SignalServiceConfiguration uncensoredConfiguration;

    public RetrieveMessagesService() {

        final TrustStore trustStore = new DomainFrontingTrustStore();
        final SignalServiceUrl baseGoogleService = new SignalServiceUrl("https://www.google.com/service", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);
        final SignalServiceUrl baseAndroidService = new SignalServiceUrl("https://android.clients.google.com/service", SERVICE_REFLECTOR_HOST, trustStore, PLAY_CONNECTION_SPEC);
        final SignalServiceUrl mapsOneAndroidService = new SignalServiceUrl("https://clients3.google.com/service", SERVICE_REFLECTOR_HOST, trustStore, GMAPS_CONNECTION_SPEC);
        final SignalServiceUrl mapsTwoAndroidService = new SignalServiceUrl("https://clients4.google.com/service", SERVICE_REFLECTOR_HOST, trustStore, GMAPS_CONNECTION_SPEC);
        final SignalServiceUrl mailAndroidService = new SignalServiceUrl("https://inbox.google.com/service", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);
        final SignalServiceUrl egyptGoogleService = new SignalServiceUrl("https://www.google.com.eg/service", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);
        final SignalServiceUrl uaeGoogleService = new SignalServiceUrl("https://www.google.ae/service", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);
        final SignalServiceUrl omanGoogleService = new SignalServiceUrl("https://www.google.com.om/service", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);
        final SignalServiceUrl qatarGoogleService = new SignalServiceUrl("https://www.google.com.qa/service", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);

        final SignalCdnUrl baseGoogleCdn = new SignalCdnUrl("https://www.google.com/cdn", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);
        final SignalCdnUrl baseAndroidCdn = new SignalCdnUrl("https://android.clients.google.com/cdn", SERVICE_REFLECTOR_HOST, trustStore, PLAY_CONNECTION_SPEC);
        final SignalCdnUrl mapsOneAndroidCdn = new SignalCdnUrl("https://clients3.google.com/cdn", SERVICE_REFLECTOR_HOST, trustStore, GMAPS_CONNECTION_SPEC);
        final SignalCdnUrl mapsTwoAndroidCdn = new SignalCdnUrl("https://clients4.google.com/cdn", SERVICE_REFLECTOR_HOST, trustStore, GMAPS_CONNECTION_SPEC);
        final SignalCdnUrl mailAndroidCdn = new SignalCdnUrl("https://inbox.google.com/cdn", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);
        final SignalCdnUrl egyptGoogleCdn = new SignalCdnUrl("https://www.google.com.eg/cdn", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);
        final SignalCdnUrl uaeGoogleCdn = new SignalCdnUrl("https://www.google.ae/cdn", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);
        final SignalCdnUrl omanGoogleCdn = new SignalCdnUrl("https://www.google.com.om/cdn", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);
        final SignalCdnUrl qatarGoogleCdn = new SignalCdnUrl("https://www.google.com.qa/cdn", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);

        final SignalContactDiscoveryUrl baseGoogleDiscovery = new SignalContactDiscoveryUrl("https://www.google.com/directory", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);
        final SignalContactDiscoveryUrl baseAndroidDiscovery = new SignalContactDiscoveryUrl("https://android.clients.google.com/directory", SERVICE_REFLECTOR_HOST, trustStore, PLAY_CONNECTION_SPEC);
        final SignalContactDiscoveryUrl mapsOneAndroidDiscovery = new SignalContactDiscoveryUrl("https://clients3.google.com/directory", SERVICE_REFLECTOR_HOST, trustStore, GMAPS_CONNECTION_SPEC);
        final SignalContactDiscoveryUrl mapsTwoAndroidDiscovery = new SignalContactDiscoveryUrl("https://clients4.google.com/directory", SERVICE_REFLECTOR_HOST, trustStore, GMAPS_CONNECTION_SPEC);
        final SignalContactDiscoveryUrl mailAndroidDiscovery = new SignalContactDiscoveryUrl("https://inbox.google.com/directory", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);
        final SignalContactDiscoveryUrl egyptGoogleDiscovery = new SignalContactDiscoveryUrl("https://www.google.com.eg/directory", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);
        final SignalContactDiscoveryUrl uaeGoogleDiscovery = new SignalContactDiscoveryUrl("https://www.google.ae/directory", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);
        final SignalContactDiscoveryUrl omanGoogleDiscovery = new SignalContactDiscoveryUrl("https://www.google.com.om/directory", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);
        final SignalContactDiscoveryUrl qatarGoogleDiscovery = new SignalContactDiscoveryUrl("https://www.google.com.qa/directory", SERVICE_REFLECTOR_HOST, trustStore, GMAIL_CONNECTION_SPEC);

        this.uncensoredConfiguration = new SignalServiceConfiguration(new SignalServiceUrl[]
            {new SignalServiceUrl(BuildConfig.SIGNAL_URL, new SignalServiceTrustStore())},
            new SignalCdnUrl[]{new SignalCdnUrl(BuildConfig.SIGNAL_CDN_URL, new SignalServiceTrustStore())});

    }

    public static SignalServiceConfiguration getUncensoredConfiguration() {
        return uncensoredConfiguration;
    }

    public static void scheduleMessageRetrieval() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    retrieveMessagesIfExists();
                } catch (IOException e) {
                    logger.error("Unable to process existing message, IOException " + e);
                }
            }
        };


        Timer timer = new Timer();
        long delay = 0;
        long intervalPeriod = 5 * 1000;
        timer.scheduleAtFixedRate(timerTask, delay, intervalPeriod);
    }

    private static void retrieveMessagesIfExists() throws IOException {
        SignalServiceMessageReceiver receiver = provideSignalMessageReceiver();
        receiver.retrieveMessages(new SignalServiceMessageReceiver.MessageReceivedCallback() {
            @Override
            public void onMessage(SignalServiceEnvelope signalServiceEnvelope) {
                try {
                    processAndSaveEnvelop(signalServiceEnvelope);
                } catch (NoSessionException | UntrustedIdentityException | LegacyMessageException | InvalidVersionException | InvalidMessageException | DuplicateMessageException | InvalidKeyException | InvalidKeyIdException e) {
                    logger.error("Unable to process signal message" + e);
                }
            }
        });
    }

    /**
     *
     * @param envelope
     * @throws NoSessionException
     * @throws UntrustedIdentityException
     * @throws LegacyMessageException
     * @throws InvalidVersionException
     * @throws InvalidMessageException
     * @throws DuplicateMessageException
     * @throws InvalidKeyException
     * @throws InvalidKeyIdException
     *
     * Extract the actual Message Content from the Encrypted Cipher
     * and Save as a registration message
     */
    private static void processAndSaveEnvelop(SignalServiceEnvelope envelope) throws NoSessionException, UntrustedIdentityException, LegacyMessageException, InvalidVersionException, InvalidMessageException, DuplicateMessageException, InvalidKeyException, InvalidKeyIdException {
        SignalProtocolStore axolotlStore = new SignalProtocolStoreImpl();
        SignalServiceAddress localAddress = new SignalServiceAddress(LOCAL_PHONE_NUMBER);
        SignalServiceCipher cipher = new SignalServiceCipher(localAddress, axolotlStore);
        SignalServiceContent content = cipher.decrypt(envelope);

    }

    public static synchronized SignalServiceMessageReceiver provideSignalMessageReceiver() {
        SignalServiceMessageReceiver messageReceiver = null;
        messageReceiver = new SignalServiceMessageReceiver(uncensoredConfiguration,
            new DynamicCredentialsProvider(),
            BuildConfig.USER_AGENT,
            new PipeConnectivityListener());

        return messageReceiver;
    }

    public static SignalServiceConfiguration getConfiguration(@Nullable String localNumber) {
        return uncensoredConfiguration;
    }

    public static class PipeConnectivityListener implements ConnectivityListener {

        @Override
        public void onConnected() {
            logger.info("onConnected()");
        }

        @Override
        public void onConnecting() {
            logger.info("onConnecting()");
        }

        @Override
        public void onDisconnected() {
            logger.info("onDisconnected()");
        }

        @Override
        public void onAuthenticationFailure() {
            logger.info("onAuthenticationFailure()");
        }

    }
}
