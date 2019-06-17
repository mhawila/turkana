package org.muzima.turkana.data;

import org.muzima.turkana.model.Registration;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Samuel Owino
 */

@Repository
public interface RegistrationRepository extends PagingAndSortingRepository<Registration, Long> {
}
