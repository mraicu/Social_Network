package map.social_network.service;

import map.social_network.domain.entities.Message;
import map.social_network.domain.entities.User;
import map.social_network.observer.NotifyStatus;
import map.social_network.observer.Observable;
import map.social_network.observer.Observer;
import map.social_network.repository.Repository;
import map.social_network.utils.events.MessageChangeEvent;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageService implements Observable<MessageChangeEvent> {
    private List<Observer<MessageChangeEvent>> messageObservers = new ArrayList<>();
    private Repository<Long, User> userRepository;
    private Repository<Long, Message> messageRepository;

    public MessageService(Repository<Long, User> userRepository, Repository<Long, Message> messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public List<Message> findByUser(Long from, Long to) {
        Predicate<Message> predicateUser = m -> (m.getFrom().getId() == from && m.getTo().get(0).getId()==to)||(m.getFrom().getId() == to && m.getTo().get(0).getId()==from);

        return StreamSupport.stream(messageRepository.findAll().spliterator(), false)
                .filter(predicateUser)
                .collect(Collectors.toList());
    }

    public Optional<Message> saveMessage(String text, User from, User to, Long replyId, Timestamp date) {
        Message message = new Message(text, from, to, replyId, date);

        Optional<Message> res = messageRepository.save(message);
        if (!res.isEmpty()) {
            notifyObservers(new MessageChangeEvent(NotifyStatus.ADD_MESSAGE, message));
        }
        return res;
    }
    public Optional<Message> findOneService(Long id) {
        return this.messageRepository.findOne(id);
    }

    @Override
    public void addObserver(Observer<MessageChangeEvent> e) {
        messageObservers.add(e);
    }

    @Override
    public void removeObserver(Observer<MessageChangeEvent> e) {

    }

    @Override
    public void notifyObservers(MessageChangeEvent t) {
        messageObservers.stream().forEach(o->o.update(t));
    }
}
