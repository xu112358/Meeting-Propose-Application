package csci310.model;

import csci310.entity.Event;

import java.io.Serializable;
import java.util.List;

public class InviteModel implements Serializable {
    private String sender;
    private List<Event> events;
    private List<String> receivers;
    private String invite_name;

    public InviteModel() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> evnets) {
        this.events = evnets;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<String> receiver) {
        this.receivers = receiver;
    }

    public String getInvite_name() {
        return invite_name;
    }

    public void setInvite_name(String invite_name) {
        this.invite_name = invite_name;
    }
}
