package map.social_network.domain.entities;

import map.social_network.domain.Entity;
import map.social_network.domain.StatusRequest;

public class Request extends Entity<Long> {

    Long id;
    User from, to;
    StatusRequest status;

    private static long count = 1;

    public Request(User from, User to, StatusRequest status) {
        this.id = Long.valueOf(count);
        this.from = from;
        this.to = to;
        this.status = status;
        setCount(id);
    }

    public Long getId() {
        return id;
    }

    public User getFrom() {
        return from;
    }

    public User getTo() {
        return to;
    }

    public String getFromName() {
        return from.getFirstName() + "-" + from.getLastName();
    }

    public StatusRequest getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public void setStatus(StatusRequest status) {
        this.status = status;
    }

    public void setCount(long id) {
        if (id >= count) count = id + 1;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", status='" + status + '\'' +
                '}';
    }
}

