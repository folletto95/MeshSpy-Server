# MeshSpy

## Description

MeshSpy is a project currently under development aimed at creating an advanced system for managing, monitoring, and interacting with Meshtastic node-based networks. The software will be entirely implemented in Go (latest available version), focusing on high performance, reliability, ease of maintenance, and simplicity of deployment, with native support for USB, Wi-Fi, and Bluetooth connections.

## Goals and Planned Features

* **Centralized Management:** Administration and remote monitoring of Meshtastic nodes (similar to Ubiquiti UniFi style).
* **Modern Graphical Interface:** Intuitive web UI based on React? with fast REST APIs built in Go.
* **MQTT Integration:** Real-time communication between nodes and backend through MQTT broker.
* **Multi-connectivity:** Support for USB, Bluetooth, and Wi-Fi connections for nodes.
* **Complete Automation:** Automated installation and simplified configuration.

## Prerequisites

* **Go** (latest available version)
* **Node.js and npm** (for the React interface)?
* **MQTT Broker** (e.g., Mosquitto, optional if using the integrated broker)

## Quick Development Guide

Clone the repository and navigate to the project directory:

```bash
git clone https://github.com/folletto95/MeshSpy.git
cd MeshSpy
```

### Backend (Go)

1. Build the backend with Go:

```bash
make build
```

2. Start the backend (under development):

```bash
./meshspy
```

The backend will be available by default at `http://localhost:8000` (currently under development).

### Frontend (React)

1. Install dependencies and launch the web interface (under development):

```bash
cd meshspy-ui
npm install
npm run dev
```

The UI will be accessible at `http://localhost:5173` (currently under development).

### Admin Module

A minimal Spring Boot admin interface is available under the `admin` module.
First build the application, then start the admin service with Docker Compose:

```bash
docker compose up devel   # builds the JARs
docker compose up debug-admin
```

Once started, open `http://localhost:8080` to access a simple page showing the list of nodes and an interactive map.
Ensure your devices send their HTTP requests to the same address (replace `localhost` with your server IP if needed).
The admin service now persists nodes and pending requests under the `data/` directory (or a custom path via `-Dadmin.data.dir=...`).
The repository does not include Leaflet images to keep the history light. Run the helper script to download them locally before starting the admin module:

```bash
./scripts/get_leaflet_assets.sh
```

## Planned MQTT Configuration

The MQTT broker configuration is planned through a `config.json` file:

```json
{
  "mqtt_broker": "mqtt://localhost:1883",
  "mqtt_topic": "msh/#"
}
```

## Docker (Optional Planned Feature)

The option to launch MeshSpy with Docker is planned:

```bash
docker-compose up -d
```

## Contributing

If you would like to contribute to the project, please feel free to open issues, propose changes, or improvements through pull requests.

## License

MeshSpy is distributed under the GPL-3 license. See the [LICENSE](LICENSE) file for more details.
