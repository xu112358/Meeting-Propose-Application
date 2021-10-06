package csci310.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "event_name", unique=true)
    private String eventName;
    @Column(name = "genre")
    private String genre;
    @Column(name = "event_date")
    private Date eventDate;

    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
    private List<Invite> invites;

    /*@JoinTable(
            name = "user_event_map",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "event_id",
                    referencedColumnName = "id"
            )
    )
    private List<User> user;*/

    public Event() {
    }

    public Event(int id, String eventName, String genre, Date eventDate, List<Invite> invites) {
        this.id = id;
        this.eventName = eventName;
        this.genre = genre;
        this.eventDate = eventDate;
        this.invites = invites;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public List<Invite> getInvites() {
        return invites;
    }

    public void setInvites(List<Invite> invites) {
        this.invites = invites;
    }


}
