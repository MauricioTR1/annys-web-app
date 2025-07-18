document.addEventListener('DOMContentLoaded', () => {
    const profileLink = document.querySelector('.profile-pic a');

    if (profileLink) {
        profileLink.addEventListener('click', (event) => {
            event.preventDefault();
            const jwtToken = localStorage.getItem('jwtToken');

            if (jwtToken) {
                // Si hay un token, el usuario está logueado, redirige a la página de perfil
                window.location.href = 'Perfil.html';
            } else {
                // Si no hay token, el usuario no está logueado, redirige a la página de inicio de sesión
                window.location.href = 'inicioSesion.html';
            }
        });
    } else {
        console.error('Enlace de perfil no encontrado. Asegúrate de que exista un <a> dentro de .profile-pic.');
    }
});