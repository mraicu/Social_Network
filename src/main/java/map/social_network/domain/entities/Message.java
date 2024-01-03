package map.social_network.domain.entities;

import map.social_network.domain.Entity;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Message extends Entity<Long> {

    private String text;
    private User from;
    private List<User> to = new ArrayList<>();
    private Timestamp date;
    private Long replyId;

    private static long count = 4;

    private Long id;

    public Message(String text, User from, User to, Long replyId, Timestamp time) {
        this.id = Long.valueOf(count);
        this.text = text;
        this.from = from;
        this.date = time;
        this.replyId = replyId;
        this.addTo(to);
        setCount(id);
    }

    public String getText() {
        return text;
    }

    public User getFrom() {
        return from;
    }

    public String getFromName() {
        return from.getFirstName() + "-" + from.getLastName();
    }

    public String getToName() {
        String names = "";
        for (User t : to) {
            names = names + (t.getFirstName() + "-" + t.getLastName()) + " ";
        }

        return names;
    }

    public List<User> getTo() {
        return to;
    }

    public Timestamp getDate() {
        return date;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public void addTo(User to) {
        this.to.add(to);
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public void setCount(long id) {
        if (id >= count) count = id + 1;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                "text='" + text + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", date=" + date +
                ", replyId=" + replyId +
                '}';
    }
}
