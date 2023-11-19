package map.social_network.utils.events;

import map.social_network.domain.entities.Friendship;
import map.social_network.domain.entities.User;
import map.social_network.observer.Event;
import map.social_network.observer.NotifyStatus;

public class FriendshipChangeEvent implements Event {
    private NotifyStatus type;
    private Friendship friendship, oldFriendship;

    public FriendshipChangeEvent(NotifyStatus type, Friendship friendship) {
        this.type = type;
        this.friendship = friendship;
    }
    public FriendshipChangeEvent(NotifyStatus type, Friendship friendship, Friendship oldFriendship) {
        this.type = type;
        this.friendship = friendship;
        this.oldFriendship = oldFriendship;
    }

    public NotifyStatus getType() {
        return type;
    }

    public Friendship getFriendship() {
        return friendship;
    }

    public Friendship getOldData() {
        return oldFriendship;
    }
}
