package map.social_network.observer;

public interface Observer<E extends Event> {
    void update(E e);
}