package csci310.repository;

import csci310.entity.Event;
import csci310.entity.Invite;
import csci310.entity.User;
import io.cucumber.java.bs.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private InviteRepository inviteRepository;
    @Test
    @Transactional
    public void testSaveUser(){
        User user1= new User();
        user1.setFirstName("Kaituo");
        user1.setUsername("kaituo");
        user1.setLastName("Xu");
        user1.setHashPassword("jdwuua276");
        userRepository.save(user1);

        User user= userRepository.findByUsername("kaituo123");
    }

    @Test
    @Transactional
    public void testUser(){
        List<User> users = new ArrayList<>();
        User user= new User();
        user.setFirstName("firstname");
        user.setUsername("userTest");
        user.setLastName("lastname");
        user.setHashPassword("password");
        users.add(user);
        userRepository.save(user);
        User userReturn = userRepository.findByUsername(user.getUsername());
        user.setId(userReturn.getId());

        assertEquals("lastname", user.getLastName());
        assertEquals("firstname", user.getFirstName());
        java.sql.Date date =  java.sql.Date.valueOf("2021-10-16");
        List<Event> events = new ArrayList<>();
        Event event = new Event();
        event.setEventName("event");
        event.setGenre("event");
        event.setEventDate(date);
        event.setLocation("LA");
        events.add(event);
        eventRepository.save(event);
        Event eventReturn = eventRepository.findTopByOrderByIdDesc();
        assertEquals("event", eventReturn.getEventName());
        assertEquals("event", eventReturn.getGenre());
        event.setId(eventReturn.getId());

        List<Invite> invites = new ArrayList<>();
        Invite invite = new Invite();
        invite.setInviteName("inviteName");
        invite.setCreateDate(date);
        invite.setStatus("not comfirmed");
        invites.add(invite);
        inviteRepository.save(invite);
        Invite inviteReturn = inviteRepository.findTopByOrderByIdDesc();
        assertEquals("inviteName", invite.getInviteName());
        assertEquals("not comfirmed", invite.getStatus());
        invite.setId(10000L);

        user.setUser_events_list(events);
        user.setReceive_invites_list(invites);
        user.setSend_invites_list(invites);
        user.getReceive_invites_list();

        event.setUsers_who_hold_event(users);
        event.setInvites_which_hold_event(invites);
        event.getUsers_who_hold_event();
        event.getInvites_which_hold_event();

        invite.setInvite_events_list(events);
        invite.setSender(user);
        invite.setReceivers(users);
        invite.getSender();

    }
}