package org.muzima.turkana.data.signal;

import org.muzima.turkana.model.SessionRecord;
import org.muzima.turkana.model.SignalSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRecordRepository extends JpaRepository<SignalSession,Long> {
    void deleteAllFor(String name);

    List<Integer> getSubDevices(String name);

    List<SessionRecord> getAllFor(String name);
}
