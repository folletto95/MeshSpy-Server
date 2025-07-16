package io.meshspy.meshspy_server.version;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;

@Service
public class VersionService {
    private static final Logger log = LoggerFactory.getLogger(VersionService.class);
    private String version = "0.0";

    @PostConstruct
    private void init() {
        Properties props = new Properties();
        try {
            props.load(new ClassPathResource("git.properties").getInputStream());
            String count = props.getProperty("git.total.commit.count");
            if (count != null) {
                version = "0." + count;
            }
        } catch (IOException e) {
            log.warn("Could not load git.properties", e);
        }
        log.info("Starting MeshSpy Admin version {}", version);
    }

    public String getVersion() {
        return version;
    }
}
