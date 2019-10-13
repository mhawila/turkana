package org.muzima.turkana.data.signal;

import org.muzima.turkana.model.SignalSession;
import org.muzima.turkana.utils.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.protocol.CiphertextMessage;
import org.whispersystems.libsignal.state.SessionRecord;
import org.whispersystems.libsignal.state.SessionState;
import org.whispersystems.libsignal.state.SessionStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextSecureSessionStore implements SessionStore {

    private static final String TAG = TextSecureSessionStore.class.getSimpleName();

    @Autowired
    SessionRecordRepository sessionRecordRepository;

    private Logger logger = LoggerFactory.getLogger(TextSecureSessionStore.class);

    private static final Object FILE_LOCK = new Object();

    public TextSecureSessionStore() {
    }

    @Override
    public SessionRecord loadSession(SignalProtocolAddress address) {
        synchronized (FILE_LOCK) {
            SessionRecord sessionRecord =  new SessionRecord();

            for (SignalSession signalSession : sessionRecordRepository.findAll()) {
                if (signalSession.getAddress().equalsIgnoreCase(address.getName())) {
                    try {
                        sessionRecord = new SessionRecord(Base64.decode(signalSession.getRecord()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (sessionRecord == null) {
                logger.info("No existing session information found.");
                return new SessionRecord();
            }

            return sessionRecord;
        }
    }

    @Override
    public void storeSession(SignalProtocolAddress address, SessionRecord record) {
        synchronized (FILE_LOCK) {
            sessionRecordRepository.save(new SignalSession(address.getName(), String.valueOf(address.getDeviceId()), record.toString()));
        }
    }

    @Override
    public boolean containsSession(SignalProtocolAddress address) {
        synchronized (FILE_LOCK) {
            SessionRecord sessionRecord = new SessionRecord();
            for (SignalSession signalSession : sessionRecordRepository.findAll()) {
                if (signalSession.getAddress().equalsIgnoreCase(address.getName())){
                    sessionRecord.setState( new SessionState());
                }

            }

            return sessionRecord != null &&
                sessionRecord.getSessionState().hasSenderChain() &&
                sessionRecord.getSessionState().getSessionVersion() == CiphertextMessage.CURRENT_VERSION;
        }
    }

    @Override
    public void deleteSession(SignalProtocolAddress address) {
        synchronized (FILE_LOCK) {
            for (SignalSession signalSession : sessionRecordRepository.findAll()) {
                if (signalSession.getAddress().equalsIgnoreCase(address.getName()))
                    sessionRecordRepository.delete(signalSession);
            }
        }
    }

    @Override
    public void deleteAllSessions(String name) {
        synchronized (FILE_LOCK) {
            for (SignalSession signalSession : sessionRecordRepository.findAll()) {
                if (signalSession.getAddress().equalsIgnoreCase(name))
                    sessionRecordRepository.delete(signalSession);
            }
        }
    }

    @Override
    public List<Integer> getSubDeviceSessions(String name) {
        synchronized (FILE_LOCK) {
            return new ArrayList<>();
        }
    }

    public void archiveSiblingSessions(SignalProtocolAddress address) throws IOException {
        synchronized (FILE_LOCK) {
            List<org.muzima.turkana.model.SessionRecord> sessions = new ArrayList<>();

            for (SignalSession signalSession : sessionRecordRepository.findAll()) {
                if (signalSession.getAddress().equalsIgnoreCase(address.getName())){
                    sessions.add( new org.muzima.turkana.model.SessionRecord());
                }
            }

            for (org.muzima.turkana.model.SessionRecord row : sessions) {
                if (Integer.parseInt(row.getDevice()) != address.getDeviceId()) {
                    new org.muzima.turkana.model.SessionRecord(row.getRecord()).archiveCurrentState();

                    storeSession(new SignalProtocolAddress(row.getAddress(), 1),
                        new SessionRecord(row.getRecord().getBytes()));
                }
            }
        }
    }

    public void archiveAllSessions() throws IOException {
        synchronized (FILE_LOCK) {
            List<SignalSession> sessions = sessionRecordRepository.findAll();

            for (SignalSession row : sessions) {
                storeSession(new SignalProtocolAddress(row.getAddress(), Integer.parseInt(row.getDeviceId())), new SessionRecord(row.getRecord().getBytes()));
            }
        }
    }
}
