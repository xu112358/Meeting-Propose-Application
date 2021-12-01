package csci310.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import csci310.annotation.AttributeEncryptor;
import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "username", unique=true)
    @Convert(converter = AttributeEncryptor.class)
    private String username;
    @Column(name = "hash_password")
    private String hashPassword;
    @Column(name = "available_start_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Convert(converter = AttributeEncryptor.class)
    private Date startDate;
    @Column(name = "available_end_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Convert(converter = AttributeEncryptor.class)
    private Date endDate;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "receiver")
    private List<Event> user_events_list = new ArrayList<>();; // all received event

    @OneToMany(mappedBy = "sender", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Invite> send_invites_list = new ArrayList<>();; //sent invite list

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "invite_receivers",inverseJoinColumns = @JoinColumn(name = "invite_id"),joinColumns = @JoinColumn(name="user_id"))
    private List<Invite> receive_invites_list = new ArrayList<>();; //receive invite list not rejected

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "invite_reject_receivers",inverseJoinColumns = @JoinColumn(name = "invite_id"), joinColumns = @JoinColumn(name="user_id"))
    private List<Invite> reject_invites_list = new ArrayList<>();;  //reject invite list

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "invite_confirmed_receivers",inverseJoinColumns = @JoinColumn(name = "invite_id"),joinColumns = @JoinColumn(name="user_id"))
    private List<Invite> confirmed_invites_list = new ArrayList<>();;  //confirmed invite list

    @ManyToMany(mappedBy = "block_list", cascade = CascadeType.ALL)
    private List<User> blocked_by_list = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_block",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name="blocked_user_id"))
    private List<User> block_list = new ArrayList<>();



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

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
    @JsonIgnore
    public List<Invite> getReject_invites_list() {
        return reject_invites_list;
    }
    @JsonIgnore
    public void setReject_invites_list(List<Invite> reject_invites_list) {
        this.reject_invites_list = reject_invites_list;
    }
    @JsonIgnore
    public List<Invite> getConfirmed_invites_list() {
        return confirmed_invites_list;
    }
    @JsonIgnore
    public void setConfirmed_invites_list(List<Invite> confirmed_invites_list) {
        this.confirmed_invites_list = confirmed_invites_list;
    }

    @JsonIgnore
    public List<User> getBlocked_by_list() {
        return blocked_by_list;
    }
    @JsonIgnore
    public void setBlocked_by_list(List<User> blocked_by_list) {
        this.blocked_by_list = blocked_by_list;
    }
    @JsonIgnore
    public List<User> getBlock_list() {
        return block_list;
    }
    @JsonIgnore
    public void setBlock_list(List<User> block_list) {
        this.block_list = block_list;
    }
}
