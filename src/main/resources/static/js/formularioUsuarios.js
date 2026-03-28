document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('formUsuarioEmpleado');

    // Botones para ver contraseña
    const togglePassword = document.getElementById('togglePassword');
    const password = document.getElementById('password');
    const toggleValidatePassword = document.getElementById('toggleValidatePassword');
    const validatepassword = document.getElementById('validatepassword');

    if (togglePassword) {
        togglePassword.addEventListener('click', function() {
            const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
            password.setAttribute('type', type);
            this.querySelector('i').classList.toggle('fa-eye');
            this.querySelector('i').classList.toggle('fa-eye-slash');
        });
    }

    if (toggleValidatePassword) {
        toggleValidatePassword.addEventListener('click', function() {
            const type = validatepassword.getAttribute('type') === 'password' ? 'text' : 'password';
            validatepassword.setAttribute('type', type);
            this.querySelector('i').classList.toggle('fa-eye');
            this.querySelector('i').classList.toggle('fa-eye-slash');
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

        // --- VALIDACIÓN DOCUMENTO (5-10 caracteres, solo números, no duplicado) ---
        const regexDocumento = /^\d{5,10}$/;
        if (documento.value.trim() === ''){
            mostrarError(documento, 'El documento es obligatorio.');
            isValid = false;
        } else if (!regexDocumento.test(documento.value.trim())) {
            mostrarError(documento, 'El documento debe tener entre 5 y 10 números.');
            isValid = false;
        } else {
            // Validar si ya existe en la BD
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

        if (password.value.trim() === '') {
            mostrarError(password, 'La contraseña es obligatoria.');
            isValid = false;
        } else if (password.value.length < 8) {
            mostrarError(password, 'La contraseña debe tener al menos 8 caracteres.');
            isValid = false;
        }

        if (validatepassword.value.trim() === '') {
            mostrarError(validatepassword, 'Debe repetir la contraseña.');
            isValid = false;
        } else if (password.value !== validatepassword.value) {
            mostrarError(validatepassword, 'Las contraseñas no coinciden.');
            isValid = false;
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
                            title: '¡Usuario Registrado!',
                            text: 'El usuario se ha guardado con éxito.',
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
                            text: 'Hubo un error al guardar el usuario en el servidor.',
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
        // Si el input está dentro de un input-group, el feedback suele ser hermano del input-group o hijo
        let feedbackDiv = inputElement.nextElementSibling;
        
        // Manejo especial para input-group con el botón de ver contraseña
        if (inputElement.parentElement.classList.contains('input-group')) {
             feedbackDiv = inputElement.parentElement.querySelector('.invalid-feedback');
             if (!feedbackDiv) {
                feedbackDiv = document.createElement('div');
                feedbackDiv.className = 'invalid-feedback';
                inputElement.parentElement.appendChild(feedbackDiv);
             }
        }

        if (feedbackDiv && feedbackDiv.classList.contains('invalid-feedback')) {
            feedbackDiv.textContent = mensaje;
            feedbackDiv.style.display = 'block';
        }
    }
});