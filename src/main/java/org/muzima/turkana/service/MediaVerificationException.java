package org.muzima.turkana.service;

import org.muzima.turkana.model.MediaMetadata;
import org.muzima.turkana.model.Sms;

import java.util.List;

/**
 * @uthor Willa Mhawila<a.mhawila@gmail.com> on 8/19/19.
 */
public class MediaVerificationException extends Exception {
    private MediaMetadata metadata;
    private byte[] signature;
    private String publicKey;
    public MediaVerificationException(MediaMetadata mediaMetadata, byte[] signature, String publicKey) {
        super("Could not verify media (metadata really) integrity against signature");
        this.metadata = mediaMetadata;
        this.signature = signature;
        this.publicKey = publicKey;
    }
}
