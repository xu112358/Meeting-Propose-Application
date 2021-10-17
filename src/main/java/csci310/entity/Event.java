package csci310.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Event")
@Data
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "event_name")
    private String eventName;
    @Column(name = "genre")
    private String genre;
    @Temporal(TemporalType.DATE)
    @Column(name = "event_date", columnDefinition = "DATE")
    private Date eventDate;
    private String location;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_event",joinColumns = @JoinColumn(name = "event_id"),inverseJoinColumns = @JoinColumn(name="user_id"))
    private List<User> users_who_hold_event;

    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "invite_events_list")
    private List<Invite> invites_which_hold_event;



    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", eventName='" + eventName + '\'' +
                ", genre='" + genre + '\'' +
                ", eventDate=" + eventDate +
                '}';
    }
}
