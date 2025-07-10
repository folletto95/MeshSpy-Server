let map;
let markers = [];

async function loadClients() {
    const response = await fetch('/clients');
    const clients = await response.json();
    const pending = document.querySelector('#pending tbody');
    const online = document.querySelector('#online tbody');
    const offline = document.querySelector('#offline tbody');
    pending.innerHTML = '';
    online.innerHTML = '';
    offline.innerHTML = '';
    markers.forEach(m => m.remove());
    markers = [];
    clients.forEach(c => {
        if (!c.name) return;
        let target = online;
        const status = c.status || (c.online ? 'online' : c.approved ? 'offline' : 'pending');
        const tr = document.createElement('tr');
        if (status === 'pending') {
            target = pending;
            tr.innerHTML = `<td>${c.id}</td><td>${c.name}</td><td>${c.address ?? ''}</td>`;
        } else {
            if (status === 'offline') target = offline;
            tr.innerHTML = `<td>${c.id}</td><td>${c.name}</td>`;
        }
        target.appendChild(tr);

        if (c.latitude != null && c.longitude != null && map) {
            const count = c.nodeCount ?? c.nodes ?? c.connectedNodes ?? 0;
            const marker = L.marker([c.latitude, c.longitude]).addTo(map)
                .bindTooltip(`${c.name} - ${count} nodi connessi`);
            markers.push(marker);
        }
    });
}

function initMap() {
    map = L.map('map').setView([0,0],2);
    const osm = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom:19,
        attribution:'&copy; OpenStreetMap contributors'
    });
    osm.on('tileerror', () => {
        if(!map.hasLayer(osm)) return;
        map.removeLayer(osm);
        L.tileLayer('leaflet/blank.png', {maxZoom:3, attribution:'Offline tiles'}).addTo(map);
    });
    osm.addTo(map);
}

async function resetDb() {
    await fetch('/reset', {method:'POST'});
    loadClients();
}

document.addEventListener('DOMContentLoaded', () => {
    initMap();
    loadClients();
    document.getElementById('reset-db').addEventListener('click', resetDb);
});
