document.addEventListener('DOMContentLoaded', () => {
    const registerForm = document.getElementById('registrar-usuario');
    if (registerForm) {
        registerForm.addEventListener('submit', async (event) => {
            event.preventDefault();

            const nombre = document.getElementById('nombre').value.trim();
            const apellidos = document.getElementById('apellidos').value.trim();
            const correo = document.getElementById('correo').value.trim();
            const contrasena = document.getElementById('contrasena').value.trim();

            if (!nombre || !apellidos || !correo || !contrasena) {
                alert('Por favor, completa todos los campos.');
                return;
            }

            const nombreCompleto = `${nombre} ${apellidos}`;

            const userData = {
                nombreUsuario: nombreCompleto,
                correoUsuario: correo,
                contraseniaUsuario: contrasena
            };

            const backendUrl = 'http://localhost:8080/api/auth/register';

            try {
                const response = await fetch(backendUrl, {
                    mode: 'cors',
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(userData)
                });

                if (response.ok) {
                    const result = await response.json();
                    alert('¡Registro exitoso! Ahora puedes iniciar sesión.');
                    console.log('Usuario registrado:', result);
                    window.location.href = 'inicioSesion.html';
                } else if (response.status === 409) {
                    const errorData = await response.text();
                    alert(`Error de registro: ${errorData}`);
                    console.error('Error de conflicto:', errorData);
                } else {
                    const errorData = await response.text();
                    alert(`Error al registrar usuario: ${response.status} - ${errorData || response.statusText}`);
                    console.error('Error en el registro:', response.status, errorData);
                }
            } catch (error) {
                alert('Error de conexión. Por favor, intenta de nuevo más tarde.');
                console.error('Error de red o solicitud:', error);
            }
        });
    } else {
        console.error('Formulario de registro no encontrado.');
    }
});