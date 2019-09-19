package org.muzima.turkana.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Initialize Firebase App to Receive
 * Remote Message Pus Notifications from
 * Signal Server
 *
 * @author Samuel Owino
 */

@Service
public class FcmInitializer {

    @Value("${application.properties}")
    private String firebaseConfigPath;

    private Logger logger = LoggerFactory.getLogger(FcmInitializer.class);

    @PostConstruct
    public void initialize() {
        try {
            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream()))
                .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(firebaseOptions);
            }
        } catch (IOException ex) {
            logger.info("Unable to initialize fcm  | " + ex);
        }

    }
}
