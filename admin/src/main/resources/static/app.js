let map;
let markers = [];

async function loadNodes() {
    const response = await fetch('/nodes');
    const nodes = await response.json();
    const tbody = document.querySelector('#nodes tbody');
    tbody.innerHTML = '';
    markers.forEach(m => m.remove());
    markers = [];
    nodes.forEach(n => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${n.id}</td><td>${n.name}</td><td>${n.address}</td><td>${n.latitude ?? ''}</td><td>${n.longitude ?? ''}</td>`;
        tbody.appendChild(tr);
        if (n.latitude != null && n.longitude != null && map) {
            const marker = L.marker([n.latitude, n.longitude]).addTo(map).bindPopup(n.name);
            markers.push(marker);
        }
    });
}

async function loadRequests() {
    const response = await fetch('/node-requests');
    const requests = await response.json();
    const tbody = document.querySelector('#requests tbody');
    tbody.innerHTML = '';
    requests.forEach(r => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${r.id}</td><td>${r.name}</td><td>${r.address}</td><td><button data-id="${r.id}" class="approve">Approve</button> <button data-id="${r.id}" class="reject">Reject</button></td>`;
        tbody.appendChild(tr);
    });
    document.querySelectorAll('.approve').forEach(btn => btn.addEventListener('click', approve));
    document.querySelectorAll('.reject').forEach(btn => btn.addEventListener('click', reject));
}

async function approve(e) {
    const id = e.target.dataset.id;
    await fetch(`/node-requests/${id}/approve`, {method: 'POST'});
    loadRequests();
    loadNodes();
}

async function reject(e) {
    const id = e.target.dataset.id;
    await fetch(`/node-requests/${id}`, {method: 'DELETE'});
    loadRequests();
}

async function resetDb() {
    await fetch('/reset', {method: 'POST'});
    loadNodes();
    loadRequests();
}

function initMap() {
    map = L.map('map').setView([0, 0], 2);

    const osm = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; OpenStreetMap contributors'
    });

    osm.on('tileerror', () => {
        if (!map.hasLayer(osm)) return;
        map.removeLayer(osm);
        L.tileLayer('leaflet/blank.png', {
            maxZoom: 3,
            attribution: 'Offline tiles'
        }).addTo(map);
    });

    osm.addTo(map);
}

initMap();
loadNodes();
loadRequests();
document.getElementById('reset-db').addEventListener('click', resetDb);
