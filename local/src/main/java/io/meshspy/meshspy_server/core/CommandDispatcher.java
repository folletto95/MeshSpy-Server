package io.meshspy.meshspy_server.core;

import io.meshspy.meshspy_server.util.ShellExecutor;
import org.springframework.stereotype.Component;

@Component
public class CommandDispatcher {

    public String dispatch(String commandJson) {
        // Per ora semplice: esegui un comando fisso per test
        return ShellExecutor.execute("meshtastic --info");
    }
}
