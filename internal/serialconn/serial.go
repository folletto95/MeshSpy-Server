package serialconn

import (
    "github.com/tarm/serial"
    "log"
    "meshspy/internal/protobufs/generated/meshtastic"
    "google.golang.org/protobuf/proto"
)

func StartSerialReader(port string, baud int) {
    config := &serial.Config{Name: port, Baud: baud}
    s, err := serial.OpenPort(config)
    if err != nil {
        log.Fatal("Errore apertura porta seriale:", err)
    }

    buf := make([]byte, 4096)

    for {
        n, err := s.Read(buf)
        if err != nil {
            log.Println("Errore lettura seriale:", err)
            continue
        }
        go handleSerialData(buf[:n])
    }
}

func handleSerialData(data []byte) {
    msg := &meshtastic.FromRadio{}
    if err := proto.Unmarshal(data, msg); err != nil {
        log.Printf("⚠️ Errore decoding Protobuf: %v\n", err)
        return
    }

    log.Printf("✅ FromRadio Message ricevuto: %+v\n", msg)

    // Esempio: mostra versione firmware e hw
    if msg.MyInfo != nil {
        log.Printf("📟 Firmware: %s | HW: %v\n", msg.MyInfo.FirmwareVersion, msg.MyInfo.HwModel)
    }
}
