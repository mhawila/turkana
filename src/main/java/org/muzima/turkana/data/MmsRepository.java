package org.muzima.turkana.data;

import org.muzima.turkana.model.Mms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Samuel Owino
 */


public interface MmsRepository extends JpaRepository<Mms,Long> {
}
