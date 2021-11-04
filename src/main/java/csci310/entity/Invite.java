package csci310.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Invite")
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "create_date", columnDefinition = "DATE")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    private Date createDate;
    @Column(name = "invite_name")
    private String inviteName;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "invites_which_hold_event")
    private List<Event> invite_events_list = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "invite_receivers",joinColumns = @JoinColumn(name = "invite_id", referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"))
    private List<User> receivers = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getInviteName() {
        return inviteName;
    }

    public void setInviteName(String inviteName) {
        this.inviteName = inviteName;
    }
    @JsonIgnore
    public User getSender() {
        return sender;
    }
    @JsonIgnore
    public void setSender(User sender) {
        this.sender = sender;
    }

    public List<Event> getInvite_events_list() {
        return invite_events_list;
    }

    public void setInvite_events_list(List<Event> invite_events_list) {
        this.invite_events_list = invite_events_list;
    }
    @JsonIgnore
    public List<User> getReceivers() {
        return receivers;
    }
    @JsonIgnore
    public void setReceivers(List<User> receivers) {
        this.receivers = receivers;
    }
}
