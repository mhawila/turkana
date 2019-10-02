package org.muzima.turkana.utils;

import org.muzima.turkana.data.SmsRepository;
import org.muzima.turkana.data.signal.TextSecureIdentityKeyStore;
import org.muzima.turkana.data.signal.TextSecureSessionStore;
import org.muzima.turkana.model.Identity;
import org.muzima.turkana.model.Recipient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.IdentityKeyStore;
import org.whispersystems.libsignal.state.SessionRecord;
import org.whispersystems.libsignal.state.SessionStore;
import org.whispersystems.libsignal.util.guava.Optional;
import org.whispersystems.signalservice.internal.util.concurrent.ListenableFuture;
import org.whispersystems.signalservice.internal.util.concurrent.SettableFuture;

import static org.whispersystems.libsignal.SessionCipher.SESSION_LOCK;

public class IdentityUtil {

    @Autowired
    SmsRepository smsRepository;

    private static final String TAG = IdentityUtil.class.getSimpleName();
    private static Logger logger = LoggerFactory.getLogger(IdentityKeyUtil.class);

    public static ListenableFuture<Optional<Identity>> getRemoteIdentityKey(final Recipient recipient) {
        final SettableFuture<Optional<Identity>> future = new SettableFuture<>();
        return future;
    }

    public void markIdentityVerified(Recipient recipient, boolean verified, boolean remote) {
        long time = System.currentTimeMillis();
    }

    public static void markIdentityUpdate(Recipient recipient) {
        long time = System.currentTimeMillis();
    }

    public static void saveIdentity(String number, IdentityKey identityKey) {
        synchronized (SESSION_LOCK) {
            IdentityKeyStore identityKeyStore = new TextSecureIdentityKeyStore();
            SessionStore sessionStore = new TextSecureSessionStore();
            SignalProtocolAddress address = new SignalProtocolAddress(number, 1);

            if (identityKeyStore.saveIdentity(address, identityKey)) {
                if (sessionStore.containsSession(address)) {
                    SessionRecord sessionRecord = sessionStore.loadSession(address);
                    sessionRecord.archiveCurrentState();

                    sessionStore.storeSession(address, sessionRecord);
                }
            }
        }
    }

}
