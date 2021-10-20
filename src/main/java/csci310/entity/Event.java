package csci310.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Data
@NoArgsConstructor
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    private Date eventDate;
    @Column(name = "location")
    private String location;
    @Column(name = "preference")
    private int preference; //"1-5"
    @Column(name = "availability")
    private int availability;//"0","1","maybe?"



    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_event",joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"))
    private List<User> users_who_hold_event = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "invite_events_list")
    private List<Invite> invites_which_hold_event = new ArrayList<>();;



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
