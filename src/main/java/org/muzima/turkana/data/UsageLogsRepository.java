package org.muzima.turkana.data;

import org.muzima.turkana.model.UsageLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Samuel Owino
 */

@Repository
public interface UsageLogsRepository extends JpaRepository<UsageLogs,Long> {
}
