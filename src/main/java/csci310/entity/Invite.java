package csci310.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "invite")
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "sender_id")
    private Long senderId;
    @Column(name = "receiver_id")
    private Long receiverId;
    @Column(name = "event_id")
    private Long eventId;
    @Column(name = "preference")
    private int preference;
    @Column(name = "availability")
    private int availability;
    @Column(name = "status")
    private String status;
    @Column(name = "create_time")
    private Date createTime;

    public Invite() {
    }

    public Invite(Long id, Long senderId, Long receiverId, Long eventId, int preference, int availability, String status, Date createTime) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.eventId = eventId;
        this.preference = preference;
        this.availability = availability;
        this.status = status;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Invite{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", eventId=" + eventId +
                ", preference=" + preference +
                ", availability=" + availability +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
