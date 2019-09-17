package org.muzima.turkana.service;

import org.muzima.turkana.model.Message;

import java.util.List;

/**
 * @uthor Willa Mhawila<a.mhawila@gmail.com> on 8/16/19.
 */
public class MessageVerificationException extends Exception {
    private List<? extends Message> messageList;
    private byte[] signature;
    private String publicKey;
    private Message message;

    public MessageVerificationException(Message message, byte[] signature, String publicKey) {
        super("Could not verify message integrity against signature");
        this.message = message;
        this.signature = signature;
        this.publicKey = publicKey;
    }

    public MessageVerificationException(List<? extends Message> messageList, byte[] signature, String publicKey) {
        super("Could not verify messages integrity against signature");
        this.messageList = messageList;
        this.signature = signature;
        this.publicKey = publicKey;
    }
}
