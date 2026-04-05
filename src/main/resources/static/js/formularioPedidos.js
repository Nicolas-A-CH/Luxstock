document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('pedidoForm');
    if (form) {
        form.addEventListener('submit', manejarEnvioFormulario);
    }
});

function agregarFila() {
    const template = document.getElementById('filaTemplate').innerHTML;
    const newRow = template.replace(/INDEX/g, index);
    document.getElementById('tbodyDetalles').insertAdjacentHTML('beforeend', newRow);
    index++;
}

function actualizarPrecio(select) {
    const selectedValue = select.value;

    // 1. VALIDACIÓN: Evitar productos duplicados en la tabla
    if (selectedValue) {
        const allSelects = document.querySelectorAll('.producto-select');
        let count = 0;

        allSelects.forEach(s => {
            if (s.value === selectedValue) count++;
        });

        if (count > 1) {
            Swal.fire({
                icon: 'warning',
                title: 'Producto duplicado',
                text: 'Este producto ya ha sido agregado al pedido.',
                confirmButtonColor: '#3085d6'
            });
            select.value = ''; // Reseteamos el select
        }
    }

    // 2. Actualizar el precio si pasó la validación
    const option = select.options[select.selectedIndex];
    // Si reseteamos el select, option no tendrá data-precio, por lo que usamos 0
    const precio = option && select.value !== '' ? parseFloat(option.getAttribute('data-precio')) : 0;

    const row = select.closest('tr');
    row.querySelector('.precio-unitario').value = precio.toFixed(2);
    calcularSubtotal(row.querySelector('.cantidad-input'));
}

function calcularSubtotal(input) {
    const row = input.closest('tr');
    const precio = parseFloat(row.querySelector('.precio-unitario').value) || 0;
    const cantidad = parseFloat(input.value) || 0;
    const subtotal = precio * cantidad;
    row.querySelector('.subtotal').value = subtotal.toFixed(2);
    calcularTotalGeneral();
}

function calcularTotalGeneral() {
    let total = 0;
    document.querySelectorAll('.subtotal').forEach(input => {
        total += parseFloat(input.value) || 0;
    });
    document.getElementById('totalPedido').value = total.toFixed(2);
}

function eliminarFila(btn) {
    const row = btn.closest('tr');
    row.remove();
    calcularTotalGeneral();
}

// Inicializar precios y subtotales si es edición
window.onload = function() {
    document.querySelectorAll('.producto-select').forEach(select => {
        if(select.value) actualizarPrecio(select);
    });
};

async function manejarEnvioFormulario(event) {
    event.preventDefault(); // Evitamos que el formulario recargue la página

    const form = event.target;
    const rows = document.querySelectorAll('#tbodyDetalles tr');
    let isValid = true;
    let errorMessage = '';

    // Validar que haya al menos un producto
    if (rows.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Tabla vacía',
            text: 'Debe agregar al menos un producto al pedido.'
        });
        return;
    }

    // Validar el estado (si el select está presente y habilitado en edición)
    const estadoSelect = form.querySelector('[name="estado"]');
    if (estadoSelect && !estadoSelect.readOnly && !estadoSelect.value) {
        isValid = false;
        errorMessage = 'Debe seleccionar un estado para el pedido.';
        estadoSelect.classList.add('is-invalid');
    } else if (estadoSelect) {
        estadoSelect.classList.remove('is-invalid');
    }

    // Validar cada fila de productos
    rows.forEach((row, idx) => {
        const select = row.querySelector('.producto-select');
        const cantidad = row.querySelector('.cantidad-input');

        // Validar que se haya elegido un producto
        if (!select.value) {
            isValid = false;
            errorMessage = errorMessage || `Seleccione un producto en la fila ${idx + 1}.`;
            select.classList.add('is-invalid'); // Clase de Bootstrap para marcar error visual
        } else {
            select.classList.remove('is-invalid');
        }

        // Validar que la cantidad sea mayor a 0
        if (!cantidad.value || cantidad.value < 1) {
            isValid = false;
            errorMessage = errorMessage || `Ingrese una cantidad válida en la fila ${idx + 1}.`;
            cantidad.classList.add('is-invalid');
        } else {
            cantidad.classList.remove('is-invalid');
        }
    });

    // Si falta algo, detenemos el proceso y mostramos alerta
    if (!isValid) {
        Swal.fire({
            icon: 'warning',
            title: 'Campos incompletos',
            text: errorMessage || 'Por favor, complete todos los campos obligatorios.'
        });
        return;
    }

    // Preparar los datos para el envío
    // Al usar FormData, Spring Boot lo recibe perfectamente con @ModelAttribute
    const formData = new FormData(form);

    try {
        // Hacemos la petición al backend
        const response = await fetch(form.action, {
            method: form.method,
            body: formData
        });

        // El backend devuelve un ResponseEntity<String>, leemos el texto
        const resultText = await response.text();

        if (response.ok) {
            // Código 200 - Éxito
            Swal.fire({
                icon: 'success',
                title: '¡Éxito!',
                text: resultText || 'Pedido guardado correctamente.',
                showConfirmButton: true
            }).then(() => {
                // Redirigir a la vista de pedidos
                window.location.href = '/luxbar/pedidos';
            });
        } else {
            // Código de error (Ej: 500)
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: resultText || 'Hubo un problema al guardar el pedido.'
            });
        }
    } catch (error) {
        console.error("Error en la petición:", error);
        Swal.fire({
            icon: 'error',
            title: 'Error de conexión',
            text: 'Ocurrió un error al comunicarse con el servidor.'
        });
    }
}