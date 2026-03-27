document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('formSede');

    form.addEventListener('submit', function(e) {
        // 1. Evitamos que el formulario haga el envío clásico (recarga de página)
        e.preventDefault();

        // Limpiamos los errores previos
        document.querySelectorAll('.is-invalid').forEach(el => {
            el.classList.remove('is-invalid');
        });

        let isValid = true;

        // Referencias a los inputs
        const nombre = document.getElementById('nombre');
        const telefono = document.getElementById('telefono');
        const ciudad = document.getElementById('ciudad');
        const direccion = document.getElementById('direccion');

        // --- FILTRO 1: Todos los campos son obligatorios ---
        if (nombre.value.trim() === '') {
            mostrarError(nombre, 'El nombre es obligatorio.');
            isValid = false;
        }

        if (ciudad.value.trim() === '') {
            mostrarError(ciudad, 'La ciudad es obligatoria.');
            isValid = false;
        }

        // --- FILTRO 2: Teléfono (Obligatorio, numérico, máximo 10 dígitos) ---
        // Expresión regular: Solo números, entre 1 y 10 caracteres
        const regexTelefono = /^\d{1,10}$/;
        if (telefono.value.trim() === '') {
            mostrarError(telefono, 'El teléfono es obligatorio.');
            isValid = false;
        } else if (!regexTelefono.test(telefono.value.trim())) {
            mostrarError(telefono, 'Solo números y máximo 10 dígitos permitidos.');
            isValid = false;
        }

        // --- FILTRO 3: Dirección (Obligatorio, mínimo 5 caracteres) ---
        if (direccion.value.trim() === '') {
            mostrarError(direccion, 'La dirección es obligatoria.');
            isValid = false;
        } else if (direccion.value.trim().length < 5) {
            mostrarError(direccion, 'La dirección debe tener al menos 5 caracteres.');
            isValid = false;
        }

        // --- ENVÍO POR JS (Fetch API) ---
        if (isValid) {
            // Recolectamos todos los datos del formulario (esto respeta el th:object de Spring)
            const formData = new FormData(form);

            fetch(form.action, {
                method: 'POST',
                body: formData
            })
            .then(response => {
                if (response.ok) {
                    // SweetAlert de éxito estilo AdminLTE
                    Swal.fire({
                        icon: 'success',
                        title: '¡Sede Registrada!',
                        text: 'La sede se ha guardado con éxito.',
                        confirmButtonText: 'Aceptar',
                        confirmButtonColor: '#007bff' // Color primario de AdminLTE
                    }).then((result) => {
                        // Se ejecuta cuando el usuario cierra la alerta
                        if (result.isConfirmed || result.isDismissed) {
                            window.location.href = '/luxbar/sedes';
                        }
                    });
                } else {
                    // SweetAlert de error del servidor
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: 'Hubo un error al guardar la sede en el servidor.',
                        confirmButtonText: 'Entendido',
                        confirmButtonColor: '#dc3545' // Color danger de AdminLTE
                    });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                // SweetAlert de error de red/conexión
                Swal.fire({
                    icon: 'error',
                    title: 'Error de conexión',
                    text: 'No se pudo conectar con el servidor.',
                    confirmButtonText: 'Entendido',
                    confirmButtonColor: '#dc3545'
                });
            });
        }
    });

    // Función auxiliar para pintar el error al estilo AdminLTE
    function mostrarError(inputElement, mensaje) {
        inputElement.classList.add('is-invalid'); // Pinta de rojo el borde
        const feedbackDiv = inputElement.nextElementSibling; // Busca el div.invalid-feedback hermano
        if (feedbackDiv && feedbackDiv.classList.contains('invalid-feedback')) {
            feedbackDiv.textContent = mensaje;
        }
    }
});