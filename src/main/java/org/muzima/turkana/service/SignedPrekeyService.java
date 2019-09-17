package org.muzima.turkana.service;

import org.springframework.stereotype.Service;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;

import java.util.List;

@Service
public interface SignedPrekeyService {

    public SignedPreKeyRecord getSignedPreKey(int keyId);

    public List<SignedPreKeyRecord> getAllSignedPreKeys();

    public void insertSignedPreKey(int keyId, SignedPreKeyRecord record);

    void removeSignedPreKey(int signedPreKeyId);

    void deleteAll();
}
