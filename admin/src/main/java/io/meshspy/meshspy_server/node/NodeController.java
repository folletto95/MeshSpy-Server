package io.meshspy.meshspy_server.node;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nodes")
public class NodeController {
    private final NodeService nodeService;

    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping
    public List<Node> listNodes() {
        return nodeService.listNodes();
    }

    @GetMapping("/{id}")
    public Node getNode(@PathVariable String id) {
        return nodeService.getNode(id).orElseThrow(() -> new NodeNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Node addNode(@RequestBody Node node) {
        return nodeService.addNode(node);
    }
}
