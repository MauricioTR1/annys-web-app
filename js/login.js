document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('form-login');
    if (loginForm) {
        loginForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            const correo = document.getElementById('correo').value.trim();
            const contrasena = document.getElementById('contrasena').value.trim();
            if (!correo || !contrasena) {
                alert('Por favor, ingresa tu correo electrónico y contraseña.');
                return;
            }
            const loginData = {
                username: correo,
                password: contrasena
            };
            const backendLoginUrl = 'http://localhost:8080/api/auth/login';

            try {
                const response = await fetch(backendLoginUrl, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(loginData)
                });

                if (response.ok) {
                    const result = await response.json();
                    localStorage.setItem('jwtToken', result.jwtToken);
                    localStorage.setItem('userEmail', result.email);

                    alert('¡Inicio de sesión exitoso!');
                    console.log('Token JWT:', result.jwtToken);
                    console.log('Correo del usuario:', result.email);
                    window.location.href = 'Inicio.html';
                } else if (response.status === 401) { // Si el estado es 401 Unauthorized (credenciales inválidas)
                    const errorData = await response.text();
                    alert(`Error de inicio de sesión: ${errorData || 'Credenciales inválidas'}`);
                    console.error('Error de autenticación:', response.status, errorData);
                } else { // Para otros errores (ej. 400 Bad Request, 500 Internal Server Error)
                    const errorData = await response.text();
                    alert(`Error al iniciar sesión: ${response.status} - ${errorData || response.statusText}`);
                    console.error('Error en el inicio de sesión:', response.status, errorData);
                }
            } catch (error) {
                alert('Error de conexión. Por favor, intenta de nuevo más tarde.');
                console.error('Error de red o solicitud:', error);
            }
        });
    } else {
        console.error('Formulario de login no encontrado.');
    }
});
