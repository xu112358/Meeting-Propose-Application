package csci310.repository;

import csci310.entity.Event;
import csci310.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EventRepositoryTest {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    UserRepository  userRepository;
    @Test
    void testSaveEvent() throws ParseException {
//        Event event = new Event();
//        Date eventDate=new Date();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        eventDate=df.parse("2021-10-16");
//        event.setEventName("basketball");
//        event.setGenre("sports");
//        event.setEventDate(eventDate);
//        event.setLocation("New York");
//        eventRepository.save(event);
//        System.out.println(event);
    }
    @Test
    void testEventDateBetween() {

        java.sql.Date sqlDate =  java.sql.Date.valueOf("2021-10-16");
        List<Event> events=eventRepository.findAllByEventDateBetween(sqlDate,sqlDate);
        System.out.println(events);
    }

    @Test
    @Transactional
    void testUserEventMapping() {

        java.sql.Date sqlDate =  java.sql.Date.valueOf("2021-10-16");
        List<Event> events=eventRepository.findAllByEventDateBetween(sqlDate,sqlDate);
        User user0=userRepository.findByUsername("root");
        System.out.println(user0.getUser_events_list());
        for(Event obj:events){
            obj.setReceiver(user0);
            user0.getUser_events_list().add(obj);
        }
        System.out.println(user0.getUser_events_list());
        eventRepository.saveAll(events);
        userRepository.save(user0);
        User user1=userRepository.findByUsername("root");
        System.out.println(user1.getUser_events_list());
    }
}