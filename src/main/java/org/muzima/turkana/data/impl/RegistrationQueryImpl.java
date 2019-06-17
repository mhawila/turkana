package org.muzima.turkana.data.impl;

import com.querydsl.jpa.impl.JPAQuery;
import org.muzima.turkana.data.RegistrationQuery;
import org.muzima.turkana.model.QRegistration;
import org.muzima.turkana.model.Registration;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Willa aka Baba Imu on 5/17/19.
 *
 * This class is used for custom queries that are difficult to express using the Spring Data convention.
 * Use the Corresponding RegistrationRepository which provide standard methods such as save, findAll, count (See SpringData documentation)
 */

@Repository
public class RegistrationQueryImpl implements RegistrationQuery {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Registration getActiveRegistrationByDeviceId(@NonNull String deviceId) {
        QRegistration registration = QRegistration.registration;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        return query.select(registration).from(registration).where(
                registration.deviceId.eq(deviceId)
                        .and(registration.retired.eq(false))
        ).orderBy(registration.dateRegistered.desc()).fetchFirst();
    }

    @Override
    public Registration getActiveRegistrationByPhoneNumber(@NonNull String phoneNumber) {
        QRegistration registration = QRegistration.registration;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        return query.select(registration).from(registration).where(
                registration.phoneNumber.eq(phoneNumber)
                        .and(registration.retired.eq(false))
        ).orderBy(registration.dateRegistered.desc()).fetchFirst();
    }
}
