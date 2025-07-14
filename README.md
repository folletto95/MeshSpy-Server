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

Once started, open `http://localhost:8080` to access simple pages showing the list of nodes or clients and an interactive map.

The repository does not include Leaflet images to keep the history light. Run the helper script to download them locally before starting the admin module.  The script fetches the assets directly from GitHub:

```bash
./scripts/get_leaflet_assets.sh
```

The map attempts to load tiles from OpenStreetMap. If the tiles cannot be retrieved (for example due to blocked network access), it falls back to a blank offline tile and the map will appear empty.

### Environment configuration

Both the admin and local modules automatically load variables from a `.env` file at startup. You can override default paths or other settings by creating a file named `.env` in the project root:

```properties
ADMIN_DATA_DIR=/path/to/data
```

Values provided via real environment variables still take precedence over the contents of `.env`.
## Admin REST API

The admin service exposes a small JSON API used by the web pages under
`admin/src/main/resources/static` and by Meshtastic devices. When running
`docker compose up debug-admin` the service is reachable at
`http://localhost:8080`.

### Node endpoints

* `GET /nodes` – list all approved nodes.
* `GET /nodes/{id}` – details for a single node or `404 Not Found`.
* `POST /nodes` – register a node. If the node ID has already been approved the
  saved node is returned with status `201 Created`; otherwise a registration
  request is stored and the server replies with `202 Accepted`.
* `POST /nodes/reset` – remove all nodes but keep pending requests (development only).

### Node management endpoints

Endpoints for firmware configuration backups and updates are provided under `/nodes/{id}`:

* `GET /nodes/{id}/backups` – list stored configuration backups for the node.
* `POST /nodes/{id}/backup` – upload a new configuration backup.
* `POST /nodes/{id}/restore` – restore configuration from the provided backup data.
* `POST /nodes/{id}/firmware/update` – record a firmware update request. The payload
  can specify a version or URL of the image to install.
* `GET /nodes/{id}/firmware` – return the latest known firmware version for the node.

Example request body for `POST /nodes`:

```json
{
  "id": "AA:BB:CC",
  "name": "Test Node",
  "address": "192.168.1.10",
  "latitude": 45.0,
  "longitude": 9.0
}
```

### Registration request endpoints

* `GET /node-requests` – list pending registration requests.
* `POST /node-requests` – create a new request. In addition to the fields above
  it accepts `model`, `firmware`, `longName` and `shortName`.
* `POST /node-requests/{id}/approve` – approve a request and promote the node.
* `DELETE /node-requests/{id}` – reject and delete a request.

All endpoints return JSON data.

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
