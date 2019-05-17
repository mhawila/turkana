package org.muzima.turkana.data;

import org.muzima.turkana.model.Registration;
import org.springframework.lang.NonNull;

/**
 * Created by Willa aka Baba Imu on 5/17/19.
 */
public interface RegistrationQuery {
    Registration getActiveRegistrationByDeviceId(@NonNull String deviceId);
    Registration getActiveRegistrationByPhoneNumber(@NonNull String phoneNumber);
}
