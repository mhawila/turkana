package org.muzima.turkana.data;

import org.muzima.turkana.model.Identity;
import org.muzima.turkana.model.VerifiedStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.whispersystems.libsignal.IdentityKey;

public interface IdentityRepository extends JpaRepository<Identity,Long> {

    Identity getIdentity(String signalAddress);

    void saveIdentity(String signalAddress, IdentityKey identityKey, VerifiedStatus aDefault, boolean b, long currentTimeMillis, boolean nonBlockingApproval);

    void setApproval(String signalAddress, boolean nonBlockingApproval);
}
