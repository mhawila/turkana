package org.muzima.turkana.data;

import org.muzima.turkana.model.OneTimePreKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface OneTimePrekeyRepository extends JpaRepository<OneTimePreKey,Integer> {
}
