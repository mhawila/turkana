package org.muzima.turkana.service;

import org.springframework.stereotype.Service;
import org.whispersystems.libsignal.state.PreKeyRecord;

@Service
public interface OneTimePrekeyService {

    public PreKeyRecord getPreKey(int keyId);

    public void insertPreKey(int keyId, PreKeyRecord record);

    public void removePreKey(int keyId);

    void deleteAll();
}
