package org.muzima.turkana.service;

import org.muzima.turkana.model.Mms;
import org.muzima.turkana.model.Registration;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

/**
 * @author Samuel Owino
 */

@Service
public interface MmsService {

    public Mms saveMms(Mms mms);

    public List<Mms> saveAllMms(List<Mms> mmsList);

    public Mms getMms(Long id);

    public List<Mms> getAllMms();

    public void updateMms(Mms mms);

    public void deleteMms(Long id);

    /**
     *
     * @param mms
     * @param signature
     * @param registration
     * @return
     */
    Mms verifyMmsAndSave(Mms mms, final byte[] signature, Registration registration) throws NoSuchAlgorithmException,
        InvalidKeySpecException, InvalidKeyException, SignatureException, MessageVerificationException;

    /**
     *
     * @param mms
     * @param signature
     * @param registration
     * @return
     */
    List<Mms> verifyMultipleMmsAndSave(List<Mms> mms, final byte[] signature, Registration registration) throws NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, SignatureException, MessageVerificationException;
}
