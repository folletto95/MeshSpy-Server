package io.meshspy.meshspy_server.node;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NodeService {
    private final Map<String, Node> nodes = new HashMap<>();

    public List<Node> listNodes() {
        return new ArrayList<>(nodes.values());
    }

    public Optional<Node> getNode(String id) {
        return Optional.ofNullable(nodes.get(id));
    }

    public Node addNode(Node node) {
        nodes.put(node.getId(), node);
        return node;
    }
}
