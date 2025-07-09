package io.meshspy.meshspy_server.request;

import io.meshspy.meshspy_server.node.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/node-requests")
public class NodeRequestController {
    private static final Logger log = LoggerFactory.getLogger(NodeRequestController.class);
    private final NodeService nodeService;

    public NodeRequestController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping
    public List<NodeRegistrationRequest> listRequests() {
        log.debug("API GET /node-requests");
        return nodeService.listRequests();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NodeRegistrationRequest addRequest(@RequestBody NodeRegistrationRequest request) {
        log.debug("API POST /node-requests {}", request.getId());
        return nodeService.addRequest(request);
    }

    @PostMapping("/{id}/approve")
    public void approve(@PathVariable String id) {
        log.debug("API POST /node-requests/{}/approve", id);
        nodeService.approveRequest(id);
    }

    @DeleteMapping("/{id}")
    public void reject(@PathVariable String id) {
        log.debug("API DELETE /node-requests/{}", id);
        nodeService.rejectRequest(id);
    }
}
