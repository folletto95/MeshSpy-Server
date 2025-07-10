package io.meshspy.meshspy_server.node;

import io.meshspy.meshspy_server.request.NodeRegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class NodeService {
    private static final Logger log = LoggerFactory.getLogger(NodeService.class);
    private final Map<String, Node> nodes = new HashMap<>();
    private final Map<String, NodeRegistrationRequest> requests = new HashMap<>();

    private final ObjectMapper mapper = new ObjectMapper();
    private final Path dataDir = Paths.get(Optional.ofNullable(System.getProperty("admin.data.dir")).orElse("data"));
    private final Path nodesFile = dataDir.resolve("nodes.json");
    private final Path requestsFile = dataDir.resolve("requests.json");

    @PostConstruct
    private void load() {
        try {
            if (Files.exists(nodesFile)) {
                nodes.putAll(mapper.readValue(nodesFile.toFile(), new TypeReference<Map<String, Node>>() {}));
            }
            if (Files.exists(requestsFile)) {
                requests.putAll(mapper.readValue(requestsFile.toFile(), new TypeReference<Map<String, NodeRegistrationRequest>>() {}));
            }
        } catch (IOException e) {
            log.warn("Failed to load data", e);
        }
    }

    private void saveNodes() {
        save(nodesFile, nodes);
    }

    private void saveRequests() {
        save(requestsFile, requests);
    }

    private void save(Path file, Object data) {
        try {
            Files.createDirectories(dataDir);
            mapper.writeValue(file.toFile(), data);
        } catch (IOException e) {
            log.warn("Failed to save {}", file, e);
        }
    }

    /**
     * Check if a node is already registered/approved.
     */
    public boolean isApproved(String id) {
        return nodes.containsKey(id);
    }

    /**
     * Create or update a pending registration request for the given node.
     */
    public void addRequestFromNode(Node node) {
        NodeRegistrationRequest req = new NodeRegistrationRequest(
                node.getId(),
                node.getName(),
                node.getAddress(),
                node.getLatitude(),
                node.getLongitude());
        requests.put(node.getId(), req);
        saveRequests();
        log.debug("Stored registration request {}", node.getId());
    }

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
        saveNodes();
        return node;
    }

    public NodeRegistrationRequest addRequest(NodeRegistrationRequest request) {
        log.debug("Adding request {}", request.getId());
        requests.put(request.getId(), request);
        saveRequests();
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
            saveRequests();
        }
    }

    public void rejectRequest(String id) {
        log.debug("Rejecting request {}", id);
        if (requests.remove(id) != null) {
            saveRequests();
        }
    }
}
