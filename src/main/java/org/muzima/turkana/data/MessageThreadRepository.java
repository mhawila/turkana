package org.muzima.turkana.data;

import org.muzima.turkana.model.MessageThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Samuel Owino
 */

@Repository
public interface MessageThreadRepository extends JpaRepository<MessageThread,String> {

}
