package io.meshspy.meshspy_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class AdminMeshSpyServerApplication {

        public static void main(String[] args) {
                Dotenv.configure()
                        .ignoreIfMalformed()
                        .ignoreIfMissing()
                        .systemProperties()
                        .load();
                SpringApplication.run(AdminMeshSpyServerApplication.class, args);
        }

}
