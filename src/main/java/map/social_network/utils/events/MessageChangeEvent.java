package map.social_network.utils.events;
import map.social_network.domain.entities.Message;
import map.social_network.observer.Event;
import map.social_network.observer.NotifyStatus;
public class MessageChangeEvent  implements Event {

    private NotifyStatus type;
    private Message message, oldMessage;

    public MessageChangeEvent(NotifyStatus type, Message message) {
        this.type = type;
        this.message = message;
    }
    public MessageChangeEvent(NotifyStatus type, Message message, Message oldMessage) {
        this.type = type;
        this.message = message;
        this.oldMessage = oldMessage;
    }

    public NotifyStatus getType() {
        return type;
    }

    public Message getMessage() {
        return message;
    }

    public Message getOldData() {
        return oldMessage;
    }
}

