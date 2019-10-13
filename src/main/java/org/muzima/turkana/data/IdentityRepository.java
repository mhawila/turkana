package org.muzima.turkana.data;

import org.muzima.turkana.model.Identity;
import org.muzima.turkana.model.VerifiedStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.whispersystems.libsignal.IdentityKey;

public interface IdentityRepository extends JpaRepository<Identity,Long> {

}
