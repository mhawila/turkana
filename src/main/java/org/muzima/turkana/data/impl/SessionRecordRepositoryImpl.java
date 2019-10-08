package org.muzima.turkana.data.impl;

import org.muzima.turkana.data.signal.SessionRecordRepository;
import org.muzima.turkana.model.SessionRecord;
import org.muzima.turkana.model.SignalSession;
import org.muzima.turkana.utils.Base64;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public abstract class SessionRecordRepositoryImpl implements SessionRecordRepository {

    @Override
    public void deleteAllFor(String name) {
        for (SignalSession signalSession : findAll()) {
            if (signalSession.getAddress().equalsIgnoreCase(name)) delete(signalSession);
        }
    }

    @Override
    public List<Integer> getSubDevices(String name) {
        List<Integer> deviceIds = new ArrayList<>();
        for (SignalSession signalSession : findAll()) {
            if (signalSession.getAddress().equalsIgnoreCase(name)) {
                deviceIds.add(Integer.parseInt(signalSession.getDeviceId()));
            }
        }
        return deviceIds;
    }

    @Override
    public List<SessionRecord> getAllFor(String name) {
        List<SessionRecord> sessionRecordList = new ArrayList<>();
        for (SignalSession signalSession : findAll()) {
            sessionRecordList.add(new SessionRecord(signalSession.getRecord()));
        }

        return sessionRecordList;
    }

    @Override
    public org.whispersystems.libsignal.state.SessionRecord load(String name) {
        for (SignalSession signalSession : findAll()) {
            if (signalSession.getAddress().equalsIgnoreCase(name)) {
                try {
                    return new org.whispersystems.libsignal.state.SessionRecord(Base64.decode(name));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public Object store(String name) {
        for (SignalSession signalSession : findAll()) {
            if (signalSession.getAddress().equalsIgnoreCase(name)) {
                return save(signalSession);
            }
        }

        return null;
    }

    @Override
    public void delete(String name) {
        for (SignalSession signalSession : findAll()) {
            if (signalSession.getAddress().equalsIgnoreCase(name))
                delete(signalSession);
        }
    }
}
