let map;
let markers = [];

async function loadNodes() {
    const response = await fetch('nodes');
    const nodes = await response.json();
    const nodesTable = document.querySelector('#nodes tbody');
    const onlineTable = document.querySelector('#online tbody');
    if (nodesTable) nodesTable.innerHTML = '';
    if (onlineTable) onlineTable.innerHTML = '';
    markers.forEach(m => m.remove());
    markers = [];
    nodes.forEach(n => {
        if (nodesTable) {
            const tr = document.createElement('tr');
            tr.innerHTML = `<td>${n.id}</td><td>${n.name}</td><td>${n.address}</td><td>${n.latitude ?? ''}</td><td>${n.longitude ?? ''}</td>`;
            nodesTable.appendChild(tr);
        }
        if (onlineTable) {
            const tr = document.createElement('tr');
            tr.innerHTML = `<td>${n.id}</td><td>${n.name}</td><td>${n.address}</td>`;
            onlineTable.appendChild(tr);
        }
        if (n.latitude != null && n.longitude != null && map) {
            const marker = L.marker([n.latitude, n.longitude]).addTo(map).bindPopup(n.name);
            markers.push(marker);
        }
    });
}

async function loadRequests() {
    const response = await fetch('node-requests');
    const requests = await response.json();
    const table = document.querySelector('#requests tbody') || document.querySelector('#pending tbody');
    if (!table) return;
    table.innerHTML = '';
    requests.forEach(r => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${r.id}</td><td>${r.name || r.longName}</td><td>${r.address}</td><td><button data-id="${r.id}" class="info-btn">i</button> <button data-id="${r.id}" class="approve">Approve</button> <button data-id="${r.id}" class="reject">Reject</button></td>`;
        table.appendChild(tr);
        const info = document.createElement('tr');
        info.id = `info-${r.id}`;
        info.classList.add('info-row', 'hidden');
        info.innerHTML = `<td colspan="4"><strong>Model:</strong> ${r.model ?? ''}<br><strong>Firmware:</strong> ${r.firmware ?? ''}<br><strong>Longname:</strong> ${r.longName ?? ''}<br><strong>Shortname:</strong> ${r.shortName ?? ''}</td>`;
        table.appendChild(info);
    });
    document.querySelectorAll('.approve').forEach(btn => btn.addEventListener('click', approve));
    document.querySelectorAll('.reject').forEach(btn => btn.addEventListener('click', reject));
    document.querySelectorAll('.info-btn').forEach(btn => btn.addEventListener('click', toggleInfo));
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
    await fetch('nodes/reset', {method: 'POST'});
    loadNodes();
    loadRequests();
}

function initMap() {
    const mapEl = document.getElementById('map');
    if (!mapEl) return;
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

function resetMap() {
    if (map) {
        map.setView([0,0], 2);
    }
}

function resetMap() {
    if (map) {
        map.setView([0,0], 2);
    }
}

async function resetDb() {
    if (confirm('Reset all nodes and requests?')) {
        await fetch('nodes/reset', {method: 'POST'});
        loadNodes();
        loadRequests();
        resetMap();
    }
}

}

initMap();
loadNodes();
loadRequests();
document.getElementById('reset-db').addEventListener('click', resetDb);
