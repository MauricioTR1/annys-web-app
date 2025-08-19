document.addEventListener('DOMContentLoaded', async () => {
    const nombreUsuarioElement = document.getElementById('nombre-usuario');
    const correoUsuarioElement = document.getElementById('correo-usuario');
    const logoutButton = document.getElementById('cerrar-sesion');

    function cerrarSesion() {
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('userEmail');
        alert('Has cerrado sesión.');
        window.location.href = 'inicioSesion.html';
    }
    async function cargarDatosPerfil() {
        const jwtToken = localStorage.getItem('jwtToken');
        const userEmail = localStorage.getItem('userEmail');
        if (!jwtToken || !userEmail) {
            alert('No has iniciado sesión. Por favor, inicia sesión para ver tu perfil.');
            window.location.href = 'inicioSesion.html';
            return;
        }
        const backendUrl = 'http://localhost:8080/api/perfil/mi-perfil';

        try {
            const response = await fetch(backendUrl, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${jwtToken}`,
                    'Content-Type': 'application/json'
                }
            });
            if (response.ok) {
                const usuarioActual = await response.json();

                if (usuarioActual) {
                    nombreUsuarioElement.textContent = usuarioActual.nombreUsuario;
                    correoUsuarioElement.textContent = usuarioActual.correoUsuario;
                } else {
                    alert('No se encontraron los datos de tu perfil.');
                    console.error('Empty or invalid profile data received.');
                    cerrarSesion();
                }
            } else if (response.status === 401) {
                alert('Tu sesión ha expirado o es inválida. Por favor, inicia sesión de nuevo.');
                cerrarSesion();
            } else {
                const errorData = await response.text();
                alert(`Error al cargar el perfil: ${response.status} - ${errorData || response.statusText}`);
                console.error('Error loading profile:', response.status, errorData);
                cerrarSesion();
            }
        } catch (error) {
            alert('Error de conexión al cargar el perfil. Por favor, intenta de nuevo más tarde.');
            console.error('Network error loading profile:', error);
            cerrarSesion();
        }
    }
    cargarDatosPerfil();

    if (logoutButton) {
        logoutButton.addEventListener('click', (event) => {
            event.preventDefault();
            cerrarSesion();
        });
    } else {
        console.error('Logout button with ID "cerrar-sesion" not found.');
    }
});