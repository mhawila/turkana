package org.muzima.turkana.utils;

import org.springframework.beans.factory.annotation.Value;
import org.whispersystems.signalservice.api.util.CredentialsProvider;

public class DynamicCredentialsProvider implements CredentialsProvider {

    @Value("${turkana.phonenumber}")
    public String LOCAL_PHONE_NUMBER;

    public DynamicCredentialsProvider() {
    }

    @Override
    public String getUser() {
        return LOCAL_PHONE_NUMBER;
    }

    @Override
    public String getPassword() {
        return "push service password";
    }

    @Override
    public String getSignalingKey() {
        return "signaling key";
    }
}
