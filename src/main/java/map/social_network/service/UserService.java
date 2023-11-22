package map.social_network.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import map.social_network.domain.Tuple;
import map.social_network.domain.entities.Friendship;
import map.social_network.domain.entities.User;
import map.social_network.observer.NotifyStatus;
import map.social_network.observer.Observable;
import map.social_network.observer.Observer;
import map.social_network.repository.Repository;
import map.social_network.utils.Utils;
import map.social_network.utils.events.UserChangeEvent;

public class UserService implements Observable<UserChangeEvent> {
    private List<Observer<UserChangeEvent>> userObservers = new ArrayList<>();
    private Repository<Long, User> userRepository;
    private Repository<Tuple<Long, Long>, Friendship> friendshipRepository;

    public UserService(Repository<Long, User> userRepository, Repository<Tuple<Long, Long>, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public Optional<User> saveUser(String firstName, String secondName) {
        User user = new User(firstName, secondName);
        Optional<User> res = userRepository.save(user);
        if (!res.isEmpty()) {
            notifyObservers(new UserChangeEvent(NotifyStatus.ADD_USER, user));
        }

        return res;
    }

    public Optional<User> deleteUser(Long id) {
        Optional<User> user = userRepository.findOne(id);
        Optional<User> res = userRepository.delete(id);

        if (res.isEmpty()) {
            notifyObservers(new UserChangeEvent(NotifyStatus.DELETE_USER, user.get()));
        }
        return res;
    }

    public Optional<User> updateUser(User user) {
        Optional<User> res = userRepository.update(user);
        User newUser = userRepository.findOne(user.getId()).get();
        if (res.isEmpty()) {
            notifyObservers(new UserChangeEvent(NotifyStatus.UPDATE_USER, newUser));
        }
        return res;
    }

    public Iterable<User> findAllService() {
        return this.userRepository.findAll();
    }

    public Optional<User> findOneService(Long id) {
        return this.userRepository.findOne(id);
    }

    public boolean addFriend(User friend, User newFriend) {
        Friendship friendship = new Friendship(friend.getId(), newFriend.getId());
        friendshipRepository.save(friendship);
        return friend.addFriend(newFriend);
    }

    public User deleteFriend(User user, User friend) {
        Tuple<Long, Long> id = new Tuple<>(user.getId(), friend.getId());
        friendshipRepository.delete(id);
        return user.deleteFriend(friend);
    }

    public List<User> NFriends(int n) {
        Predicate<User> predicate = user -> user.getFriends().size() >= n;

        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .filter(predicate)
                .collect(Collectors.toList());

    }

    public List<Friendship> filterFrendship(Long id, String month) {

        Utils utils = new Utils();
        LocalDateTime date = utils.stringToLocalDateTime(month);

        Predicate<Friendship> friendshipPredicate = friendship -> friendship.getId().getRight() == id || friendship.getId().getLeft() == id;
        Predicate<Friendship> monthPredicate = friendship -> friendship.getDate().getMonth() == date.getMonth();

        return StreamSupport.stream(friendshipRepository.findAll().spliterator(), false)
                .filter(friendshipPredicate)
                .filter(monthPredicate)
                .collect(Collectors.toList());

    }

    public List<User> filterFrendshipByString(String string) {

        Predicate<User> namePredicate = user -> user.getFirstName().contains(string) || user.getLastName().contains(string);

        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .filter(namePredicate)
                .collect(Collectors.toList());
    }


    public List<String> getAllUserFriendshipFromDate(Long id, String month) {
        List<Friendship> filtered = filterFrendship(id, month);

        List<String> names = new ArrayList<>();
        for (Friendship f : filtered) {
            String nameAndDate = "";
            if (f.getId().getLeft() == id) {
                User rightUser = userRepository.findOne(f.getId().getRight()).get();
                nameAndDate = nameAndDate + "|" + rightUser.getFirstName() + "|" + rightUser.getLastName();
            } else {
                User leftUser = userRepository.findOne(f.getId().getLeft()).get();
                nameAndDate = nameAndDate + "|" + leftUser.getFirstName() + "|" + leftUser.getLastName();
            }

            nameAndDate = nameAndDate + "|" + f.getDate().toString();
            names.add(nameAndDate);
        }
        return names;
    }

    @Override
    public void addObserver(Observer<UserChangeEvent> e) {
        userObservers.add(e);
    }

    @Override
    public void removeObserver(Observer<UserChangeEvent> e) {
        //
    }

    @Override
    public void notifyObservers(UserChangeEvent t) {
        userObservers.stream().forEach(o -> o.update(t));
    }
}