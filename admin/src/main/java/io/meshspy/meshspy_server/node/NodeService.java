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
    // local clients physically connected via USB and approved
    private final Map<String, Node> clients = new HashMap<>();
    // pending node registration requests
    private final Map<String, NodeRegistrationRequest> requests = new HashMap<>();
    // pending client registration requests
    private final Map<String, NodeRegistrationRequest> clientRequests = new HashMap<>();
    // configuration backups indexed by node id
    private final Map<String, List<String>> backups = new HashMap<>();
    // stored firmware version or image identifier per node
    private final Map<String, String> firmware = new HashMap<>();
    // generic data sent by clients indexed by client id
    private final Map<String, List<String>> clientData = new HashMap<>();

    private final ObjectMapper mapper = new ObjectMapper();
    private final Path dataDir = Paths.get(Optional.ofNullable(System.getProperty("admin.data.dir")).orElse("data"));
    private final Path nodesFile = dataDir.resolve("nodes.json");
    private final Path clientsFile = dataDir.resolve("clients.json");
    private final Path requestsFile = dataDir.resolve("requests.json");
    private final Path clientRequestsFile = dataDir.resolve("client_requests.json");
    private final Path backupsFile = dataDir.resolve("backups.json");
    private final Path firmwareFile = dataDir.resolve("firmware.json");
    private final Path clientDataFile = dataDir.resolve("client_data.json");

    @PostConstruct
    private void load() {
        try {
            if (Files.exists(nodesFile)) {
                nodes.putAll(mapper.readValue(nodesFile.toFile(), new TypeReference<Map<String, Node>>() {}));
            }
            if (Files.exists(clientsFile)) {
                clients.putAll(mapper.readValue(clientsFile.toFile(), new TypeReference<Map<String, Node>>() {}));
            }
            if (Files.exists(requestsFile)) {
                requests.putAll(mapper.readValue(requestsFile.toFile(), new TypeReference<Map<String, NodeRegistrationRequest>>() {}));
            }
            if (Files.exists(clientRequestsFile)) {
                clientRequests.putAll(mapper.readValue(clientRequestsFile.toFile(), new TypeReference<Map<String, NodeRegistrationRequest>>() {}));
            }
            if (Files.exists(backupsFile)) {
                backups.putAll(mapper.readValue(backupsFile.toFile(), new TypeReference<Map<String, List<String>>>() {}));
            }
            if (Files.exists(firmwareFile)) {
                firmware.putAll(mapper.readValue(firmwareFile.toFile(), new TypeReference<Map<String, String>>() {}));
            }
            if (Files.exists(clientDataFile)) {
                clientData.putAll(mapper.readValue(clientDataFile.toFile(), new TypeReference<Map<String, List<String>>>() {}));
            }
        } catch (IOException e) {
            log.warn("Failed to load data", e);
        }
    }

    private void saveNodes() {
        save(nodesFile, nodes);
    }

    private void saveClients() {
        save(clientsFile, clients);
    }

    private void saveRequests() {
        save(requestsFile, requests);
    }

    private void saveClientRequests() {
        save(clientRequestsFile, clientRequests);
    }

    private void saveBackups() {
        save(backupsFile, backups);
    }

    private void saveFirmware() {
        save(firmwareFile, firmware);
    }

    private void saveClientData() {
        save(clientDataFile, clientData);
    }

    public void reset() {
        nodes.clear();
        clients.clear();
        clientRequests.clear();
        clientData.clear();
        backups.clear();
        firmware.clear();
        try {
            Files.deleteIfExists(nodesFile);
            Files.deleteIfExists(clientsFile);
            Files.deleteIfExists(clientRequestsFile);
            Files.deleteIfExists(clientDataFile);
            Files.deleteIfExists(backupsFile);
            Files.deleteIfExists(firmwareFile);
        } catch (IOException e) {
            log.warn("Failed to delete nodes file", e);
        }
    }

    public void resetClients() {
        clients.clear();
        clientRequests.clear();
        clientData.clear();
        try {
            Files.deleteIfExists(clientsFile);
            Files.deleteIfExists(clientRequestsFile);
            Files.deleteIfExists(clientDataFile);
        } catch (IOException e) {
            log.warn("Failed to delete clients file", e);
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
     * Check if a local client is already approved.
     */
    public boolean isClientApproved(String id) {
        return clients.containsKey(id);
    }

    /**
     * Create or update a pending registration request for the given node.
     */
    public void addRequestFromNode(Node node) {
        if (node.isClient()) {
            addClientRequestFromNode(node);
            return;
        }
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

    public void addClientRequestFromNode(Node node) {
        NodeRegistrationRequest req = new NodeRegistrationRequest(
                node.getId(), node.getName(), node.getAddress(),
                node.getLatitude(), node.getLongitude());
        clientRequests.put(node.getId(), req);
        saveClientRequests();
        log.debug("Stored client registration request {}", node.getId());
    }

    public NodeRegistrationRequest addClientRequest(NodeRegistrationRequest request) {
        clientRequests.put(request.getId(), request);
        saveClientRequests();
        log.debug("Adding client request {}", request.getId());
        return request;
    }
    public List<Node> listNodes() {
        log.debug("Listing {} nodes", nodes.size());
        return new ArrayList<>(nodes.values());
    }

    public List<Node> listClients() {
        log.debug("Listing {} clients", clients.size());
        return new ArrayList<>(clients.values());
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

    public Node addClient(Node node) {
        log.debug("Adding client {}", node.getId());
        clients.put(node.getId(), node);
        saveClients();
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

    public List<NodeRegistrationRequest> listClientRequests() {
        log.debug("Listing {} client requests", clientRequests.size());
        return new ArrayList<>(clientRequests.values());
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

    public void approveClientRequest(String id) {
        NodeRegistrationRequest request = clientRequests.remove(id);
        if (request != null) {
            log.debug("Approving client request {}", id);
            String name = request.getName();
            if (name == null || name.isBlank()) {
                name = Optional.ofNullable(request.getLongName())
                        .orElse(request.getShortName());
            }
            addClient(new Node(request.getId(), name, request.getAddress(),
                    request.getLatitude(), request.getLongitude(), true));
            saveClientRequests();
        }
    }

    public void rejectRequest(String id) {
        log.debug("Rejecting request {}", id);
        if (requests.remove(id) != null) {
            saveRequests();
        }
    }

    public void rejectClientRequest(String id) {
        log.debug("Rejecting client request {}", id);
        if (clientRequests.remove(id) != null) {
            saveClientRequests();
        }
    }

    // ----- client data operations -----
    public void storeClientData(String id, String data) {
        clientData.computeIfAbsent(id, k -> new ArrayList<>()).add(data);
        saveClientData();
        log.debug("Stored data for client {}", id);
    }

    public List<String> listClientData(String id) {
        return clientData.getOrDefault(id, Collections.emptyList());
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
