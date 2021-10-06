package csci310.repository;

import csci310.entity.Event;
import csci310.entity.Invite;
import csci310.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class InviteRepositoryTest {
    @Autowired
    private InviteRepository inviteRepository;
    @Test
    @Transactional
    void sendInvite() {
        User user1= new User();
        user1.setFirstName("Kaituo");
        user1.setUsername("kaituo123");
        user1.setLastName("Xu");
        user1.setHashPassword("jdwuua276");

        User user2= new User();
        user1.setFirstName("Minyi");
        user1.setUsername("minyiche");
        user1.setLastName("Chen");
        user1.setHashPassword("minyi276");

        User user3= new User();
        user1.setFirstName("Minyi");
        user1.setUsername("minyiche");
        user1.setLastName("Chen");
        user1.setHashPassword("minyi276");

        Event event = new Event();
        event.setEventName("race");
        event.setGenre("sports");

        List<User> users = new ArrayList<>();
        users.add(user2);
        users.add(user3);
        for(int i = 0; i < users.size(); i ++){
            Invite invite = new Invite();
            invite.setAvailability(0);
            invite.setPreference(0);
            invite.setStatus("undecided");
            invite.setSender(user1);
            invite.setEvent(event);
            invite.setReceiver(users.get(i));
            System.out.println(invite);
            inviteRepository.save(invite);
        }

        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user3);
    }

}
