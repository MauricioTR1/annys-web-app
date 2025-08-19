document.addEventListener('DOMContentLoaded', () => {
    const backendUrl = 'http://localhost:8080/api/proveedores';

    // Elementos
    const rfcInput = document.getElementById('rfc_proveedor');
    const nombreInput = document.getElementById('nombre_proveedor');
    const registroBtn = document.querySelector('.btn-registro');
    const tablaBody = document.querySelector('table tbody');

    let editMode = false;
    let rfcEditando = null; // Para saber a quién estamos editando

    cargarProveedores();

    // Registro o actualización de proveedor
    registroBtn.addEventListener('click', async (event) => {
        event.preventDefault();

        const rfc = rfcInput.value.trim();
        const nombre = nombreInput.value.trim();

        if (!rfc || !nombre) {
            alert('Por favor, completa todos los campos.');
            return;
        }

        if (!editMode) {
            // REGISTRO
            try {
                const response = await fetch(backendUrl, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        rfc: rfc,
                        nombreProveedor: nombre
                    })
                });

                if (response.ok) {
                    alert('¡Proveedor registrado!');
                    rfcInput.value = '';
                    nombreInput.value = '';
                    cargarProveedores();
                } else {
                    const errorText = await response.text();
                    alert(`Error al registrar: ${response.status} - ${errorText}`);
                }
            } catch (error) {
                alert('Error de conexión. Intenta de nuevo más tarde.');
                console.error(error);
            }
        } else {
            // ACTUALIZACIÓN
            try {
                const response = await fetch(`${backendUrl}/${encodeURIComponent(rfcEditando)}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        rfc: rfc,
                        nombreProveedor: nombre
                    })
                });

                if (response.ok) {
                    alert('¡Proveedor actualizado!');
                    rfcInput.value = '';
                    nombreInput.value = '';
                    registroBtn.textContent = 'Registrar Nuevo Proveedor';
                    registroBtn.classList.remove('btn-actualizar');
                    editMode = false;
                    rfcEditando = null;
                    cargarProveedores();
                } else {
                    const errorText = await response.text();
                    alert(`Error al actualizar: ${response.status} - ${errorText}`);
                }
            } catch (error) {
                alert('Error de conexión. Intenta de nuevo más tarde.');
                console.error(error);
            }
        }
    });

    // Función para cargar proveedores y renderizar la tabla
    async function cargarProveedores() {
        try {
            const response = await fetch(backendUrl);
            if (response.ok) {
                const proveedores = await response.json();
                tablaBody.innerHTML = '';

                if (proveedores.length === 0) {
                    const tr = document.createElement('tr');
                    tr.innerHTML = `<td colspan="3">No hay proveedores registrados.</td>`;
                    tablaBody.appendChild(tr);
                    return;
                }

                proveedores.forEach(proveedor => {
                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td>${proveedor.rfc}</td>
                        <td>${proveedor.nombreProveedor}</td>
                        <td class="acciones-col">
                            <button class="btn-editar" data-rfc="${proveedor.rfc}" data-nombre="${proveedor.nombreProveedor}">Editar</button>
                            <button class="btn-eliminar" data-rfc="${proveedor.rfc}" data-nombre="${proveedor.nombreProveedor}">Eliminar</button>
                        </td>
                    `;
                    tablaBody.appendChild(tr);
                });

                // Asignar eventos de eliminar y editar a los nuevos botones
                document.querySelectorAll('.btn-eliminar').forEach(btn => {
                    btn.addEventListener('click', eliminarProveedor);
                });
                document.querySelectorAll('.btn-editar').forEach(btn => {
                    btn.addEventListener('click', prepararEdicion);
                });
            } else {
                tablaBody.innerHTML = `<tr><td colspan="3">Error al cargar proveedores.</td></tr>`;
            }
        } catch (error) {
            tablaBody.innerHTML = `<tr><td colspan="3">Error de conexión.</td></tr>`;
            console.error(error);
        }
    }

    // Eliminar proveedor
    async function eliminarProveedor(event) {
        const rfc = event.target.getAttribute('data-rfc');
        const nombre = event.target.getAttribute('data-nombre');

        const confirmado = confirm(`¿Estás seguro de eliminar al proveedor ${nombre}? Esta acción es irreversible.`);
        if (!confirmado) return;

        try {
            const response = await fetch(`${backendUrl}/${encodeURIComponent(rfc)}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                alert('Proveedor eliminado.');
                cargarProveedores();
            } else {
                const errorText = await response.text();
                alert(`Error al eliminar: ${response.status} - ${errorText}`);
            }
        } catch (error) {
            alert('Error de conexión. Intenta de nuevo más tarde.');
            console.error(error);
        }
    }

    // Preparar edición
    function prepararEdicion(event) {
        const rfc = event.target.getAttribute('data-rfc');
        const nombre = event.target.getAttribute('data-nombre');

        rfcInput.value = rfc;
        nombreInput.value = nombre;
        rfcInput.disabled = true; // Para evitar que cambien el RFC (clave primaria)
        registroBtn.textContent = 'Actualizar Proveedor';
        registroBtn.classList.add('btn-actualizar');
        editMode = true;
        rfcEditando = rfc;
    }
});