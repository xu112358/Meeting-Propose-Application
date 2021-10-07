package csci310.repository;

import csci310.entity.Event;
import csci310.entity.User;
import csci310.entity.UserEventMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserEventMapRepositoryTest {

    @Autowired
    UserEventMapRepository userEventMapRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EventRepository eventRepository;

    @Test
    @Transactional
    void findEventByUserId() {
//        Event event = new Event();
//        event.setEventName("race");
//        event.setGenre("sports");
//        eventRepository.save(event);

        User user1 = new User();
        user1.setFirstName("Kaituo");
        user1.setUsername("kaituo123");
        user1.setLastName("Xu");
        user1.setHashPassword("jdwuua276");
        userRepository.save(user1);

//        User user2= new User();
//        user2.setFirstName("Minyi");
//        user2.setUsername("minyiche");
//        user2.setLastName("Chen");
//        user2.setHashPassword("minyi276");
//        userRepository.save(user2);

        List<UserEventMap> userEventList = userEventMapRepository.findUserEventMapByUserId(user1.getId());
        for(int i = 0; i < userEventList.size(); i ++){
            System.out.println(userEventList.get(i));
        }

    }

    @Test
    void findUserEventMapByEventId() {

    }
}