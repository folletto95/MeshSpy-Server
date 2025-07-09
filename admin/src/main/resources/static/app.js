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

document.getElementById('node-form').addEventListener('submit', async e => {
    e.preventDefault();
    const node = {
        id: document.getElementById('node-id').value,
        name: document.getElementById('node-name').value,
        address: document.getElementById('node-address').value,
        latitude: parseFloat(document.getElementById('node-lat').value),
        longitude: parseFloat(document.getElementById('node-lon').value)
    };
    await fetch('/nodes', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(node)
    });
    e.target.reset();
    loadNodes();
});

function initMap() {
    map = L.map('map').setView([0, 0], 2);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; OpenStreetMap contributors'
    }).addTo(map);
}

initMap();
loadNodes();
