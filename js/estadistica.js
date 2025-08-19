document.addEventListener('DOMContentLoaded', () => {
    // Datos de ejemplo para vista previa
    const datosEjemplo = [
        { fecha: "2025-07-13", ventas: 15, dinero: 2350.00 },
        { fecha: "2025-07-14", ventas: 20, dinero: 3100.00 },
        { fecha: "2025-07-15", ventas: 25, dinero: 4500.00 },
        { fecha: "2025-07-16", ventas: 18, dinero: 2800.00 },
        { fecha: "2025-07-17", ventas: 22, dinero: 3600.00 },
        { fecha: "2025-07-18", ventas: 30, dinero: 5200.00 },
        { fecha: "2025-07-19", ventas: 28, dinero: 4950.00 }
    ];

    // Formatear dinero
    function formateaDinero(n) {
        return n.toLocaleString('es-MX', { style: 'currency', currency: 'MXN' });
    }

    // Llenar tabla
    function renderTabla(datos) {
        const tbody = document.getElementById('tabla-ventas7dias');
        tbody.innerHTML = '';
        datos.forEach(row => {
            const tr = document.createElement('tr');
            tr.innerHTML = `<td>${row.fecha}</td><td>${row.ventas}</td><td>${formateaDinero(row.dinero)}</td>`;
            tbody.appendChild(tr);
        });
    }

    // Gráfica de ventas (líneas)
    function renderGraficaVentas(datos) {
        const ctx = document.getElementById('ventas7diasChart').getContext('2d');
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: datos.map(d => d.fecha),
                datasets: [{
                    label: 'Ventas',
                    data: datos.map(d => d.ventas),
                    borderColor: '#007bff',
                    backgroundColor: 'rgba(0,123,255,0.15)',
                    fill: true,
                    tension: 0.3,
                    pointBackgroundColor: '#007bff'
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { display: true }
                },
                scales: {
                    y: { beginAtZero: true, ticks: { stepSize: 5 } }
                }
            }
        });
    }

    // Gráfica de dinero ganado (barras)
    function renderGraficaDinero(datos) {
        const ctx = document.getElementById('dinero7diasChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: datos.map(d => d.fecha),
                datasets: [{
                    label: 'Dinero ganado',
                    data: datos.map(d => d.dinero),
                    backgroundColor: '#28a745',
                    borderColor: '#218838',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { display: true }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) {
                                return formateaDinero(value);
                            }
                        }
                    }
                }
            }
        });
    }

    // Para vista previa manual:
    renderTabla(datosEjemplo);
    renderGraficaVentas(datosEjemplo);
    renderGraficaDinero(datosEjemplo);

    // Si tienes backend, descomenta y ajusta esto:
    /*
    fetch('http://localhost:8080/api/estadisticas/ultimos7dias')
        .then(resp => resp.json())
        .then(datos => {
            renderTabla(datos);
            renderGraficaVentas(datos);
            renderGraficaDinero(datos);
        })
        .catch(e => {
            renderTabla(datosEjemplo);
            renderGraficaVentas(datosEjemplo);
            renderGraficaDinero(datosEjemplo);
        });
    */
});