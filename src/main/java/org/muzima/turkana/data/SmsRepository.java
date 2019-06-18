package org.muzima.turkana.data;

import org.muzima.turkana.model.Sms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Samuel Owino
 */

@Repository
public interface SmsRepository extends JpaRepository<Sms,Long> {
}
