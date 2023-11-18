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
import map.social_network.repository.Repository;
import map.social_network.utils.Utils;

public class UserService {

    private Repository<Long, User> userRepository;
    private Repository<Tuple<Long, Long>, Friendship> friendshipRepository;

    public UserService(Repository<Long, User> userRepository, Repository<Tuple<Long, Long>, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public Optional<User> saveUser(String firstName, String secondName) {
        User user = new User(firstName, secondName);
        return userRepository.save(user);
    }

    public Optional<User> deleteUser(Long id) {
        return userRepository.delete(id);
    }

    public Optional<User> updateUser(User user) {
        return userRepository.update(user);
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

}