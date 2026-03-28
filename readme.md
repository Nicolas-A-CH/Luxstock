# Mapeo de Base de Datos - Sistema de Gestión de Bar

## Tecnologías Utilizadas

* **Java 17**
* **Spring Boot 3+**
* **JPA / Hibernate:** Para el mapeo objeto-relacional (ORM).
* **Lombok:** Para reducir el código repetitivo (boilerplate).
* **Jakarta Persistence:** Especificación estándar para las anotaciones de base de datos.
* **Thymeleaf:** Motor de plantillas para el frontend.
* **AdminLTE 3:** Plantilla base para el diseño del panel administrativo.
* **Webjars:** Gestión de librerías cliente (Bootstrap, jQuery, FontAwesome, SweetAlert2).

---

## Estructura de Base de Datos

Este sección del documento explica la estructura y las decisiones de diseño tomadas para el mapeo de la base de datos del sistema de gestión de bar.

### Estructura de Entidades y Relaciones

El modelo de datos está dividido lógicamente en tres módulos principales:

#### 1. Gestión de Personal y Acceso
* **`Sede`** (`sedes`): Representa las sucursales del bar.
* **`Rol`** (`roles`): Define los niveles de acceso (Administrador, Cajero, Mesero).
* **`Empleado`** (`empleados`): Contiene la información del personal.
* **`Usuario`** (`usuarios`): Credenciales de acceso al sistema.

#### 2. Gestión de Productos e Inventario
* **`Producto`** (`productos`): Catálogo de bebidas, comidas, etc.
* **`Inventario`** (`inventario`): Control de stock por sede.

#### 3. Operaciones (Pedidos y Ventas)
* **`Pedido`** y **`DetallePedido`**: Gestión de las órdenes.
* **`Venta`** y **`DetalleVenta`**: Registro de facturación en caja.

---

## Configuración Inicial del Sistema

### Creación del Usuario Administrador

Para que el sistema sea funcional por primera vez, se requiere la existencia de al menos un Rol de Administrador, una Sede y un Usuario con dichos privilegios. Debido a que las contraseñas están encriptadas con **BCrypt**, no se recomienda insertarlas manualmente vía SQL plano sin la función de encriptación adecuada.

Sigue estos pasos para la configuración inicial:

1. **Roles y Sedes**: Asegúrate de que las tablas `roles` y `sedes` tengan al menos un registro.
   ```sql
   INSERT INTO roles (nombre) VALUES ('ADMINISTRADOR');
   INSERT INTO sedes (nombre, direccion) VALUES ('Sede Principal', 'Calle Falsa 123');
   ```

2. **Registro vía Interfaz**: Una vez que el sistema esté corriendo, utiliza el formulario de **Registro de Usuarios** (`/luxbar/crear usuario`) para crear el primer administrador. El sistema se encargará de:
   - Validar que el documento (CC) tenga entre 5 y 10 dígitos.
   - Encriptar la contraseña automáticamente.
   - Vincular al empleado con su usuario y sede correspondientes.

3. **Seguridad**: El nombre de usuario y la contraseña definidos en este paso serán los que se utilicen para el acceso inicial al panel administrativo.

---

## Implementación de Interfaz (Thymeleaf + AdminLTE)

Para mantener la consistencia visual y facilitar el mantenimiento, se utiliza un sistema de fragmentos con Thymeleaf localizado en `src/main/resources/templates/fragments/layout.html`.

### Fragmentos Disponibles

1.  **`head`**: Incluye todos los estilos CSS necesarios (AdminLTE, Bootstrap, FontAwesome, SweetAlert2).
2.  **`menu`**: Define la barra lateral (sidebar) con la navegación principal.
3.  **`scripts`**: Incluye las librerías JavaScript requeridas al final del cuerpo (jQuery, Bootstrap Bundle, SweetAlert2, AdminLTE JS).

### Cómo usar la plantilla en una nueva página

Para crear una nueva vista utilizando la estructura base de AdminLTE, sigue este esquema:

```html
<!DOCTYPE html>
<html lang="es" xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{fragments/layout :: head}">
    <!-- Puedes añadir estilos adicionales aquí si es necesario -->
</head>
<body class="hold-transition sidebar-mini">
    <div class="wrapper">
        <!-- Sidebar -->
        <aside th:replace="~{fragments/layout :: menu}"></aside>

        <!-- Contenido Principal -->
        <div class="content-wrapper">
            <section class="content-header">
                <h1>Título de la Página</h1>
            </section>
            <section class="content">
                <div class="container-fluid">
                    <!-- Tu contenido aquí -->
                </div>
            </section>
        </div>
    </div>

    <!-- Scripts -->
    <th:block th:replace="~{fragments/layout :: scripts}"></th:block>
    <!-- Tus scripts específicos aquí -->
</body>
</html>
```

### Librerías Incluidas (vía Webjars)

Las siguientes dependencias están configuradas en el proyecto y se cargan automáticamente a través del fragmento `head` y `scripts`:

*   **AdminLTE 3.2.0**: Framework de UI.
*   **FontAwesome 6.4.0**: Iconos.
*   **Bootstrap 5.3.8**: Layout y componentes.
*   **SweetAlert2 11.26.17**: Notificaciones y alertas modales.
*   **jQuery 3.6.4**: Requerido por AdminLTE y plugins.
