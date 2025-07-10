package io.meshspy.meshspy_server.node;

import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public Node addClient(@RequestBody Node client) {
        return nodeService.addClient(client);
    }

    @PostMapping("/reset")
    public void reset() {
        nodeService.resetClients();
    }
}
