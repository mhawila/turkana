package org.muzima.turkana.data;

import org.muzima.turkana.model.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {

}
