package org.muzima.turkana.data.signal;

import org.muzima.turkana.model.SignedPreKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignedPrekeyRepository extends JpaRepository<SignedPreKey,Integer> {
}
