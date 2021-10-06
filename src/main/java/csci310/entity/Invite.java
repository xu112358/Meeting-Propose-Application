package csci310.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "invite")
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "preference")
    public int preference;
    @Column(name = "availability")
    public int availability;
    @Column(name = "status")
    public String status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "receiver_id",
            referencedColumnName = "id"
    )
    User receiver;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "sender_id",
            referencedColumnName = "id"
    )
    private User sender;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "event_id",
            referencedColumnName = "id"
    )
    private Event event;

    public Invite() {
    }

    public Invite(int id, int preference, int availability, String status, User receiver, User sender, Event event) {
        this.id = id;
        this.preference = preference;
        this.availability = availability;
        this.status = status;
        this.receiver = receiver;
        this.sender = sender;
        this.event = event;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPreference() {
        return preference;
    }

    public void setPreference(int preference) {
        this.preference = preference;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

}
