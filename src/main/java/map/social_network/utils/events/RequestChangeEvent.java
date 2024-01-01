package map.social_network.utils.events;

import map.social_network.domain.entities.Request;
import map.social_network.observer.Event;
import map.social_network.observer.NotifyStatus;

public class RequestChangeEvent implements Event {
    private NotifyStatus type;
    private Request request, oldRequest;

    public RequestChangeEvent(NotifyStatus type, Request request) {
        this.type = type;
        this.request = request;
    }
    public RequestChangeEvent(NotifyStatus type, Request request, Request oldRequest) {
        this.type = type;
        this.request = request;
        this.oldRequest = oldRequest;
    }

    public NotifyStatus getType() {
        return type;
    }

    public Request getRequest() {
        return request;
    }

    public Request getOldData() {
        return oldRequest;
    }
}
