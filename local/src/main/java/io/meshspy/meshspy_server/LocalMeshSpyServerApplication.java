package io.meshspy.meshspy_server;

import io.meshspy.meshspy_server.mqtt.MqttService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LocalMeshSpyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocalMeshSpyServerApplication.class, args);
    }

    @Bean
    CommandLineRunner startMqtt(MqttService mqttService) {
        return args -> mqttService.connectAndSubscribe();
    }
}
