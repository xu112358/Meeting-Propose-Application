package csci310.repository;

import csci310.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsername(String username);

    @Query("select u from User u where u.firstName = ?1")
    User getUserByFirstName(String firstName);


}
