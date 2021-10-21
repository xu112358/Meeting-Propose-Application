package csci310.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "User")
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

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }
    @JsonIgnore
    public List<Event> getUser_events_list() {
        return user_events_list;
    }
    @JsonIgnore
    public void setUser_events_list(List<Event> user_events_list) {
        this.user_events_list = user_events_list;
    }
    @JsonIgnore
    public List<Invite> getSend_invites_list() {
        return send_invites_list;
    }
    @JsonIgnore
    public void setSend_invites_list(List<Invite> send_invites_list) {
        this.send_invites_list = send_invites_list;
    }
    @JsonIgnore
    public List<Invite> getReceive_invites_list() {
        return receive_invites_list;
    }
    @JsonIgnore
    public void setReceive_invites_list(List<Invite> receive_invites_list) {
        this.receive_invites_list = receive_invites_list;
    }
}
