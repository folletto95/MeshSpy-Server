package io.meshspy.meshspy_server.node;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final NodeService nodeService;

    public ClientController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping
    public List<Node> listClients() {
        return nodeService.listClients();
    }

    @PostMapping
    public ResponseEntity<?> addClient(@RequestBody Node client) {
        if (nodeService.isClientApproved(client.getId())) {
            Node saved = nodeService.addClient(client);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        }
        nodeService.addClientRequestFromNode(client);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/reset")
    public void reset() {
        nodeService.resetClients();
    }
}
