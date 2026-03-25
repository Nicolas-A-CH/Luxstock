# Mapeo de Base de Datos - Sistema de Gestión de Bar
## Tecnologías Utilizadas

* **Java** * **JPA / Hibernate:** Para el mapeo objeto-relacional (ORM).
* **Lombok:** Para reducir el código repetitivo (boilerplate) como Getters, Setters y constructores.
* **Jakarta Persistence:** Especificación estándar para las anotaciones de base de datos (`jakarta.persistence.*`).
* springboot v 4
* java v 17
* admin lte

---
## Estructura base de datos

Este seccion del documento explica la estructura y las decisiones de diseño tomadas para el mapeo de la base de datos del sistema de gestión de bar, utilizando **JPA (Jakarta Persistence API)** y **Lombok**.

### Estructura de Entidades y Relaciones

El modelo de datos está dividido lógicamente en tres módulos principales:

#### 1. Gestión de Personal y Acceso
* **`Sede`** (`sedes`): Representa las sucursales del bar. Entidad independiente.
* **`Rol`** (`roles`): Define los niveles de acceso (Administrador, Cajero, Mesero). Entidad independiente.
* **`Empleado`** (`empleados`): Contiene la información del personal.
    * Relación **ManyToOne** con `Rol`.
    * Relación **ManyToOne** con `Sede`.
* **`Usuario`** (`usuarios`): Credenciales de acceso al sistema.
    * Relación **OneToOne** con `Empleado`.

#### 2. Gestión de Productos e Inventario
* **`Producto`** (`productos`): Catálogo de bebidas, comidas, etc. Entidad independiente.
* **`Inventario`** (`inventario`): Control de stock por sede.
    * Relación **ManyToOne** con `Producto`.
    * Relación **ManyToOne** con `Sede`.

#### 3. Operaciones (Pedidos y Ventas)
* **`Pedido`** y **`DetallePedido`**: Gestión de las órdenes tomadas por los meseros.
    * Relaciones **ManyToOne** hacia `Empleado`, `Sede`, `Pedido` y `Producto`.
* **`Venta`** y **`DetalleVenta`**: Registro de facturación en caja.
    * Relaciones **ManyToOne** hacia `Empleado`, `Sede`, `Venta` y `Producto`.
