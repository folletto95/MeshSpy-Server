# MeshSpy

## Descrizione

MeshSpy è un progetto in fase di sviluppo con l'obiettivo di realizzare un sistema avanzato per la gestione, il monitoraggio e l'interazione con reti basate su nodi Meshtastic. Il software sarà implementato completamente in Go (ultima versione disponibile), puntando su prestazioni elevate, affidabilità, facilità di manutenzione e semplicità di distribuzione, con supporto nativo per connessioni USB, Wi-Fi e Bluetooth.

## Obiettivi e funzionalità previste

* **Gestione centralizzata:** Amministrazione e monitoraggio remoto dei nodi Meshtastic. (Ubiquiti UniFi stile)
* **Interfaccia grafica moderna:** Web UI intuitiva basata su React' e API REST veloci basate su Go.
* **Integrazione MQTT:** Comunicazione real-time tra nodi e backend tramite broker MQTT.
* **Multiconnettività:** Supporto per connessioni via USB, Bluetooth e Wi-Fi x i nodi.
* **Automazione completa:** Installazione automatizzata e configurazione semplificata.

## Prerequisiti

* **Go** (ultima versione disponibile)
* **Node.js e npm** (per l'interfaccia React) ?
* **MQTT Broker** (es. Mosquitto, opzionale se si usa quello integrato)

## Guida rapida per lo sviluppo iniziale

Clona il repository e accedi alla cartella del progetto:

```bash
git clone https://github.com/folletto95/MeshSpy.git
cd MeshSpy
```

### Backend (Go)

1. Compila il backend con Go:

```bash
make build
```

2. Avvia il backend (in fase di sviluppo):

```bash
./meshspy
```

Il backend sarà disponibile di default su `http://localhost:8000` (in fase di sviluppo).

### Frontend (React)

1. Installa le dipendenze e avvia l'interfaccia web (in fase di sviluppo):

```bash
cd meshspy-ui
npm install
npm run dev
```

La UI sarà accessibile su `http://localhost:5173` (in fase di sviluppo).

## Configurazione prevista MQTT

È prevista la configurazione del broker MQTT tramite file `config.json`:

```json
{
  "mqtt_broker": "mqtt://localhost:1883",
  "mqtt_topic": "msh/#"
}
```

## Docker (previsto opzionale)

È prevista la possibilità di avviare MeshSpy con Docker tramite:

```bash
docker-compose up -d
```

### Configurazione tramite `.env`

I moduli admin e local caricano automaticamente le variabili definite in un file `.env` posizionato nella directory principale del progetto. Ad esempio è possibile impostare la cartella in cui salvare i dati del servizio admin specificando:

```properties
ADMIN_DATA_DIR=/percorso/dati
```

Le variabili d'ambiente reali hanno comunque precedenza sui valori presenti nel file `.env`.

## Contribuire

Se desideri contribuire al progetto, sentiti libero di aprire issue, proporre modifiche o miglioramenti tramite pull request.

## Licenza

MeshSpy è distribuito sotto licenza GPL-3. Vedi il file [LICENSE](LICENSE) per ulteriori dettagli.
