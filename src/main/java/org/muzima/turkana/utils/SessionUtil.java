package org.muzima.turkana.utils;

import org.muzima.turkana.data.signal.TextSecureSessionStore;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.SessionStore;
import org.whispersystems.signalservice.api.push.SignalServiceAddress;

public class SessionUtil {

    public static boolean hasSession(String address) {
        SessionStore sessionStore = new TextSecureSessionStore();
        SignalProtocolAddress axolotlAddress = new SignalProtocolAddress(address, SignalServiceAddress.DEFAULT_DEVICE_ID);

        return sessionStore.containsSession(axolotlAddress);
    }

    public static void archiveSiblingSessions(SignalProtocolAddress address) {
        TextSecureSessionStore sessionStore = new TextSecureSessionStore();
        sessionStore.archiveSiblingSessions(address);
    }

    public static void archiveAllSessions() {
        new TextSecureSessionStore().archiveAllSessions();
    }

}
