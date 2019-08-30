package com.agarella.exposer.Repository;

import com.agarella.exposer.Entity.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
    Message findByContent(String message);
}
