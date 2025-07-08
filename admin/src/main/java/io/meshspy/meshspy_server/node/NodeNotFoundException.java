package io.meshspy.meshspy_server.node;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NodeNotFoundException extends RuntimeException {
    public NodeNotFoundException(String id) {
        super("Node not found: " + id);
    }
}
