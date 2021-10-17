package csci310.repository;

import csci310.entity.Invite;
import csci310.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.*;
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
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
        System.out.println(user);
    }
}