package io.meshspy.meshspy_server.request;

import io.meshspy.meshspy_server.node.NodeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/node-requests")
public class NodeRequestController {
    private final NodeService nodeService;

    public NodeRequestController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping
    public List<NodeRegistrationRequest> listRequests() {
        return nodeService.listRequests();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NodeRegistrationRequest addRequest(@RequestBody NodeRegistrationRequest request) {
        return nodeService.addRequest(request);
    }

    @PostMapping("/{id}/approve")
    public void approve(@PathVariable String id) {
        nodeService.approveRequest(id);
    }

    @DeleteMapping("/{id}")
    public void reject(@PathVariable String id) {
        nodeService.rejectRequest(id);
    }
}
