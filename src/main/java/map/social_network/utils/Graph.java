package map.social_network.utils;


import map.social_network.domain.entities.User;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    private Map<Long, List<Long>> edges;
    private Map<Long, Boolean> checked;

    /**
     * Constructor
     * It must call initializeEdges of all users
     *
     * @param users all users from repository
     */
    public Graph(Iterable<User> users) {
        edges = new HashMap<>();
        initializeEdges(users);
        checked = new HashMap<>();
    }

    public void initializeEdges(Iterable<User> users) {
        users.forEach(user -> edges.put(user.getId(), new LinkedList<Long>()));
    }

    private List<List<Long>> connectedComponents() {
        List<List<Long>> components = new ArrayList<>();

        edges.keySet().forEach(userID -> checked.put(userID, false));
        edges.keySet().forEach(userID -> {
            if (!checked.get(userID)) {
                List<Long> component = DFS(userID);
                components.add(component);
            }
        });


        return components;
    }

    private List<Long> DFS(Long root) {
        Stack<Long> stack = new Stack<>();
        List<Long> component = new ArrayList<>();

        stack.add(root);
        while (!stack.empty()) {
            Long currentID = stack.pop();
            component.add(currentID);
            checked.put(currentID, true);
            for (Long friendID : edges.get(currentID)) {
                if (!checked.get(friendID)) {
                    stack.add(friendID);
                }
            }
        }

        return component;
    }

    public void addEdge(Long firstNode, Long secondNode) {
        edges.get(firstNode).add(secondNode);
        edges.get(secondNode).add(firstNode);
    }

    public long countConnectedComponents() {
        return connectedComponents().size();
    }

    public List<Long> getLargestComponent() {
        List<List<Long>> components = connectedComponents();
        components.sort((o1, o2) -> o2.size() - o1.size());
        return connectedComponents().get(0).stream().distinct().collect(Collectors.toList());
    }
}
