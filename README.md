# 🍬 Dulcería POS - Sistema de Punto de Venta

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-007396?style=for-the-badge&logo=java&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

### Sistema Integral de Gestión para Dulcerías y Tiendas de Conveniencia

*Proyecto Integrador - 6to Cuatrimestre*

[Características](#-características-principales) •
[Instalación](#-instalación) •
[Uso](#-cómo-usar-el-sistema) •
[Tecnologías](#-tecnologías-utilizadas)

</div>

---

## 📋 ¿Qué es este proyecto?

**Dulcería POS** es un sistema completo de punto de venta diseñado específicamente para dulcerías y tiendas de conveniencia. Permite gestionar de manera eficiente todos los aspectos del negocio desde un solo lugar:

- 💰 **Registrar ventas** rápidamente con carrito de compras visual
- 📦 **Controlar inventario** de productos con alertas de stock bajo
- 👥 **Gestionar usuarios** con diferentes niveles de acceso (Administrador y Cajero)
- 📊 **Generar reportes** de ventas por periodo y método de pago
- 🏷️ **Administrar catálogos** de categorías, marcas y proveedores

El sistema está diseñado para ser **intuitivo y fácil de usar**, incluso para personas sin experiencia técnica.

---

## ✨ Características Principales

### 🎯 Para Cajeros
- **Interfaz de Ventas Intuitiva**: Búsqueda rápida de productos con tarjetas visuales
- **Carrito de Compras**: Añade, modifica o elimina productos antes de cobrar
- **Cálculo Automático**: IVA y totales calculados automáticamente
- **Múltiples Métodos de Pago**: Efectivo o Tarjeta
- **Tickets Electrónicos**: Generación automática de comprobantes

### 👨‍💼 Para Administradores
- **Dashboard en Tiempo Real**: Visualiza ventas del día, transacciones y alertas de stock
- **Gestión Completa de Productos**: Alta, baja, modificación con categorías y marcas
- **Control de Usuarios**: Crea cajeros y administradores con contraseñas encriptadas
- **Reportes Personalizados**: Filtra ventas por fechas y método de pago
- **Catálogos Organizados**: Administra categorías, marcas y proveedores desde un solo lugar

### 🔒 Seguridad
- ✅ Contraseñas encriptadas con **BCrypt**
- ✅ Control de acceso por roles (Administrador/Cajero)
- ✅ Validaciones en base de datos con triggers
- ✅ Auditoría de movimientos de inventario

### 🗄️ Base de Datos Avanzada
- **Vistas Optimizadas**: Para consultas rápidas y eficientes
- **Procedimientos Almacenados**: Transacciones seguras y atómicas
- **Triggers Automáticos**: Control de stock y validaciones en tiempo real
- **Auditoría Completa**: Registro de todos los movimientos de inventario

---

## 🚀 Instalación

### Requisitos Previos

Antes de instalar, asegúrate de tener:

- ☕ **Java JDK 17 o superior** ([Descargar aquí](https://www.oracle.com/java/technologies/downloads/))
- 🗄️ **MariaDB 10.11 o superior** ([Descargar aquí](https://mariadb.org/download/))
- 📦 **Maven** (usualmente incluido con tu IDE)
- 💻 Un IDE como **IntelliJ IDEA** o **NetBeans**

### Paso 1: Clonar el Repositorio
```bash
git clone https://github.com/TU-USUARIO/dulceria-pos.git
cd dulceria-pos
```

### Paso 2: Configurar la Base de Datos

1. **Inicia tu servidor MariaDB**

2. **Abre tu cliente de MariaDB** (HeidiSQL, DBeaver, o línea de comandos)

3. **Crea un nuevo usuario** (recomendado):
```sql
CREATE USER 'vic'@'%' IDENTIFIED BY 'vic123';
GRANT ALL PRIVILEGES ON dulceria.* TO 'vic'@'%';
FLUSH PRIVILEGES;
```

4. **Importa la base de datos**:
```bash
mysql -u vic -p < respaldoCompleto.sql
```

> 💡 **Nota**: El archivo `respaldoCompleto.sql` está en la raíz del proyecto y contiene toda la estructura de la base de datos, incluyendo vistas, procedimientos almacenados y triggers.

### Paso 3: Configurar la Conexión

Abre el archivo `src/main/java/com/dulceria/pos/util/Conexion.java` y actualiza las credenciales:
```java
private final static String url = "jdbc:mariadb://localhost/dulceria";
private final static String user = "vic";           // Tu usuario de BD
private final static String password = "vic123";     // Tu contraseña de BD
```

> ⚠️ **Importante**: Si tu servidor MariaDB está en una IP diferente a localhost, cambia `localhost` por la IP correspondiente.

### Paso 4: Compilar y Ejecutar

**Desde tu IDE:**
1. Abre el proyecto en IntelliJ IDEA o NetBeans
2. Espera a que Maven descargue las dependencias
3. Ejecuta la clase `Main.java`

**Desde línea de comandos:**
```bash
mvn clean install
mvn javafx:run
```

---

## 🎮 Cómo Usar el Sistema

### 🔐 Inicio de Sesión

Al iniciar la aplicación, verás la pantalla de login:

**Credenciales de Prueba:**

| Usuario | Contraseña | Rol | Acceso |
|---------|------------|-----|--------|
| `admin` | `admin123` | Administrador | Acceso completo a todos los módulos |
| `Cajero1` | `cajero123` | Cajero | Solo módulo de ventas |

### 📱 Navegación Principal

Una vez dentro, verás un menú lateral con las siguientes opciones:

#### 🏠 Inicio (Dashboard)
- **Ventas Totales del Día**: Monto total generado hoy
- **Número de Transacciones**: Cantidad de tickets emitidos
- **Alertas de Stock**: Productos con menos de 10 unidades
- **Gráfica de Ventas**: Ventas por categoría en tiempo real

💡 *Tip: Haz clic en el número de alertas de stock para ver directamente los productos que necesitan reabastecerse*

#### 🛒 Ventas (Para Cajeros)

**Proceso de Venta:**

1. **Buscar Productos**: 
   - Escribe el nombre del producto en la barra de búsqueda
   - Los resultados se actualizan en tiempo real

2. **Añadir al Carrito**:
   - Haz clic en el botón "➕ Agregar" de cada producto
   - Los productos se añaden al ticket en el panel derecho

3. **Revisar el Ticket**:
   - Verifica los productos, cantidades y precios
   - El subtotal, IVA y total se calculan automáticamente

4. **Cobrar**:
   - Haz clic en el botón verde "COBRAR"
   - Selecciona el método de pago (Efectivo o Tarjeta)
   - ¡Venta completada! El stock se actualiza automáticamente

**Otras Opciones:**
- **Quitar Item**: Selecciona un producto del ticket y clic en "Quitar Item"
- **Cancelar Venta**: Botón rojo para vaciar todo el carrito

#### 📦 Productos (Solo Administradores)

**Agregar un Nuevo Producto:**

1. Completa el formulario de la izquierda:
   - Nombre del producto
   - Categoría (selecciona del menú desplegable)
   - Marca (selecciona del menú desplegable)
   - Stock inicial
   - Unidad de medida (PZA, CAJA, PQTE, BOLSA)
   - Precio de venta
   - Marca si está activo

2. Clic en "Guardar" (botón verde)

**Editar un Producto:**

1. Haz clic en cualquier producto de la tabla
2. Los datos se cargarán en el formulario
3. Modifica lo que necesites
4. Clic en "Guardar"

**Buscar y Filtrar:**
- **Por Nombre**: Escribe en el campo de búsqueda
- **Por Categoría**: Selecciona una categoría del filtro
- Ambos filtros funcionan simultáneamente

#### 👥 Usuarios (Solo Administradores)

**Crear un Nuevo Usuario:**

1. Llena el formulario:
   - Nombre Completo
   - Usuario (para login)
   - Contraseña
   - Selecciona el Rol (Administrador o Cajero)
   - Marca "Usuario Activo"

2. Clic en "Guardar"

> 🔒 Las contraseñas se encriptan automáticamente antes de guardarse en la base de datos

**Editar un Usuario:**

1. Haz clic en un usuario de la tabla
2. Modifica los datos necesarios
3. **Para cambiar contraseña**: escribe una nueva
4. **Para mantener la contraseña**: deja el campo vacío
5. Clic en "Guardar"

#### 📊 Reportes (Solo Administradores)

**Generar Reporte de Ventas:**

1. Selecciona **Fecha Inicial** y **Fecha Final**
2. Elige el tipo de reporte:
   - Ventas Generales
   - Ventas por Efectivo
   - Ventas por Tarjeta
3. Clic en "Generar Reporte"

La tabla mostrará todas las ventas del periodo con:
- Fecha y hora de la venta
- Número de ticket
- Nombre del cajero
- Método de pago
- Total de la venta

💰 El total del periodo se muestra en la parte inferior

#### 📝 Catálogos (Solo Administradores)

Gestiona las categorías, marcas y proveedores desde pestañas separadas:

**Para Crear:**
1. Ve a la pestaña correspondiente
2. Llena el nombre
3. Marca si está activo
4. Clic en "Guardar"

**Para Editar:**
1. Haz clic en un elemento de la tabla
2. Modifica el nombre o estado
3. Clic en "Guardar"

> ⚠️ **Nota sobre categorías inactivas**: Si desactivas una categoría, los productos de esa categoría ya no aparecerán en el módulo de ventas, pero se conservarán en el sistema para reportes históricos.

---

## 🏗️ Estructura del Proyecto
```
dulceria-pos/
│
├── src/main/java/com/dulceria/pos/
│   ├── controlador/          # Controladores de JavaFX
│   │   ├── LoginController.java
│   │   ├── PrincipalController.java
│   │   ├── VentasController.java
│   │   ├── ProductosController.java
│   │   ├── UsuariosController.java
│   │   ├── ReportesController.java
│   │   ├── CatalogosController.java
│   │   └── InicioController.java
│   │
│   ├── DAO/                  # Data Access Objects
│   │   ├── UsuarioDAO.java
│   │   ├── ProductoDAO.java
│   │   ├── VentaDAO.java
│   │   ├── CategoriaDAO.java
│   │   ├── MarcaDAO.java
│   │   └── ProveedorDAO.java
│   │
│   ├── modelo/               # Modelos (POJOs)
│   │   ├── Usuario.java
│   │   ├── Producto.java
│   │   ├── Venta.java
│   │   ├── DetalleVenta.java
│   │   ├── Categoria.java
│   │   ├── Marca.java
│   │   └── Proveedor.java
│   │
│   ├── util/                 # Utilidades
│   │   └── Conexion.java
│   │
│   └── Main.java             # Punto de entrada
│
├── src/main/resources/
│   ├── com/dulceria/pos/     # Archivos FXML
│   │   ├── Login.fxml
│   │   ├── Principal.fxml
│   │   ├── Ventas.fxml
│   │   ├── Productos.fxml
│   │   └── ...
│   │
│   └── imagenes/             # Recursos gráficos
│       ├── iconos/
│       └── productos/
│
├── respaldoCompleto.sql      # Base de datos completa
├── pom.xml                   # Configuración Maven
└── README.md                 # Este archivo
```

---

## 🛠️ Tecnologías Utilizadas

### Frontend
- **JavaFX 25**: Framework para interfaces gráficas modernas
- **FXML**: Diseño declarativo de interfaces
- **CSS**: Estilos personalizados para la UI

### Backend
- **Java 17**: Lenguaje de programación principal
- **Programación Orientada a Objetos**: Arquitectura limpia y escalable
- **Patrón MVC**: Separación de responsabilidades
- **Patrón DAO**: Abstracción de acceso a datos

### Base de Datos
- **MariaDB 10.11**: Sistema de gestión de bases de datos
- **Vistas SQL**: Para consultas optimizadas
- **Procedimientos Almacenados**: Operaciones complejas en la BD
- **Triggers**: Validaciones y auditoría automática

### Seguridad
- **BCrypt**: Encriptación de contraseñas
- **Control de Acceso por Roles**: Administrador vs Cajero

### Herramientas de Desarrollo
- **Maven**: Gestión de dependencias
- **Git**: Control de versiones
- **IntelliJ IDEA**: IDE recomendado

---

## 📊 Modelo de Base de Datos

El sistema cuenta con **9 tablas principales** relacionadas:
```
┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│   Usuarios  │────<│    Ventas    │>────│ Detalle_    │
│             │     │              │     │   Ventas    │
└─────────────┘     └──────────────┘     └─────────────┘
       │                                         │
       │                                         │
┌─────────────┐                          ┌─────────────┐
│    Roles    │                          │  Productos  │
└─────────────┘                          └─────────────┘
                                                │
                        ┌───────────────────────┼───────────────────────┐
                        │                       │                       │
                 ┌─────────────┐         ┌─────────────┐       ┌─────────────┐
                 │ Categorías  │         │   Marcas    │       │ Proveedores │
                 └─────────────┘         └─────────────┘       └─────────────┘
```

**Tablas adicionales:**
- `Stock_Movimientos`: Auditoría de cambios en inventario

**Vistas:**
- `vista_productos`: Productos con nombres de categoría y marca
- `v_ventas_por_categoria_hoy`: Resumen de ventas por categoría
- `v_ventas_resumen_dia`: Totales de ventas por día

---

## 🎓 Características Académicas

Este proyecto cumple con los requerimientos del **Proyecto Integrador de 6to Cuatrimestre**:

### ✅ Requisitos Cumplidos

- [x] Modelo entidad-relación complejo (9 entidades)
- [x] Normalización hasta 3FN
- [x] Implementación de procedimientos almacenados
- [x] Implementación de triggers para validación y auditoría
- [x] Vistas para optimización de consultas
- [x] Gestión de usuarios y privilegios en BD
- [x] Interfaz gráfica elaborada en JavaFX
- [x] Operaciones CRUD completas
- [x] Módulo de reportes con filtros
- [x] Sistema de login con roles
- [x] Documentación técnica completa

### 📚 Conceptos Aplicados

**Programación Orientada a Objetos:**
- Encapsulamiento (getters/setters, clases POJO)
- Herencia (jerarquía de controladores)
- Polimorfismo (interfaces DAO)
- Abstracción (separación de capas)

**Patrones de Diseño:**
- MVC (Modelo-Vista-Controlador)
- DAO (Data Access Object)
- Singleton (Conexión a BD)

**Base de Datos Avanzada:**
- Normalización
- Integridad referencial
- Transacciones ACID
- Procedimientos almacenados
- Triggers
- Vistas materializadas

---

## 🐛 Solución de Problemas

### ❌ Error: "No se puede conectar al servidor"

**Solución:**
1. Verifica que MariaDB esté corriendo
2. Revisa las credenciales en `Conexion.java`
3. Asegúrate de que el puerto 3306 esté disponible

### ❌ Error: "Table doesn't exist"

**Solución:**
1. Asegúrate de haber importado `respaldoCompleto.sql`
2. Verifica que la base de datos se llame `dulceria`
3. Ejecuta: `SHOW TABLES;` para confirmar

### ❌ Error al compilar: "Package javafx does not exist"

**Solución:**
1. Verifica que Maven haya descargado las dependencias
2. En tu IDE, ejecuta: `Maven → Reload Project`
3. Limpia y recompila: `mvn clean install`

### ❌ Las imágenes de productos no se ven

**Solución:**
Las imágenes son opcionales. El sistema funciona sin ellas. Si quieres añadirlas:
1. Coloca imágenes en `src/main/resources/imagenes/productos/`
2. Nómbralas con el ID del producto (ej: `1.png`, `2.jpg`)
3. O usa el nombre de la categoría (ej: `chocolates.png`)

---

## 📞 Contacto y Soporte

**Autor**: Victor Manuel Ramirez Mendoza

Si tienes dudas o sugerencias:
- 📧 Email: victorman.rmz14@gmail.com 
- 💼 GitHub: 

---

## 📜 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

---

## 🙏 Agradecimientos

- A mis compañeras de equipo por contribuir con datos de prueba 
- A los profesores por la guía en el desarrollo

---

<div align="center">

### ⭐ Si este proyecto te fue útil, considera darle una estrella en GitHub

**Hecho con ❤️ y mucho ☕ por el equipo de desarrollo**

</div>

---

## 🔄 Actualizaciones Recientes

### v1.0.0 (Diciembre 2025)
- ✅ Sistema completo de ventas con carrito
- ✅ Dashboard con estadísticas en tiempo real
- ✅ Gestión completa de productos, usuarios y catálogos
- ✅ Reportes por periodo y método de pago
- ✅ Base de datos con procedimientos almacenados y triggers
- ✅ Encriptación de contraseñas con BCrypt
- ✅ Control de stock automático
- ✅ Interfaz responsive y moderna

---

> 💡 **Tip Final**: Explora cada módulo del sistema haciendo clic en los botones del menú lateral. ¡El sistema es intuitivo y fácil de usar!
