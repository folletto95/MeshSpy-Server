async function loadNodes() {
    const response = await fetch('/nodes');
    const nodes = await response.json();
    const tbody = document.querySelector('#nodes tbody');
    tbody.innerHTML = '';
    nodes.forEach(n => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${n.id}</td><td>${n.name}</td><td>${n.address}</td>`;
        tbody.appendChild(tr);
    });
}

document.getElementById('node-form').addEventListener('submit', async e => {
    e.preventDefault();
    const node = {
        id: document.getElementById('node-id').value,
        name: document.getElementById('node-name').value,
        address: document.getElementById('node-address').value
    };
    await fetch('/nodes', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(node)
    });
    e.target.reset();
    loadNodes();
});

loadNodes();
