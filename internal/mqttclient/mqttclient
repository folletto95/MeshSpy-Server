package mqttclient

import (
    mqtt "github.com/eclipse/paho.mqtt.golang"
    "log"
)

func StartMQTT(broker, topic string) {
    opts := mqtt.NewClientOptions().AddBroker(broker)
    opts.SetClientID("meshspy-backend")
    client := mqtt.NewClient(opts)

    if token := client.Connect(); token.Wait() && token.Error() != nil {
        log.Fatal(token.Error())
    }

    if token := client.Subscribe(topic, 1, nil); token.Wait() && token.Error() != nil {
        log.Fatal(token.Error())
    }

    log.Println("MQTT client connected to", broker)
}
