package csci310.repository;

import csci310.entity.Event;
import csci310.entity.Invite;
import csci310.entity.User;
import csci310.model.InviteModel;
import io.cucumber.java.bs.A;
import org.junit.Assert;
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
        user1.setUsername("kaituo");
        user1.setHashPassword("jdwuua276");
        userRepository.save(user1);

        User user= userRepository.findByUsername("kaituo123");
    }

//    @Test
//    //@Transactional
//    //generate data for testing
//    public void generateUserTestingData(){
//        User sender = new User();
//        sender.setUsername("minyiche1");
//        sender.setHashPassword("password");
//        userRepository.save(sender);
//
//        User receiver1 = new User();
//        receiver1.setUsername("minyiche2");
//        receiver1.setHashPassword("password");
//        userRepository.save(receiver1);
//
//        User receiver2 = new User();
//        receiver2.setUsername("minyiche3");
//        receiver2.setHashPassword("password");
//        userRepository.save(receiver2);
//
//        User receiver3 = new User();
//        receiver3.setUsername("minyiche4");
//        receiver3.setHashPassword("password");
//        userRepository.save(receiver3);
//
//        //login data
//        User loginUser = new User();
//        loginUser.setUsername("root");
//        loginUser.setHashPassword("$2a$10$t5.Z7ln/4fw8H9S1AEsbPucZXcTe2h7qn1NWf1fnEc1QFEbqroIxi");
//        userRepository.save(loginUser);
//
//        //login data
//        User acceptanceTestUser = new User();
//        acceptanceTestUser.setUsername("username");
//        acceptanceTestUser.setHashPassword("$2a$10$WSteqgM/SBeSJPLLLr7f4Ozth6UlhtkpzbAZrNbijQICdaeq2EmgG");
//        userRepository.save(acceptanceTestUser);
//    }



    @Test
    @Transactional
    public void testUser(){
        List<User> users = new ArrayList<>();
        User user= new User();
        user.setUsername("userTest");
        user.setHashPassword("password");
        users.add(user);
        userRepository.save(user);
        User userReturn = userRepository.findByUsername(user.getUsername());
        user.setId(userReturn.getId());

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
        invite.getReceivers();
        invites.add(invite);
        inviteRepository.save(invite);
        Invite inviteReturn = inviteRepository.findTopByOrderByIdDesc();
        assertEquals("inviteName", invite.getInviteName());
        invite.setId(10000L);

        user.setUser_events_list(events);
        user.setReceive_invites_list(invites);
        user.setSend_invites_list(invites);
        user.getReceive_invites_list();
        user.setStartDate(date);
        user.setEndDate(date);
        user.getStartDate();
        user.getEndDate();
        user.setBlock_list(null);
        user.getBlock_list();
        user.setBlocked_by_list(null);
        user.getBlocked_by_list();
        user.getSend_invites_list();

        event.setReceiver(user);
        event.setInvite(invite);
        event.getReceiver();
        event.getInvite();

        invite.setInvite_events_list(events);
        invite.setSender(user);
        invite.setReceivers(users);
        invite.getSender();

    }

    @Test
    public void testfindByUsernameStartingWith(){
        List<User> users=userRepository.findByUsernameStartingWith("r");
        Boolean found=false;
        for(User obj:users){
            if(obj.getUsername().equalsIgnoreCase("root")){
                found=true;
                break;
            }
        }

        Assert.assertTrue(found);
    }
}