package io.meshspy.meshspy_server.node;

import io.meshspy.meshspy_server.request.NodeRegistrationRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NodeService {
    private final Map<String, Node> nodes = new HashMap<>();
    private final Map<String, NodeRegistrationRequest> requests = new HashMap<>();

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

    public NodeRegistrationRequest addRequest(NodeRegistrationRequest request) {
        requests.put(request.getId(), request);
        return request;
    }

    public List<NodeRegistrationRequest> listRequests() {
        return new ArrayList<>(requests.values());
    }

    public void approveRequest(String id) {
        NodeRegistrationRequest request = requests.remove(id);
        if (request != null) {
            addNode(new Node(request.getId(), request.getName(), request.getAddress(), request.getLatitude(), request.getLongitude()));
        }
    }

    public void rejectRequest(String id) {
        requests.remove(id);
    }
}
