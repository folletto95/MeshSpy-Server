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
    // configuration backups indexed by node id
    private final Map<String, List<String>> backups = new HashMap<>();
    // stored firmware version or image identifier per node
    private final Map<String, String> firmware = new HashMap<>();

    private final ObjectMapper mapper = new ObjectMapper();
    private final Path dataDir = Paths.get(Optional.ofNullable(System.getProperty("admin.data.dir")).orElse("data"));
    private final Path nodesFile = dataDir.resolve("nodes.json");
    private final Path requestsFile = dataDir.resolve("requests.json");
    private final Path backupsFile = dataDir.resolve("backups.json");
    private final Path firmwareFile = dataDir.resolve("firmware.json");

    @PostConstruct
    private void load() {
        try {
            if (Files.exists(nodesFile)) {
                nodes.putAll(mapper.readValue(nodesFile.toFile(), new TypeReference<Map<String, Node>>() {}));
            }
            if (Files.exists(requestsFile)) {
                requests.putAll(mapper.readValue(requestsFile.toFile(), new TypeReference<Map<String, NodeRegistrationRequest>>() {}));
            }
            if (Files.exists(backupsFile)) {
                backups.putAll(mapper.readValue(backupsFile.toFile(), new TypeReference<Map<String, List<String>>>() {}));
            }
            if (Files.exists(firmwareFile)) {
                firmware.putAll(mapper.readValue(firmwareFile.toFile(), new TypeReference<Map<String, String>>() {}));
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

    private void saveBackups() {
        save(backupsFile, backups);
    }

    private void saveFirmware() {
        save(firmwareFile, firmware);
    }

    public void reset() {
        nodes.clear();
        backups.clear();
        firmware.clear();
        try {
            Files.deleteIfExists(nodesFile);
            Files.deleteIfExists(backupsFile);
            Files.deleteIfExists(firmwareFile);
        } catch (IOException e) {
            log.warn("Failed to delete nodes file", e);
        }
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
            String name = request.getName();
            if (name == null || name.isBlank()) {
                name = Optional.ofNullable(request.getLongName())
                        .orElse(request.getShortName());
            }
            addNode(new Node(request.getId(), name, request.getAddress(),
                    request.getLatitude(), request.getLongitude()));
            saveRequests();
        }
    }

    public void rejectRequest(String id) {
        log.debug("Rejecting request {}", id);
        if (requests.remove(id) != null) {
            saveRequests();
        }
    }

    // ----- configuration backup operations -----
    public void storeBackup(String id, String data) {
        backups.computeIfAbsent(id, k -> new ArrayList<>()).add(data);
        saveBackups();
        log.debug("Stored configuration backup for {}", id);
    }

    public List<String> listBackups(String id) {
        return backups.getOrDefault(id, Collections.emptyList());
    }

    public Optional<String> latestBackup(String id) {
        List<String> b = backups.get(id);
        if (b == null || b.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(b.get(b.size() - 1));
    }

    // ----- firmware operations -----
    public void updateFirmware(String id, String version) {
        firmware.put(id, version);
        saveFirmware();
        log.debug("Updated firmware for {} to {}", id, version);
    }

    public Optional<String> getFirmware(String id) {
        return Optional.ofNullable(firmware.get(id));
    }
}
