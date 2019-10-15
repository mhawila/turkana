package org.muzima.turkana.data;

import org.muzima.turkana.model.Registration;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RegistrationRepository extends PagingAndSortingRepository<Registration, Long> {
}
