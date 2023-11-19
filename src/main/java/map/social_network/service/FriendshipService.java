package map.social_network.service;

import map.social_network.domain.Tuple;
import map.social_network.domain.entities.Friendship;
import map.social_network.domain.entities.User;
import map.social_network.observer.NotifyStatus;
import map.social_network.observer.Observable;
import map.social_network.observer.Observer;
import map.social_network.repository.Repository;
import map.social_network.utils.Graph;
import map.social_network.utils.events.FriendshipChangeEvent;
import map.social_network.utils.events.UserChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendshipService implements Observable<FriendshipChangeEvent> {

    private List<Observer<FriendshipChangeEvent>> friendshipObserver = new ArrayList<>();
    private final Repository<Tuple<Long, Long>, Friendship> friendshipRepository;
    private final Repository<Long, User> userRepository;


    public FriendshipService(Repository<Tuple<Long, Long>, Friendship> friendshipRepository,
                             Repository<Long, User> userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }


    public Optional<Friendship> saveFriendship(Long id1, Long id2) {
        Friendship friendship = new Friendship(id1, id2);
        Optional<Friendship> res = friendshipRepository.save(friendship);
        if (!res.isEmpty()) {
            notifyObservers(new FriendshipChangeEvent(NotifyStatus.ADD_FRIENDSHIP, friendship));
        }
        return res;
    }

    public Iterable<Friendship> findAllService() {
        return this.friendshipRepository.findAll();
    }

    public void setNames(Long id1, Long id2) {
        Friendship friendship = friendshipRepository.findOne(new Tuple<>(id1, id2)).get();
        User user1 = userRepository.findOne(id1).get();
        User user2 = userRepository.findOne(id2).get();
        friendship.setLeftName(user1.getFirstName() + user1.getLastName());
        friendship.setLeftName(user2.getFirstName() + user2.getLastName());
    }

    private Graph defineGraph() {
        Iterable<Friendship> friendships = friendshipRepository.findAll();
        Iterable<User> users = userRepository.findAll();
        Graph graph = new Graph(users);

        friendships.forEach(friendship -> graph.addEdge(friendship.getId().getLeft(), friendship.getId().getRight()));
        return graph;
    }

    public Long getNumberOfComunities() {
        Graph graph = defineGraph();
        return graph.countConnectedComponents();
    }

    public List<Long> getLargestCommunity() {
        Graph graph = defineGraph();
        return graph.getLargestComponent();
    }

    @Override
    public void addObserver(Observer<FriendshipChangeEvent> e) {
        friendshipObserver.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendshipChangeEvent> e) {

    }

    @Override
    public void notifyObservers(FriendshipChangeEvent t) {
        friendshipObserver.stream().forEach(o -> o.update(t));
    }
}