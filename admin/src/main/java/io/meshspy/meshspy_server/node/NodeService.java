package io.meshspy.meshspy_server.node;

import io.meshspy.meshspy_server.request.NodeRegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NodeService {
    private static final Logger log = LoggerFactory.getLogger(NodeService.class);
    private final Map<String, Node> nodes = new HashMap<>();
    private final Map<String, NodeRegistrationRequest> requests = new HashMap<>();

    public List<Node> listNodes() {
        log.debug("Listing {} nodes", nodes.size());
        return new ArrayList<>(nodes.values());
    }

    public Optional<Node> getNode(String id) {
        log.debug("Fetching node {}", id);
        return Optional.ofNullable(nodes.get(id));
    }

    public Node addNode(Node node) {
        log.debug("Adding node {}", node.getId());
        nodes.put(node.getId(), node);
        return node;
    }

    public NodeRegistrationRequest addRequest(NodeRegistrationRequest request) {
        log.debug("Adding request {}", request.getId());
        requests.put(request.getId(), request);
        return request;
    }

    public List<NodeRegistrationRequest> listRequests() {
        log.debug("Listing {} requests", requests.size());
        return new ArrayList<>(requests.values());
    }

    public void approveRequest(String id) {
        NodeRegistrationRequest request = requests.remove(id);
        if (request != null) {
            log.debug("Approving request {}", id);
            addNode(new Node(request.getId(), request.getName(), request.getAddress(), request.getLatitude(), request.getLongitude()));
        }
    }

    public void rejectRequest(String id) {
        log.debug("Rejecting request {}", id);
        requests.remove(id);
    }
}
