package org.muzima.turkana.data;

import org.muzima.turkana.model.IdentityKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface IdentityKeyRepository extends JpaRepository<IdentityKey,Integer> {
}
