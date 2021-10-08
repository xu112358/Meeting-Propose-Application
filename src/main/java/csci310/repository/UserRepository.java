package csci310.repository;

import csci310.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //@Query("select u from User u where u.username = ?1")
    User findByUsername(String username);

}
