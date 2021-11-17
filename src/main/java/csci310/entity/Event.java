package csci310.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Event")
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "event_name")
    private String eventName;
    @Column(name = "genre")
    private String genre;
    @Column(name = "event_date", columnDefinition = "DATE")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date eventDate;
    @Column(name = "location")
    private String location;
    @Column(name = "status")
    private String status = "not confirmed"; // "confirmed", "not confirmed" "accepted"
    @Column(name = "preference")
    private int preference = 1; //"1-5"
    @Column(name = "availability")
    private String availability = "no";//"no","yes","maybe?"

    @OneToOne(cascade=CascadeType.ALL, mappedBy = "finalEvent")
    private Invite finalizedInvite; // finalized invite

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "invite_id", referencedColumnName = "id")
    private Invite invite;


    public Event() {

    }

    public Event(Event event) {
        this.eventName = event.eventName;
        this.genre = event.genre;
        this.eventDate = event.eventDate;
        this.location = event.location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPreference() {
        return preference;
    }

    public void setPreference(int preference) {
        this.preference = preference;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    @JsonIgnore
    public Invite getFinalizedInvite() {
        return finalizedInvite;
    }
    @JsonIgnore
    public void setFinalizedInvite(Invite finalizedInvite) {
        this.finalizedInvite = finalizedInvite;
    }
    @JsonIgnore
    public User getReceiver() {
        return receiver;
    }
    @JsonIgnore
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
    @JsonIgnore
    public Invite getInvite() {
        return invite;
    }
    @JsonIgnore
    public void setInvite(Invite invite) {
        this.invite = invite;
    }
}
