package map.social_network.observer;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);
}
