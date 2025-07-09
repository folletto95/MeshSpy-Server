package io.meshspy.meshspy_server.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nodes")
public class NodeController {
    private static final Logger log = LoggerFactory.getLogger(NodeController.class);
    private final NodeService nodeService;

    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping
    public List<Node> listNodes() {
        log.debug("API GET /nodes");
        return nodeService.listNodes();
    }

    @GetMapping("/{id}")
    public Node getNode(@PathVariable String id) {
        log.debug("API GET /nodes/{}", id);
        return nodeService.getNode(id).orElseThrow(() -> new NodeNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Node addNode(@RequestBody Node node) {
        log.debug("API POST /nodes {}", node.getId());
        return nodeService.addNode(node);
    }
}
