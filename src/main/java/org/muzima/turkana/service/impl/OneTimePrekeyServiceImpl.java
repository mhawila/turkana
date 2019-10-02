package org.muzima.turkana.service.impl;

import org.muzima.turkana.data.signal.OneTimePrekeyRepository;
import org.muzima.turkana.model.OneTimePreKey;
import org.muzima.turkana.service.OneTimePrekeyService;
import org.muzima.turkana.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECKeyPair;
import org.whispersystems.libsignal.ecc.ECPrivateKey;
import org.whispersystems.libsignal.ecc.ECPublicKey;
import org.whispersystems.libsignal.state.PreKeyRecord;

import java.io.IOException;

@Service
public class OneTimePrekeyServiceImpl implements OneTimePrekeyService {

    @Autowired
    OneTimePrekeyRepository prekeyRepository;

    @Override
    public PreKeyRecord getPreKey(int keyId) {
        try {
            OneTimePreKey oneTimePreKey = prekeyRepository.getOne(keyId);
            ECPublicKey publicKey = Curve.decodePoint(Base64.decode(oneTimePreKey.getPublic_key()), 0);
            ECPrivateKey privateKey = Curve.decodePrivatePoint(Base64.decode(oneTimePreKey.getPrivate_key()));

            return new PreKeyRecord(keyId, new ECKeyPair(publicKey, privateKey));
        } catch (InvalidKeyException | IOException ex) {
            throw new AssertionError(ex);
        }

    }

    @Override
    public void insertPreKey(int keyId, PreKeyRecord record) {

        System.out.println("Insert OneTimePreKey " + record.getKeyPair().getPublicKey().toString());

        OneTimePreKey oneTimePreKey = new OneTimePreKey();
        oneTimePreKey.setKey_id(keyId);
        oneTimePreKey.setPublic_key(Base64.encodeBytes(record.getKeyPair().getPublicKey().serialize()));
        oneTimePreKey.setPrivate_key(Base64.encodeBytes(record.getKeyPair().getPrivateKey().serialize()));

        prekeyRepository.save(oneTimePreKey);
    }

    @Override
    public void removePreKey(int keyId) {
        for (OneTimePreKey oneTimePreKey : prekeyRepository.findAll()) {
            if (oneTimePreKey.getKey_id() == keyId)
                prekeyRepository.delete(oneTimePreKey);
        }
    }

    @Override
    public void deleteAll() {
        prekeyRepository.deleteAll();
    }
}
