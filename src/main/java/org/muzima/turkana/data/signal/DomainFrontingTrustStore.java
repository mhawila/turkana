package org.muzima.turkana.data.signal;

import org.whispersystems.signalservice.api.push.TrustStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DomainFrontingTrustStore implements TrustStore {

    public DomainFrontingTrustStore() {
    }

    @Override
    public InputStream getKeyStoreInputStream() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(
                new File(getClass().getClassLoader().getResource("censorship_fronting.store").getFile()
                ));

            return inputStream;

        } catch (FileNotFoundException e) {
            throw new AssertionError("Keystore file not found.");
        }
    }

    @Override
    public String getKeyStorePassword() {
        return "whisper";
    }

}
