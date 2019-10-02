package org.muzima.turkana.service.impl;

import org.muzima.turkana.data.signal.SignedPrekeyRepository;
import org.muzima.turkana.model.SignedPreKey;
import org.muzima.turkana.service.SignedPrekeyService;
import org.muzima.turkana.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECKeyPair;
import org.whispersystems.libsignal.ecc.ECPrivateKey;
import org.whispersystems.libsignal.ecc.ECPublicKey;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class SignedPrekeyServiceImpl implements SignedPrekeyService {

    @Autowired
    SignedPrekeyRepository signedPrekeyRepository;

    @Override
    public SignedPreKeyRecord getSignedPreKey(int keyId) {

        try {
            SignedPreKey signedPreKey = signedPrekeyRepository.getOne(keyId);

            if (signedPreKey == null) return null;

            ECPublicKey publicKey = Curve.decodePoint(Base64.decode(signedPreKey.getPublic_key()), 0);
            ECPrivateKey privateKey = Curve.decodePrivatePoint(Base64.decode(signedPreKey.getPrivate_key()));
            byte[] signature = Base64.decode(signedPreKey.getSignature());
            long timestamp = signedPreKey.getTimestamp();

            return new SignedPreKeyRecord(keyId, timestamp, new ECKeyPair(publicKey, privateKey), signature);
        } catch (IOException | InvalidKeyException ex) {
            throw new AssertionError(ex);
        }


    }

    @Override
    public List<SignedPreKeyRecord> getAllSignedPreKeys() {

        try {
            List<SignedPreKeyRecord> results = new LinkedList<>();
            List<SignedPreKey> signedPreKeyList = new ArrayList<>();

            signedPreKeyList = signedPrekeyRepository.findAll();

            if (signedPreKeyList.isEmpty()) return new ArrayList<>();

            for (SignedPreKey signedPreKey : signedPreKeyList) {
                int keyId = signedPreKey.getKey_id();
                ECPublicKey publicKey = Curve.decodePoint(Base64.decode(signedPreKey.getPublic_key()), 0);
                ECPrivateKey privateKey = Curve.decodePrivatePoint(Base64.decode(signedPreKey.getPrivate_key()));
                byte[] signature = Base64.decode(signedPreKey.getSignature());
                long timestamp = signedPreKey.getTimestamp();

                results.add(new SignedPreKeyRecord(keyId, timestamp, new ECKeyPair(publicKey, privateKey), signature));

            }

            return results;
        } catch (InvalidKeyException | IOException e) {
            throw new AssertionError(e);
        }

    }

    @Override
    public void insertSignedPreKey(int keyId, SignedPreKeyRecord record) {
        SignedPreKey signedPreKey = new SignedPreKey();
        signedPreKey.setKey_id(keyId);
        signedPreKey.setPublic_key(Base64.encodeBytes(record.getKeyPair().getPublicKey().serialize()));
        signedPreKey.setPrivate_key(Base64.encodeBytes(record.getKeyPair().getPrivateKey().serialize()));
        signedPreKey.setSignature(Base64.encodeBytes(record.getSignature()));
        signedPreKey.setTimestamp(record.getTimestamp());

        signedPrekeyRepository.save(signedPreKey);
    }

    @Override
    public void removeSignedPreKey(int keyId) {
        for (SignedPreKey signedPreKey : signedPrekeyRepository.findAll()) {
            if (signedPreKey.getKey_id() == keyId)
                signedPrekeyRepository.delete(signedPreKey);
        }
    }

    @Override
    public void deleteAll() {
        signedPrekeyRepository.deleteAll();
    }
}
