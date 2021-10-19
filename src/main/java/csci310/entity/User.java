package csci310.entity;

import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "User")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "username", unique=true)
    private String username;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "hash_password")
    private String hashPassword;

    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "users_who_hold_event")
    private List<Event> user_events_list = new ArrayList<>();;

    @OneToMany(mappedBy = "sender", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Invite> send_invites_list = new ArrayList<>();;

    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "receivers")
    private List<Invite> receive_invites_list = new ArrayList<>();;



}
