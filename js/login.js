// Espera a que el DOM esté completamente cargado antes de ejecutar el script
document.addEventListener('DOMContentLoaded', () => {
    // Obtiene el formulario de login por su ID
    const loginForm = document.getElementById('form-login');

    // Verifica que el formulario exista antes de añadir el event listener
    if (loginForm) {
        // Añade un event listener para el evento 'submit' del formulario
        loginForm.addEventListener('submit', async (event) => {
            // Previene el comportamiento por defecto del formulario (recargar la página)
            event.preventDefault();

            // Obtiene los valores de los campos del formulario
            const correo = document.getElementById('correo').value.trim();
            const contrasena = document.getElementById('contrasena').value.trim();

            // Validación básica del lado del cliente
            if (!correo || !contrasena) {
                alert('Por favor, ingresa tu correo electrónico y contraseña.');
                return; // Detiene la ejecución si algún campo está vacío
            }

            // Prepara el objeto de datos para enviar al backend
            // La propiedad 'username' debe coincidir con la que espera tu LoginRequest DTO en Spring Boot
            // (que configuramos para que sea el correo electrónico)
            const loginData = {
                username: correo, // El correo electrónico se envía como 'username'
                password: contrasena
            };

            // Define la URL de tu endpoint de login en el backend
            const backendLoginUrl = 'http://localhost:8080/api/auth/login';

            try {
                // Realiza la solicitud POST al backend usando fetch API
                const response = await fetch(backendLoginUrl, {
                    method: 'POST', // Método HTTP POST
                    headers: {
                        'Content-Type': 'application/json' // Indica que el cuerpo de la solicitud es JSON
                    },
                    body: JSON.stringify(loginData) // Convierte el objeto JavaScript a una cadena JSON
                });

                // Procesa la respuesta del backend
                if (response.ok) { // Si la respuesta es 2xx (ej. 200 OK)
                    const result = await response.json(); // Parsea la respuesta JSON (JwtResponse)
                    
                    // Almacena el token JWT y el correo electrónico en localStorage
                    // localStorage es ideal para mantener la sesión iniciada incluso si el navegador se cierra
                    localStorage.setItem('jwtToken', result.jwtToken);
                    localStorage.setItem('userEmail', result.email);

                    alert('¡Inicio de sesión exitoso!'); // Muestra un mensaje de éxito
                    console.log('Token JWT:', result.jwtToken);
                    console.log('Correo del usuario:', result.email);

                    // Redirige al usuario a la página de inicio (o a donde desees después del login)
                    window.location.href = 'Inicio.html';
                } else if (response.status === 401) { // Si el estado es 401 Unauthorized (credenciales inválidas)
                    const errorData = await response.text(); // Lee el mensaje de error del cuerpo
                    alert(`Error de inicio de sesión: ${errorData || 'Credenciales inválidas'}`);
                    console.error('Error de autenticación:', response.status, errorData);
                } else { // Para otros errores (ej. 400 Bad Request, 500 Internal Server Error)
                    const errorData = await response.text(); // Lee el mensaje de error
                    alert(`Error al iniciar sesión: ${response.status} - ${errorData || response.statusText}`);
                    console.error('Error en el inicio de sesión:', response.status, errorData);
                }
            } catch (error) {
                // Captura cualquier error de red o de la solicitud fetch
                alert('Error de conexión. Por favor, intenta de nuevo más tarde.');
                console.error('Error de red o solicitud:', error);
            }
        });
    } else {
        console.error('Formulario de login no encontrado.');
    }
});
