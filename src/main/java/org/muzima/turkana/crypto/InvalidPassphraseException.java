package org.muzima.turkana.crypto;

public class InvalidPassphraseException extends Exception {

    public InvalidPassphraseException() {
        super();
    }

    public InvalidPassphraseException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidPassphraseException(Throwable throwable) {
        super(throwable);
    }

    public InvalidPassphraseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
