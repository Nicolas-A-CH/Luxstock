document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('formProducto');

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        document.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));

        const nombre = document.getElementById('nombre');
        const tipo = document.getElementById('tipo');
        const precio = document.getElementById('precio');
        let isValid = true;

        if (nombre.value.trim() === '') {
            mostrarError(nombre, 'El nombre es obligatorio.');
            isValid = false;
        }

        if (tipo.value === '') {
            mostrarError(tipo, 'El tipo es obligatorio.');
            isValid = false;
        }

        if (precio.value === '' || parseFloat(precio.value) <= 0) {
            mostrarError(precio, 'El precio debe ser mayor a 0.');
            isValid = false;
        }

        if (!isValid) return;

        fetch(form.action, {
            method: 'POST',
            body: new FormData(form)
        })
        .then(response => {
            if (response.ok) {
                Swal.fire({
                    icon: 'success',
                    title: '¡Producto guardado!',
                    text: 'El producto se ha guardado con éxito.',
                    confirmButtonText: 'Aceptar',
                    confirmButtonColor: '#007bff'
                }).then(() => {
                    window.location.href = '/luxbar/productos';
                });
            } else {
                return response.text().then(msg => {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: msg || 'Hubo un error al guardar el producto.',
                        confirmButtonText: 'Entendido',
                        confirmButtonColor: '#dc3545'
                    });
                });
            }
        })
        .catch(() => {
            Swal.fire({
                icon: 'error',
                title: 'Error de conexión',
                text: 'No se pudo conectar con el servidor.',
                confirmButtonText: 'Entendido',
                confirmButtonColor: '#dc3545'
            });
        });
    });

    function mostrarError(el, mensaje) {
        el.classList.add('is-invalid');
        const feedback = el.nextElementSibling;
        if (feedback && feedback.classList.contains('invalid-feedback')) {
            feedback.textContent = mensaje;
        }
    }
});