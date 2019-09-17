package org.muzima.turkana.service.impl;

import org.muzima.turkana.data.MmsRepository;
import org.muzima.turkana.model.Mms;
import org.muzima.turkana.model.Registration;
import org.muzima.turkana.service.MessageVerificationException;
import org.muzima.turkana.service.MmsService;
import org.muzima.turkana.utils.CryptoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import static org.muzima.turkana.utils.Constants.SECURITY.MESSAGE_DIGEST_ALGORITHM;
import static org.muzima.turkana.utils.Constants.SECURITY.SIGNATURE_ALGORITHM;

/**
 * @author Samuel Owino
 */

@Service
public class MmsServiceImpl implements MmsService {
    protected final static Logger LOGGER = LoggerFactory.getLogger(MmsServiceImpl.class);
    @Autowired
    private MmsRepository mmsRepository;

    @Override
    public Mms saveMms(Mms mms) {
        return mmsRepository.save(mms);
    }

    @Override
    public List<Mms> saveAllMms(List<Mms> mmsList) {
        return mmsRepository.saveAll(mmsList);
    }

    @Override
    public Mms getMms(Long id) {
        return mmsRepository.findById(id).orElseGet(Mms::new);
    }

    @Override
    public List<Mms> getAllMms() {
        return mmsRepository.findAll();
    }

    @Override
    public void updateMms(Mms mms) {
        mmsRepository.save(mms);
    }

    @Override
    public void deleteMms(Long id) {
        mmsRepository.deleteById(id);
    }

    @Override
    public Mms verifyMmsAndSave(final Mms mms, final byte[] signature, final Registration registration) throws NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, SignatureException, MessageVerificationException {
        Signature rsaSignatureInstance = Signature.getInstance(SIGNATURE_ALGORITHM);
        rsaSignatureInstance.initVerify(CryptoUtils.getPublicKeyFromText(registration.getPublicKey()));

        rsaSignatureInstance.update(mms.getBody().getBytes());

        if(rsaSignatureInstance.verify(signature)) {
            LOGGER.info("Signature verified successfully for MMS");
            return saveMms(mms);
        } else {
            LOGGER.error("Signature verification failed, MMS not saved");
            throw new MessageVerificationException(mms, signature, registration.getPublicKey());
        }
    }

    @Override
    public List<Mms> verifyMultipleMmsAndSave(List<Mms> mmsList, byte[] signature, Registration registration) throws NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, SignatureException, MessageVerificationException {
        final StringBuilder builder = new StringBuilder();
        for (Mms mms: mmsList) {
            builder.append(mms.getBody());
        }

        MessageDigest messageDigest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM);
        byte[] hashedConcatenatedMessages = messageDigest.digest(builder.toString().getBytes());

        Signature rsaSignatureInstance = Signature.getInstance(SIGNATURE_ALGORITHM);
        rsaSignatureInstance.initVerify(CryptoUtils.getPublicKeyFromText(registration.getPublicKey()));
        rsaSignatureInstance.update(hashedConcatenatedMessages);

        if(rsaSignatureInstance.verify(signature)) {
            LOGGER.info("Signature verified successfully for mms list");
            return saveAllMms(mmsList);
        } else {
            LOGGER.error("Signature verification failed, mms list not saved");
            throw new MessageVerificationException(mmsList, signature, registration.getPublicKey());
        }
    }
}
