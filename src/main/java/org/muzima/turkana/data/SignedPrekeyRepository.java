package org.muzima.turkana.data;

import org.muzima.turkana.model.SignedPreKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface SignedPrekeyRepository extends JpaRepository<SignedPreKey,Integer> {
}
