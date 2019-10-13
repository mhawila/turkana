package org.muzima.turkana.data.signal;

import org.muzima.turkana.model.SignalSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRecordRepository extends JpaRepository<SignalSession,Long> {

}
