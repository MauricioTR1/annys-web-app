// Espera a que el DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', () => {
    // Obtiene el elemento del ícono de perfil por su clase o ID
    // Basado en tu HTML, el enlace del ícono de perfil está dentro de un div con clase 'profile-pic'
    // y el enlace en sí no tiene un ID, así que lo seleccionaremos por su etiqueta 'a' dentro de 'profile-pic'.
    const profileLink = document.querySelector('.profile-pic a');

    if (profileLink) {
        // Añade un event listener para el clic en el enlace del perfil
        profileLink.addEventListener('click', (event) => {
            // Previene el comportamiento por defecto del enlace (que es ir a 'inicioSesion.html' por defecto)
            event.preventDefault();

            // Verifica si existe un token JWT en el localStorage
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