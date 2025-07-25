let map;
let markers = [];

async function loadClients() {
    const response = await fetch('clients');
    const clients = await response.json();
    const online = document.querySelector('#online tbody');
    const offline = document.querySelector('#offline tbody');
    online.innerHTML = '';
    offline.innerHTML = '';
    markers.forEach(m => m.remove());
    markers = [];
    clients.forEach(c => {
        if (!c.name) return;
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${c.id}</td><td>${c.name}</td>`;
        online.appendChild(tr);

        if (c.latitude != null && c.longitude != null && map) {
            const count = c.nodeCount ?? c.nodes ?? c.connectedNodes ?? 0;
            const marker = L.marker([c.latitude, c.longitude]).addTo(map)
                .bindTooltip(`${c.name} - ${count} nodi connessi`);
            markers.push(marker);
        }
    });
}

async function loadRequests() {
    const response = await fetch('client-requests');
    const requests = await response.json();
    const pending = document.querySelector('#pending tbody');
    pending.innerHTML = '';
    requests.forEach(r => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${r.id}</td><td>${r.name || r.longName || ''}</td><td>${r.address ?? ''}</td>` +
            `<td><button data-id="${r.id}" class="approve">Approve</button> ` +
            `<button data-id="${r.id}" class="reject">Reject</button></td>`;
        pending.appendChild(tr);
    });
    document.querySelectorAll('.approve').forEach(btn => btn.addEventListener('click', approve));
    document.querySelectorAll('.reject').forEach(btn => btn.addEventListener('click', reject));
}

async function approve(e) {
    const id = e.target.dataset.id;
    await fetch(`/client-requests/${id}/approve`, {method: 'POST'});
    loadRequests();
    loadClients();
}

async function reject(e) {
    const id = e.target.dataset.id;
    await fetch(`/client-requests/${id}`, {method: 'DELETE'});
    loadRequests();
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
    await fetch('clients/reset', {method:'POST'});
    loadClients();
}

document.addEventListener('DOMContentLoaded', () => {
    initMap();
    loadClients();
    loadRequests();
    document.getElementById('reset-db').addEventListener('click', resetDb);
});
