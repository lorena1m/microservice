package com.agarella.exposer.Repository;

import com.agarella.exposer.Entity.GroupMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMessageRepository extends CrudRepository<GroupMessage, Long> {
    GroupMessage findByGroup(String name);
    Iterable<GroupMessage> findTop5ByOrderByTotalDesc();
    List<GroupMessage> findByGroupIgnoreCaseContaining(String group);
}
