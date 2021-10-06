package csci310.repository;

import csci310.entity.Event;
import csci310.entity.UserEventMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserEventMapRepository extends JpaRepository<UserEventMap, Long> {
    List<UserEventMap> findUserEventMapByUserId(Long userId);

    List<UserEventMap> findUserEventMapByEventId(Long eventId);
}
