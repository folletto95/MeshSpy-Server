async function loadVersion(){
    try {
        const response = await fetch('/version');
        const v = await response.text();
        const el = document.getElementById('version');
        if (el) {
            el.textContent = 'v' + v;
        }
    } catch (e) {
        console.warn('Failed to load version', e);
    }
}

document.addEventListener('DOMContentLoaded', loadVersion);
