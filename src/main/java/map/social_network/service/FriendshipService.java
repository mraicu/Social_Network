package map.social_network.service;

import map.social_network.domain.Tuple;
import map.social_network.domain.entities.Friendship;
import map.social_network.domain.entities.User;
import map.social_network.repository.Repository;
import map.social_network.utils.Graph;

import java.util.List;

public class FriendshipService {

    private final Repository<Tuple<Long, Long>, Friendship> friendshipRepository;
    private final Repository<Long, User> userRepository;


    public FriendshipService(Repository<Tuple<Long, Long>, Friendship> friendshipRepository,
                             Repository<Long, User> userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
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

}