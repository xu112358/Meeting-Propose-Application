package csci310.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Invite")
@Data
@NoArgsConstructor
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "preference")
    private int preference; //"1-5"
    @Column(name = "availability")
    private int availability;//"0","1","maybe?"
    @Column(name = "status")
    private String status; // "confirm", "not confirm"
    @Column(name = "create_date", columnDefinition = "DATE")
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "invite_event",joinColumns = @JoinColumn(name = "invite_id"),inverseJoinColumns = @JoinColumn(name="event_id"))
    private List<Event> invite_events_list;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "invite_receivers",joinColumns = @JoinColumn(name = "invite_id"),inverseJoinColumns = @JoinColumn(name="user_id"))
    private List<User> receivers;


}
