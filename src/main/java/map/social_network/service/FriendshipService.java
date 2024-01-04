package map.social_network.service;

import map.social_network.domain.Tuple;
import map.social_network.domain.entities.Friendship;
import map.social_network.domain.entities.User;
import map.social_network.observer.NotifyStatus;
import map.social_network.observer.Observable;
import map.social_network.observer.Observer;
import map.social_network.repository.Repository;
import map.social_network.repository.paging.Page;
import map.social_network.repository.paging.Pageable;
import map.social_network.repository.paging.PageableImplementation;
import map.social_network.repository.paging.PagingRepository;
import map.social_network.utils.Graph;
import map.social_network.utils.events.FriendshipChangeEvent;
import map.social_network.utils.events.UserChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipService implements Observable<FriendshipChangeEvent> {

    private List<Observer<FriendshipChangeEvent>> friendshipObserver = new ArrayList<>();
    private final Repository<Tuple<Long, Long>, Friendship> friendshipRepository;
    private final Repository<Long, User> userRepository;
    private PagingRepository<Tuple<Long, Long>, Friendship> pagingFriendshipRepository;
    private int page;
    private int size;
    private Pageable pageable;

    User user;

    public FriendshipService(Repository<Tuple<Long, Long>, Friendship> friendshipRepository,
                             Repository<Long, User> userRepository, PagingRepository<Tuple<Long, Long>, Friendship> pagingFriendshipRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
        this.pagingFriendshipRepository = pagingFriendshipRepository;
    }


    public Optional<Friendship> saveFriendship(Long id1, Long id2) {
        Friendship friendship = new Friendship(id1, id2);
        Optional<Friendship> res = friendshipRepository.save(friendship);
        if (!res.isEmpty()) {
            notifyObservers(new FriendshipChangeEvent(NotifyStatus.ADD_FRIENDSHIP, friendship));
        }
        return res;
    }

    public Optional<Friendship> deleteFriendship(Tuple<Long, Long> id) {
        Optional<Friendship> res = friendshipRepository.findOne(id);

        friendshipRepository.delete(id);

        if (!res.isEmpty()) {
            notifyObservers(new FriendshipChangeEvent(NotifyStatus.DELETE_FRIENDSHIP, res.get()));
        }
        return res;
    }

    public Iterable<Friendship> findAllService() {
        return this.friendshipRepository.findAll();
    }

    public Iterable<Friendship> findFriendships(User user) {
        Predicate<Friendship> namePredicate = left -> left.getId().getLeft() == user.getId() || left.getId().getRight() == user.getId();

        return StreamSupport.stream(friendshipRepository.findAll().spliterator(), false)
                .filter(namePredicate)
                .collect(Collectors.toList());
    }

    public Iterable<Friendship> findNotFriendships(User user) {
        Predicate<Friendship> namePredicate = left -> left.getId().getLeft() != user.getId() && left.getId().getRight() != user.getId();

        return StreamSupport.stream(friendshipRepository.findAll().spliterator(), false)
                .filter(namePredicate)
                .collect(Collectors.toList());
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

    public void setPageSize(int size) {
        this.size = size;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public void setUser(User u) {
        this.user = u;
    }

    public Set<Friendship> getPreviousFriendships() {
        if (this.page > 0) {
            this.page--;
        }
        return getFriendshipsOnThisPage(this.page);
    }

    public Set<Friendship> getNextFriendships() {
        long numFriendships = StreamSupport.stream(findAllService().spliterator(), false).count();
        if ((numFriendships / size) > page) {
            this.page++;
        }
        return getFriendshipsOnThisPage(this.page);
    }


    public Set<Friendship> getFriendshipsOnThisPage(int page) {
        this.page = page;
        Pageable pageable = new PageableImplementation(page, size);
        Page<Friendship> userPage = pagingFriendshipRepository.findAll(pageable, user);
        Set<Friendship> p = userPage.getContent().collect(Collectors.toSet());
        return p;
    }

}