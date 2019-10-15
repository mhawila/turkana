package org.muzima.turkana.data;

import org.muzima.turkana.model.MediaMetadata;
import org.muzima.turkana.model.Registration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Willa aka Baba Imu on 6/20/19.
 */
public interface MediaMetadataRepository extends JpaRepository<MediaMetadata, Long> {
    List<MediaMetadata> findAllByRegistration(Registration registration, Pageable pageable);
}
