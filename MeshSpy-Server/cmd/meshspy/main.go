package main

import (
    "log"
    "meshspy/internal/serialconn"
    "meshspy/internal/mqttclient"
    "meshspy/internal/api"
    "meshspy/internal/updater"
)

func main() {
    log.Println("Starting MeshSpy...")

    // Aggiorna protobufs all'avvio
    updater.UpdateProtobufs()

    // Avvia MQTT
    go mqttclient.StartMQTT("tcp://localhost:1883", "meshspy/#")

    // Avvia USB serial connessione
    go serialconn.StartSerialReader("/dev/ttyUSB0", 921600)

    // Avvia server API
    api.StartHTTP(":8080")
}
