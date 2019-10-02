package org.muzima.turkana.data.signal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.protocol.CiphertextMessage;
import org.whispersystems.libsignal.state.SessionRecord;
import org.whispersystems.libsignal.state.SessionStore;

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
            SessionRecord sessionRecord = sessionRecordRepository.load(address.getName());

            if (sessionRecord == null) {
                logger.info("No existing session information found.");
                return new SessionRecord();
            }

            return sessionRecord;
        }
    }

    @Override
    public void storeSession( SignalProtocolAddress address,  SessionRecord record) {
        synchronized (FILE_LOCK) {
            sessionRecordRepository.store(address.getName()), address.getDeviceId(), record);
        }
    }

    @Override
    public boolean containsSession(SignalProtocolAddress address) {
        synchronized (FILE_LOCK) {
            SessionRecord sessionRecord = sessionRecordRepository.load(address.getName());

            return sessionRecord != null &&
                sessionRecord.getSessionState().hasSenderChain() &&
                sessionRecord.getSessionState().getSessionVersion() == CiphertextMessage.CURRENT_VERSION;
        }
    }

    @Override
    public void deleteSession(SignalProtocolAddress address) {
        synchronized (FILE_LOCK) {
            sessionRecordRepository.delete(address.getName()), address.getDeviceId());
        }
    }

    @Override
    public void deleteAllSessions(String name) {
        synchronized (FILE_LOCK) {
            sessionRecordRepository.deleteAllFor(name));
        }
    }

    @Override
    public List<Integer> getSubDeviceSessions(String name) {
        synchronized (FILE_LOCK) {
            return sessionRecordRepository.getSubDevices(name);
        }
    }

    public void archiveSiblingSessions(SignalProtocolAddress address) {
        synchronized (FILE_LOCK) {
            List<org.muzima.turkana.model.SessionRecord> sessions = sessionRecordRepository.getAllFor(address.getName());

            for (org.muzima.turkana.model.SessionRecord row : sessions) {
                if (row.getDeviceId() != address.getDeviceId()) {
                    row.getRecord().archiveCurrentState();
                    storeSession(new SignalProtocolAddress(row.getAddress().serialize(), row.getDeviceId()), row.getRecord());
                }
            }
        }
    }

    public void archiveAllSessions() {
        synchronized (FILE_LOCK) {
            List<org.muzima.turkana.model.SessionRecord> sessions = sessionRecordRepository.findAll();

            for (org.muzima.turkana.model.SessionRecord row : sessions) {
                row.getRecord().archiveCurrentState();
                storeSession(new SignalProtocolAddress(row.getAddress().serialize(), row.getDeviceId()), row.getRecord());
            }
        }
    }
}
