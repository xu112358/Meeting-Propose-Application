package csci310.repository;

import csci310.entity.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EventRepositoryTest {
    @Autowired
    EventRepository eventRepository;

    @Test
    @Transactional
    void saveEvent(){
        Event event = new Event();
        event.setEventName("race");
        event.setGenre("sports");
        eventRepository.save(event);
    }

    @Test
    void findByEventName() {
        List<Event> events = eventRepository.findByEventName("race");
        for(int i = 0; i < events.size(); i ++ ){
            assertEquals("race", events.get(i).getEventName());
        }
    }
}