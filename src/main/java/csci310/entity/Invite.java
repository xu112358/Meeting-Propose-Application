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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="America/Los_Angeles")
    private Date createDate; // invite sort date
    @Column(name = "invite_name")
    private String inviteName;
    @Column(name = "status")
    private String status = "not finalized"; // finalized or not finalized
    //@JsonBackReference
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "final_event_id")
    private Event finalEvent;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invite")
    private List<Event> invite_events_list = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "invite_receivers",joinColumns = @JoinColumn(name = "invite_id", referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"))
    private List<User> receivers = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "invite_reject_receivers",joinColumns = @JoinColumn(name = "invite_id", referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"))
    private List<User> reject_receivers = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "invite_confirmed_receivers",joinColumns = @JoinColumn(name = "invite_id", referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"))
    private List<User> confirmed_receivers = new ArrayList<>();

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Event getFinalEvent() {
        return finalEvent;
    }

    public void setFinalEvent(Event finalEvent) {
        this.finalEvent = finalEvent;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public List<Event> getInvite_events_list() {
        return invite_events_list;
    }

    public void setInvite_events_list(List<Event> invite_events_list) {
        this.invite_events_list = invite_events_list;
    }

    public List<User> getReject_receivers() {
        return reject_receivers;
    }

    public void setReject_receivers(List<User> reject_receivers) {
        this.reject_receivers = reject_receivers;
    }

    public List<User> getConfirmed_receivers() {
        return confirmed_receivers;
    }

    public void setConfirmed_receivers(List<User> confirmed_receivers) {
        this.confirmed_receivers = confirmed_receivers;
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
