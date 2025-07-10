package io.meshspy.meshspy_server.manage;

import io.meshspy.meshspy_server.node.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nodes/{id}")
public class NodeManagementController {
    private static final Logger log = LoggerFactory.getLogger(NodeManagementController.class);
    private final NodeService nodeService;

    public NodeManagementController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping("/backups")
    public List<String> listBackups(@PathVariable String id) {
        log.debug("API GET /nodes/{}/backups", id);
        return nodeService.listBackups(id);
    }

    @PostMapping("/backup")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBackup(@PathVariable String id, @RequestBody ConfigBackup backup) {
        log.debug("API POST /nodes/{}/backup", id);
        nodeService.storeBackup(id, backup.getData());
    }

    @PostMapping("/restore")
    public void restore(@PathVariable String id, @RequestBody ConfigBackup backup) {
        log.debug("API POST /nodes/{}/restore", id);
        nodeService.storeBackup(id, backup.getData());
    }

    @PostMapping("/firmware/update")
    public void updateFirmware(@PathVariable String id, @RequestBody FirmwareUpdateRequest req) {
        log.debug("API POST /nodes/{}/firmware/update", id);
        nodeService.updateFirmware(id, req.getVersion());
    }

    @GetMapping("/firmware")
    public String firmware(@PathVariable String id) {
        return nodeService.getFirmware(id).orElse("unknown");
    }
}
