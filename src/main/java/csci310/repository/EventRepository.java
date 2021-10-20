package csci310.repository;

import csci310.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByEventName(String eventName);

    List<Event> findAllByEventDateBetween(Date startDate, Date endDate);

    Event findTopByOrderByIdDesc();
}
