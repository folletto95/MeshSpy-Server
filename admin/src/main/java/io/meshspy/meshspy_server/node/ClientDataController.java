package io.meshspy.meshspy_server.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients/{id}/data")
public class ClientDataController {
    private static final Logger log = LoggerFactory.getLogger(ClientDataController.class);
    private final NodeService nodeService;

    public ClientDataController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addData(@PathVariable String id, @RequestBody String data) {
        log.debug("API POST /clients/{}/data", id);
        nodeService.storeClientData(id, data);
    }

    @GetMapping
    public List<String> listData(@PathVariable String id) {
        log.debug("API GET /clients/{}/data", id);
        return nodeService.listClientData(id);
    }
}
