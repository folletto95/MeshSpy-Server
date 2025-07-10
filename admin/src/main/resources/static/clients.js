let map;
let markers = [];

async function loadClients() {
    const response = await fetch('/clients');
    const clients = await response.json();
    const tbody = document.querySelector('#clients tbody');
    tbody.innerHTML = '';
    markers.forEach(m => m.remove());
    markers = [];
    clients.forEach(c => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${c.id}</td><td>${c.name}</td>`;
        tbody.appendChild(tr);
        if (c.latitude != null && c.longitude != null && map) {
            const marker = L.marker([c.latitude, c.longitude]).addTo(map).bindPopup(c.name);
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
