$(document).ready(function () {
    const table = $('#tablaVentas').DataTable({
        "responsive": true,
        "lengthChange": false,
        "autoWidth": false
    }).buttons().container().appendTo('#tablaVentas_wrapper .col-md-6:eq(0)');

    const rolUsuario = $('#usuarioRol').val();
    const sedeUsuarioId = $('#usuarioSedeId').val();

    // Aplicar filtro inicial por sede si no es administrador
    if (rolUsuario !== 'ADMINISTRADOR' && sedeUsuarioId) {
        $.fn.dataTable.ext.search.push(
            function (settings, data, dataIndex) {
                const sedeIdFila = data[5]; // Índice de la columna oculta SedeId (ahora es 5 por la nueva columna Estado)
                return sedeIdFila === sedeUsuarioId;
            }
        );
        $('#tablaVentas').DataTable().draw();
    }

    $('#btnFiltrar').on('click', function () {
        const fechaInicio = $('#fechaInicio').val();
        const fechaFin = $('#fechaFin').val();
        const sedeSeleccionada = $('#filtroSede').val();

        // Limpiar filtros personalizados anteriores
        $.fn.dataTable.ext.search.length = 0;

        // Añadir filtro por sede para no administradores siempre
        if (rolUsuario !== 'ADMINISTRADOR' && sedeUsuarioId) {
            $.fn.dataTable.ext.search.push(
                function (settings, data, dataIndex) {
                    return data[5] === sedeUsuarioId;
                }
            );
        }

        // Añadir nuevos filtros
        $.fn.dataTable.ext.search.push(
            function (settings, data, dataIndex) {
                const fechaFila = data[1].split(' ')[0]; // Tomar solo la parte de la fecha (yyyy-MM-dd)
                const sedeIdFila = data[5];

                // Filtro de Sede (para administradores)
                if (rolUsuario === 'ADMINISTRADOR' && sedeSeleccionada && sedeIdFila !== sedeSeleccionada) {
                    return false;
                }

                // Filtro de Rango de Fechas
                if (fechaInicio && fechaFila < fechaInicio) {
                    return false;
                }
                if (fechaFin && fechaFila > fechaFin) {
                    return false;
                }

                return true;
            }
        );

        $('#tablaVentas').DataTable().draw();
    });

    $('#btnLimpiar').on('click', function () {
        $('#fechaInicio').val('');
        $('#fechaFin').val('');
        $('#filtroSede').val('');

        // Mantener el filtro de sede para no administradores
        $.fn.dataTable.ext.search.length = 0;
        if (rolUsuario !== 'ADMINISTRADOR' && sedeUsuarioId) {
            $.fn.dataTable.ext.search.push(
                function (settings, data, dataIndex) {
                    return data[5] === sedeUsuarioId;
                }
            );
        }

        $('#tablaVentas').DataTable().draw();
    });
});
