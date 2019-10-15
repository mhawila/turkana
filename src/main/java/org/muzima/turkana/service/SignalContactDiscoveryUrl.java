package org.muzima.turkana.service;

import okhttp3.ConnectionSpec;
import org.whispersystems.signalservice.api.push.TrustStore;
import org.whispersystems.signalservice.internal.configuration.SignalUrl;

public class SignalContactDiscoveryUrl extends SignalUrl {

    public SignalContactDiscoveryUrl(String url, TrustStore trustStore) {
        super(url, trustStore);
    }

    public SignalContactDiscoveryUrl(String url, String hostHeader, TrustStore trustStore, ConnectionSpec connectionSpec) {
        super(url, hostHeader, trustStore, connectionSpec);
    }
}
