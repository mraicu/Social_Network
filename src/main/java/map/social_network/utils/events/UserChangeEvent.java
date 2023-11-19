package map.social_network.utils.events;

import map.social_network.domain.entities.User;
import map.social_network.observer.Event;
import map.social_network.observer.NotifyStatus;

public class UserChangeEvent implements Event {
    private NotifyStatus type;
    private User user, oldUser;

    public UserChangeEvent(NotifyStatus type, User user) {
        this.type = type;
        this.user = user;
    }
    public UserChangeEvent(NotifyStatus type, User user, User oldUser) {
        this.type = type;
        this.user = user;
        this.oldUser = oldUser;
    }

    public NotifyStatus getType() {
        return type;
    }

    public User getUser() {
        return user;
    }

    public User getOldData() {
        return oldUser;
    }
}