document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('formUsuarioEmpleado');
    const idEmpleado = document.getElementById('idEmpleado'); // Campo oculto con el ID del empleado

    // Botones para ver contraseña
    const togglePassword = document.getElementById('togglePassword');
    const password = document.getElementById('password');
    const toggleValidatePassword = document.getElementById('toggleValidatePassword');
    const validatepassword = document.getElementById('validatepassword');

    const isEdit = idEmpleado && idEmpleado.value !== '';

    if (togglePassword) {
        togglePassword.addEventListener('click', function() {
            const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
            password.setAttribute('type', type);
            const icon = this.querySelector('i');
            icon.classList.toggle('fa-eye');
            icon.classList.toggle('fa-eye-slash');
        });
    }

    if (toggleValidatePassword) {
        toggleValidatePassword.addEventListener('click', function() {
            const type = validatepassword.getAttribute('type') === 'password' ? 'text' : 'password';
            validatepassword.setAttribute('type', type);
            const icon = this.querySelector('i');
            icon.classList.toggle('fa-eye');
            icon.classList.toggle('fa-eye-slash');
        });
    }

    form.addEventListener('submit', async function(e) {
        e.preventDefault();

        document.querySelectorAll('.is-invalid').forEach(el => {
            el.classList.remove('is-invalid');
        });

        let isValid = true;

        const nombre = document.getElementById('nombre');
        const apellido = document.getElementById('apellido');
        const documento = document.getElementById('documento');
        const telefono = document.getElementById('telefono');
        const username = document.getElementById('username');

        if (nombre.value.trim() === '') {
            mostrarError(nombre, 'El nombre es obligatorio.');
            isValid = false;
        }
        if (apellido.value.trim() === ''){
            mostrarError(apellido, 'El apellido es obligatorio.');
            isValid = false;
        }

        // --- VALIDACIÓN DOCUMENTO ---
        const regexDocumento = /^\d{5,10}$/;
        if (documento.value.trim() === ''){
            mostrarError(documento, 'El documento es obligatorio.');
            isValid = false;
        } else if (!regexDocumento.test(documento.value.trim())) {
            mostrarError(documento, 'El documento debe tener entre 5 y 10 números.');
            isValid = false;
        } else {
            // Solo validar duplicado si es un nuevo registro o si el documento cambió
            const originalDocumento = documento.getAttribute('data-original');
            if (!isEdit || documento.value.trim() !== originalDocumento) {
                try {
                    const response = await fetch(`/api/validar-documento?documento=${documento.value.trim()}`);
                    const existe = await response.json();
                    if (existe) {
                        mostrarError(documento, 'Este documento ya está registrado.');
                        isValid = false;
                    }
                } catch (error) {
                    console.error('Error al validar documento:', error);
                }
            }
        }

        const regexTelefono = /^\d{1,10}$/;
        if (telefono.value.trim() === '') {
            mostrarError(telefono, 'El teléfono es obligatorio.');
            isValid = false;
        } else if (!regexTelefono.test(telefono.value.trim())) {
            mostrarError(telefono, 'Solo números y máximo 10 dígitos permitidos.');
            isValid = false;
        }

        if (username.value.trim() === ''){
            mostrarError(username, 'El nombre de usuario es obligatorio.');
            isValid = false;
        }

        // --- VALIDACIÓN CONTRASEÑA (Solo si no es edición O si se ha escrito algo) ---
        const passwordValue = password.value.trim();
        const validatePasswordValue = validatepassword.value.trim();

        if (!isEdit || passwordValue !== '') {
            if (passwordValue === '') {
                mostrarError(password, 'La contraseña es obligatoria.');
                isValid = false;
            } else if (passwordValue.length < 8) {
                mostrarError(password, 'La contraseña debe tener al menos 8 caracteres.');
                isValid = false;
            }

            if (validatePasswordValue === '') {
                mostrarError(validatepassword, 'Debe repetir la contraseña.');
                isValid = false;
            } else if (passwordValue !== validatePasswordValue) {
                mostrarError(validatepassword, 'Las contraseñas no coinciden.');
                isValid = false;
            }
        }

        if (isValid) {
            const formData = new FormData(form);

            fetch(form.action, {
                method: 'POST',
                body: formData
            })
                .then(response => {
                    if (response.ok) {
                        Swal.fire({
                            icon: 'success',
                            title: isEdit ? '¡Usuario Actualizado!' : '¡Usuario Registrado!',
                            text: isEdit ? 'El usuario se ha actualizado con éxito.' : 'El usuario se ha guardado con éxito.',
                            confirmButtonText: 'Aceptar',
                            confirmButtonColor: '#007bff'
                        }).then((result) => {
                            if (result.isConfirmed || result.isDismissed) {
                                window.location.href = '/luxbar/usuarios';
                            }
                        });
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error',
                            text: 'Hubo un error al procesar la solicitud.',
                            confirmButtonText: 'Entendido',
                            confirmButtonColor: '#dc3545'
                        });
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
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

    function mostrarError(inputElement, mensaje) {
        inputElement.classList.add('is-invalid');
        let container = inputElement.closest('.mb-3') || inputElement.parentElement;
        let feedbackDiv = container.querySelector('.invalid-feedback');
        
        if (!feedbackDiv) {
            feedbackDiv = document.createElement('div');
            feedbackDiv.className = 'invalid-feedback';
            container.appendChild(feedbackDiv);
        }

        feedbackDiv.textContent = mensaje;
        feedbackDiv.style.display = 'block';
    }
});