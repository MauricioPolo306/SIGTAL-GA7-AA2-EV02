============================================================
SIGTAL - SISTEMA INTEGRAL DE GESTIÓN PARA TALLER AUTOMOTRIZ
MÓDULO CLIENTES Y VEHÍCULOS
============================================================

Evidencia:
GA7-220501096-AA3-EV01
Codificación de módulos del software

Proyecto:
Sistema Integral de Gestión para Taller Automotriz Londoño

Tecnología:
Java Web - JSP - Servlets - Maven - MariaDB

============================================================
1. DESCRIPCIÓN DEL PROYECTO
============================================================

SIGTAL es un sistema web desarrollado para apoyar la gestión
administrativa y operativa de un taller automotriz.

Para la presente evidencia se seleccionó el módulo:

CLIENTES Y VEHÍCULOS

Este módulo permite gestionar la información de los clientes
y los vehículos asociados a cada cliente.

============================================================
2. FUNCIONALIDADES DEL MÓDULO
============================================================

El módulo permite:

- Registrar clientes.
- Consultar clientes.
- Actualizar clientes.
- Eliminar clientes.
- Registrar vehículos.
- Consultar vehículos.
- Actualizar vehículos.
- Eliminar vehículos.
- Asociar vehículos con clientes.
- Validar documentos duplicados.
- Validar teléfonos duplicados.
- Validar correos electrónicos duplicados.
- Validar placas de vehículos duplicadas.
- Visualizar mensajes de confirmación y error.

============================================================
3. TECNOLOGÍAS UTILIZADAS
============================================================

- Java JDK 21.
- Java Servlets.
- JSP (JavaServer Pages).
- Maven.
- MariaDB.
- HTML5.
- CSS3.
- JavaScript.
- Apache Tomcat 8.5.
- NetBeans.
- Git y GitHub.

============================================================
4. ESTRUCTURA DEL PROYECTO
============================================================

MODULO_CLIENTES_VEHICULOS/
│
├── pom.xml
│
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── mycompany/
        │           └── sigtal/
        │               ├── controlador/
        │               │   ├── ClienteServlet.java
        │               │   └── VehiculoServlet.java
        │               │
        │               ├── dao/
        │               │   ├── ClienteDAO.java
        │               │   └── VehiculoDAO.java
        │               │
        │               ├── modelo/
        │               │   ├── Cliente.java
        │               │   └── Vehiculo.java
        │               │
        │               └── util/
        │                   └── ConexionBD.java
        │
        └── webapp/
            ├── clientes.jsp
            ├── index.jsp
            ├── META-INF/
            └── WEB-INF/

============================================================
5. BASE DE DATOS
============================================================

El módulo utiliza MariaDB como sistema gestor de base de datos.

El script de creación se encuentra en:

BaseDatos/clientes_vehiculos.sql

Para instalar la base de datos:

1. Abrir XAMPP.

2. Iniciar los servicios:
   - Apache
   - MySQL

3. Abrir phpMyAdmin mediante el botón "Admin" de MySQL.

4. Seleccionar la opción "Importar".

5. Seleccionar el archivo:

   BaseDatos/clientes_vehiculos.sql

6. Ejecutar la importación.

El script crea la base de datos:

   sigtal

y las tablas:

   clientes
   vehiculos

La relación entre las tablas permite asociar uno o varios
vehículos con un cliente.

============================================================
6. CONFIGURACIÓN DE LA CONEXIÓN
============================================================

La conexión utilizada por el módulo es:

Motor:
MariaDB

Host:
localhost

Puerto:
3306

Base de datos:
sigtal

Usuario:
root

Contraseña:
vacía

URL JDBC:

jdbc:mariadb://localhost:3306/sigtal

La configuración se encuentra en:

src/main/java/com/mycompany/sigtal/útil/ConexionBD.java

Código de conexión utilizado:

private static final String URL =
    "jdbc:mariadb://localhost:3306/sigtal";

private static final String USUARIO = "root";

private static final String CONTRASENA = "";

Si la instalación local de MariaDB utiliza otros parámetros
de conexión, se debe modificar la clase ConexionBD.java.

============================================================
7. EJECUCIÓN DEL PROYECTO
============================================================

Para ejecutar el módulo:

1. Abrir el proyecto en NetBeans.

2. Verificar que esté configurado el JDK 21.

3. Verificar la conexión con MariaDB.

4. Verificar que Apache Tomcat 8.5 esté configurado.

5. Ejecutar el proyecto mediante NetBeans.

6. Acceder al módulo Clientes y Vehículos desde el navegador.

El módulo puede utilizarse para realizar las operaciones
CRUD sobre clientes y vehículos.

============================================================
8. ARQUITECTURA
============================================================

El módulo utiliza una arquitectura organizada por capas:

MODELO
- Cliente.java
- Vehiculo.java

CONTROLADOR
- ClienteServlet.java
- VehiculoServlet.java

DAO
- ClienteDAO.java
- VehiculoDAO.java

VISTA
- clientes.jsp

UTILIDAD
- ConexionBD.java

La separación de responsabilidades facilita el mantenimiento,
organización y evolución del software.

============================================================
9. VALIDACIONES
============================================================

El módulo incorpora validaciones para evitar información
duplicada.

Clientes:

- Documento único.
- Teléfono único.
- Correo electrónico único.

Vehículos:

- Placa única.

Cuando se intenta registrar información duplicada, el sistema
muestra un mensaje específico al usuario.

============================================================
10. CONTROL DE VERSIONES
============================================================

El código fuente del módulo se encuentra almacenado mediante
Git y GitHub.

Repositorio:

https://github.com/MauricioPolo306/SIGTAL-Modulo-Clientes-Vehiculos

Rama principal:

main

Primer commit:

Primera version modulo Clientes y Vehiculos

============================================================
11. EVIDENCIA
============================================================

Esta implementación corresponde a:

GA7-220501096-AA3-EV01

Codificación de módulos del software Stand alone, web y móvil
de acuerdo con el proyecto a desarrollar.

Módulo seleccionado:

Clientes y Vehículos

============================================================
12. AUTOR
============================================================

Autor:
Mauricio Jose Polo Acosta

Proyecto:
SIGTAL - Sistema Integral de Gestión para Taller Automotriz
Electricista Londoño

============================================================
FIN DEL README
============================================================