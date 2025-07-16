document.addEventListener('DOMContentLoaded', async () => {
    // Get references to the HTML elements where user data will be displayed
    const nombreUsuarioElement = document.getElementById('nombre-usuario');
    const correoUsuarioElement = document.getElementById('correo-usuario');
    const logoutButton = document.getElementById('cerrar-sesion'); // ID for the logout button

    // Function to log out (removes token and redirects)
    function cerrarSesion() {
        localStorage.removeItem('jwtToken'); // Remove the stored JWT token
        localStorage.removeItem('userEmail'); // Remove the stored user email
        alert('Has cerrado sesión.'); // Notify the user
        window.location.href = 'inicioSesion.html'; // Redirect to the login page
    }

    // Asynchronous function to load and display user profile data
    async function cargarDatosPerfil() {
        const jwtToken = localStorage.getItem('jwtToken'); // Get the JWT token from local storage
        const userEmail = localStorage.getItem('userEmail'); // Get the user email from local storage (for initial check)

        // If no token or email, the user is not authenticated
        if (!jwtToken || !userEmail) {
            alert('No has iniciado sesión. Por favor, inicia sesión para ver tu perfil.');
            window.location.href = 'inicioSesion.html'; // Redirect to login
            return; // Stop execution
        }

        // UPDATED: Now using the specific endpoint for the authenticated user's profile
        const backendUrl = 'http://localhost:8080/api/perfil/mi-perfil';

        try {
            // Make a GET request to the backend to get the authenticated user's profile
            const response = await fetch(backendUrl, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${jwtToken}`, // Send the JWT token in the Authorization header
                    'Content-Type': 'application/json'
                }
            });

            // Process the backend response
            if (response.ok) { // If the response is successful (2xx status code)
                // UPDATED: We now expect a single user object, not a list
                const usuarioActual = await response.json();

                if (usuarioActual) {
                    // If the user is found, update the content of the HTML elements
                    nombreUsuarioElement.textContent = usuarioActual.nombreUsuario;
                    correoUsuarioElement.textContent = usuarioActual.correoUsuario;
                } else {
                    // This should ideally not happen if response.ok is true, but as a fallback
                    alert('No se encontraron los datos de tu perfil.');
                    console.error('Empty or invalid profile data received.');
                    cerrarSesion(); // Log out as profile could not be loaded
                }
            } else if (response.status === 401) {
                // If the token is invalid or expired, a 401 Unauthorized is received
                alert('Tu sesión ha expirado o es inválida. Por favor, inicia sesión de nuevo.');
                cerrarSesion(); // Log out and redirect to login
            } else {
                // Handle other server errors (e.g., 500 Internal Server Error)
                const errorData = await response.text();
                alert(`Error al cargar el perfil: ${response.status} - ${errorData || response.statusText}`);
                console.error('Error loading profile:', response.status, errorData);
                cerrarSesion(); // Consider logging out in case of severe errors
            }
        } catch (error) {
            // Handle network errors (e.g., server unavailable)
            alert('Error de conexión al cargar el perfil. Por favor, intenta de nuevo más tarde.');
            console.error('Network error loading profile:', error);
            cerrarSesion(); // Log out in case of connection issues
        }
    }

    // Call the function to load profile data when the page loads
    cargarDatosPerfil();

    // Add the event listener to the logout button
    if (logoutButton) {
        logoutButton.addEventListener('click', (event) => {
            event.preventDefault(); // Prevent any default button behavior
            cerrarSesion(); // Call the logout function
        });
    } else {
        console.error('Logout button with ID "cerrar-sesion" not found.');
    }
});