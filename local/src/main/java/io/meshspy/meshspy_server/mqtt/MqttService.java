package io.meshspy.meshspy_server.mqtt;

import io.meshspy.meshspy_server.core.CommandDispatcher;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

    private final String broker = "tcp://localhost:1883";
    private final String clientId = "berry-0w";
    private final String commandTopic = "meshspy/commands/" + clientId;
    private final String logTopic = "meshspy/data/" + clientId + "/log";
    private final CommandDispatcher dispatcher;
    private MqttClient mqttClient;

    public MqttService(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void connectAndSubscribe() throws MqttException {
        mqttClient = new MqttClient(broker, clientId);
        mqttClient.connect();
        mqttClient.subscribe(commandTopic, (topic, message) -> {
            String payload = new String(message.getPayload());
            String result = dispatcher.dispatch(payload);
            mqttClient.publish(logTopic, new MqttMessage(result.getBytes()));
        });
        System.out.println("MQTT Connected and subscribed to " + commandTopic);
    }
}
