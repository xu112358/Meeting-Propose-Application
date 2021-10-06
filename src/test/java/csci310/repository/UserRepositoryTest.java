package csci310.repository;

import csci310.entity.Invite;
import csci310.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.*;
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    public void saveUser(){
        User user1= new User();
        user1.setFirstName("Kaituo");
        user1.setUsername("kaituo123");
        user1.setLastName("Xu");
        user1.setHashPassword("jdwuua276");

        List<User> users= userRepository.findByUsername("kaituo123");
        System.out.println("users="+users);
    }
}