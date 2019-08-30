package com.agarella.exposer.Repository;

import com.agarella.exposer.Entity.GroupMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMessageRepository extends CrudRepository<GroupMessage, Long> {
    GroupMessage findByGroup(String name);
}
