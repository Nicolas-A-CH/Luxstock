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

// NUEVA FUNCIÓN: Consulta el stock real al servidor
async function consultarStock(idProducto, idSede, row) {
    const stockInput = row.querySelector('.stock-input');

    if (!idProducto || !idSede) {
        stockInput.value = 0;
        return;
    }

    try {
        // Debes crear este endpoint en tu controlador de Java
        const response = await fetch(`/luxbar/api/productos/stock?idProducto=${idProducto}&idSede=${idSede}`);
        const stock = await response.json();

        stockInput.value = stock;

        // Estilo visual: rojo si no hay stock
        stockInput.style.color = stock <= 0 ? 'red' : 'green';
    } catch (error) {
        console.error("Error consultando stock:", error);
        stockInput.value = 0;
    }
}

async function actualizarPrecio(select) {
    const selectedValue = select.value;
    const idSede = document.getElementById('idSede').value;
    const row = select.closest('tr');

    // 1. VALIDACIÓN: Obligar a seleccionar sede primero
    if (!idSede && selectedValue) {
        Swal.fire({
            icon: 'info',
            title: 'Seleccione una Sede',
            text: 'Debe seleccionar una sede antes de agregar productos para verificar stock.'
        });
        select.value = '';
        return;
    }

    // 2. VALIDACIÓN: Evitar productos duplicados
    if (selectedValue) {
        const allSelects = document.querySelectorAll('.producto-select');
        let count = 0;
        allSelects.forEach(s => { if (s.value === selectedValue) count++; });

        if (count > 1) {
            Swal.fire({
                icon: 'warning',
                title: 'Producto duplicado',
                text: 'Este producto ya ha sido agregado al pedido.',
            });
            select.value = '';
        }
    }

    // 3. Actualizar Precio y Consultar Stock
    const option = select.options[select.selectedIndex];
    const precio = option && select.value !== '' ? parseFloat(option.getAttribute('data-precio')) : 0;

    row.querySelector('.precio-unitario').value = precio.toFixed(2);

    // Llamada a la consulta de stock
    if (selectedValue) {
        await consultarStock(selectedValue, idSede, row);
    }

    calcularSubtotal(row.querySelector('.cantidad-input'));
}

function calcularSubtotal(input) {
    const row = input.closest('tr');
    const precio = parseFloat(row.querySelector('.precio-unitario').value) || 0;
    const cantidad = parseFloat(input.value) || 0;
    const stock = parseFloat(row.querySelector('.stock-input').value) || 0;

    // Validación visual de stock mientras escribe
    if (cantidad > stock) {
        input.classList.add('is-invalid');
    } else {
        input.classList.remove('is-invalid');
    }

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

async function manejarEnvioFormulario(event) {
    event.preventDefault();

    const form = event.target;
    const rows = document.querySelectorAll('#tbodyDetalles tr');
    let isValid = true;
    let errorMessage = '';

    if (rows.length === 0) {
        Swal.fire({ icon: 'error', title: 'Tabla vacía', text: 'Debe agregar al menos un producto.' });
        return;
    }

    // VALIDACIÓN DE STOCK ANTES DE ENVIAR
    rows.forEach((row, idx) => {
        const cantidad = parseInt(row.querySelector('.cantidad-input').value) || 0;
        const stock = parseInt(row.querySelector('.stock-input').value) || 0;

        if (cantidad > stock) {
            isValid = false;
            errorMessage = `En la fila ${idx + 1}, la cantidad (${cantidad}) supera al stock disponible (${stock}).`;
        }
    });

    if (!isValid) {
        Swal.fire({ icon: 'error', title: 'Stock insuficiente', text: errorMessage });
        return;
    }

    // Si todo es válido, proceder con el fetch original
    const formData = new FormData(form);
    try {
        const response = await fetch(form.action, { method: form.method, body: formData });
        const resultText = await response.text();

        if (response.ok) {
            Swal.fire({ icon: 'success', title: '¡Éxito!', text: 'Pedido guardado.' })
                .then(() => window.location.href = '/luxbar/pedidos');
        } else {
            Swal.fire({ icon: 'error', title: 'Error', text: resultText });
        }
    } catch (error) {
        Swal.fire({ icon: 'error', title: 'Error de conexión', text: 'Error al comunicarse con el servidor.' });
    }
}
