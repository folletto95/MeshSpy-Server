package io.meshspy.meshspy_server.request;

import io.meshspy.meshspy_server.node.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client-requests")
public class ClientRequestController {
    private static final Logger log = LoggerFactory.getLogger(ClientRequestController.class);
    private final NodeService nodeService;

    public ClientRequestController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping
    public List<NodeRegistrationRequest> listRequests() {
        log.debug("API GET /client-requests");
        return nodeService.listClientRequests();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NodeRegistrationRequest addRequest(@RequestBody NodeRegistrationRequest request) {
        log.debug("API POST /client-requests {}", request.getId());
        nodeService.addClientRequestFromNode(new io.meshspy.meshspy_server.node.Node(request.getId(), request.getName(), request.getAddress(), request.getLatitude(), request.getLongitude(), true));
        return request;
    }

    @PostMapping("/{id}/approve")
    public void approve(@PathVariable String id) {
        log.debug("API POST /client-requests/{}/approve", id);
        nodeService.approveClientRequest(id);
    }

    @DeleteMapping("/{id}")
    public void reject(@PathVariable String id) {
        log.debug("API DELETE /client-requests/{}", id);
        nodeService.rejectClientRequest(id);
    }
}
