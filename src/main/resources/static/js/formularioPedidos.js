function agregarFila() {
    const template = document.getElementById('filaTemplate').innerHTML;
    const newRow = template.replace(/INDEX/g, index);
    document.getElementById('tbodyDetalles').insertAdjacentHTML('beforeend', newRow);
    index++;
}

function actualizarPrecio(select) {
    const option = select.options[select.selectedIndex];
    const precio = option.getAttribute('data-precio') || 0;
    const row = select.closest('tr');
    row.querySelector('.precio-unitario').value = precio;
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