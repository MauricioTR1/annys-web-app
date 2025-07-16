// Espera a que el DOM esté completamente cargado antes de ejecutar el script
document.addEventListener('DOMContentLoaded', () => {
    // Obtiene el formulario de registro por su ID
    const registerForm = document.getElementById('registrar-usuario');

    // Verifica que el formulario exista antes de añadir el event listener
    if (registerForm) {
        // Añade un event listener para el evento 'submit' del formulario
        registerForm.addEventListener('submit', async (event) => {
            // Previene el comportamiento por defecto del formulario (recargar la página)
            event.preventDefault();

            // Obtiene los valores de los campos del formulario
            const nombre = document.getElementById('nombre').value.trim();
            const apellidos = document.getElementById('apellidos').value.trim();
            const correo = document.getElementById('correo').value.trim();
            const contrasena = document.getElementById('contrasena').value.trim();

            // Validación básica del lado del cliente
            if (!nombre || !apellidos || !correo || !contrasena) {
                alert('Por favor, completa todos los campos.');
                return; // Detiene la ejecución si algún campo está vacío
            }

            // Combina nombre y apellidos para el campo 'nombreUsuario' del backend
            const nombreCompleto = `${nombre} ${apellidos}`;

            // Prepara el objeto de datos para enviar al backend
            // Los nombres de las propiedades deben coincidir con los campos de tu entidad Usuarios en Spring Boot
            const userData = {
                nombreUsuario: nombreCompleto,
                correoUsuario: correo,
                contraseniaUsuario: contrasena
            };

            // Define la URL de tu endpoint de registro en el backend
            const backendUrl = 'http://localhost:8080/api/auth/register';

            try {
                // Realiza la solicitud POST al backend usando fetch API
                const response = await fetch(backendUrl, {
                    mode: 'cors',
                    method: 'POST', // Método HTTP POST
                    headers: {
                        'Content-Type': 'application/json' // Indica que el cuerpo de la solicitud es JSON
                    },
                    body: JSON.stringify(userData) // Convierte el objeto JavaScript a una cadena JSON
                });

                // Procesa la respuesta del backend
                if (response.ok) { // Si la respuesta es 2xx (ej. 201 Created)
                    const result = await response.json(); // Parsea la respuesta JSON
                    alert('¡Registro exitoso! Ahora puedes iniciar sesión.'); // Muestra un mensaje de éxito
                    console.log('Usuario registrado:', result);
                    // Redirige al usuario a la página de inicio de sesión
                    window.location.href = 'inicioSesion.html';
                } else if (response.status === 409) { // Si el estado es 409 Conflict (usuario/correo ya existe)
                    const errorData = await response.text(); // Lee el mensaje de error del cuerpo
                    alert(`Error de registro: ${errorData}`); // Muestra el mensaje de error específico
                    console.error('Error de conflicto:', errorData);
                } else { // Para otros errores (ej. 400 Bad Request, 500 Internal Server Error)
                    const errorData = await response.text(); // Lee el mensaje de error
                    alert(`Error al registrar usuario: ${response.status} - ${errorData || response.statusText}`);
                    console.error('Error en el registro:', response.status, errorData);
                }
            } catch (error) {
                // Captura cualquier error de red o de la solicitud fetch
                alert('Error de conexión. Por favor, intenta de nuevo más tarde.');
                console.error('Error de red o solicitud:', error);
            }
        });
    } else {
        console.error('Formulario de registro no encontrado.');
    }
});