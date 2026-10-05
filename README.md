# SIGTAL - GA7-220501096-AA2-EV02

## Módulos de software codificados y probados

**Proyecto:** SIGTAL - Sistema Integral de Gestión para Taller Automotriz Londoño

**Evidencia:** GA7-220501096-AA2-EV02 - Módulos de software codificados y probados

## Descripción

Este proyecto corresponde al desarrollo del módulo de Clientes y Vehículos del sistema SIGTAL.

El módulo fue desarrollado como una aplicación web utilizando Java Servlets, JSP, JDBC y MySQL, de acuerdo con los requerimientos establecidos para la evidencia GA7-220501096-AA2-EV02.

## Funcionalidades

### Clientes
- Registrar clientes.
- Listar clientes.
- Editar clientes.
- Actualizar clientes.
- Eliminar clientes.
- Validar documentos duplicados.
- Validar teléfonos duplicados.
- Validar correos electrónicos duplicados.

### Vehículos

- Registrar vehículos.
- Listar vehículos.
- Consultar vehículos asociados a un cliente.
- Editar vehículos.
- Actualizar vehículos.
- Eliminar vehículos.
- Validar placas duplicadas.

## Tecnologías

- Java 21
- Java Servlets
- JSP
- JDBC
- MySQL / MariaDB
- Apache Tomcat 10.1
- Maven
- HTML5
- CSS
- JavaScript
- Git
- GitHub
- NetBeans

## Métodos HTTP

El sistema utiliza:

- GET para consultas, listados, edición y eliminación.
- POST para registro y actualización de información.

## Base de datos

Base de datos utilizada:

sigtal

Tablas principales:

- clientes
- vehiculos

## Pruebas realizadas

Se realizaron pruebas de:

- Acceso al módulo de clientes.
- Acceso al módulo de vehículos.
- Registro de clientes.
- Validación de documento duplicado.
- Validación de teléfono duplicado.
- Validación de correo duplicado.
- Actualización de clientes.
- Validación de documento duplicado durante actualización.
- Registro de vehículos.
- Validación de placa duplicada.
- Actualización de vehículos.
- Eliminación de vehículos.
- Eliminación de clientes.
- Verificación de registros directamente en MySQL.

## Construcción

Para compilar el proyecto:

mvn clean package

El resultado esperado es:

BUILD SUCCESS

El archivo WAR generado se encuentra en:

target/MODULO-CLIENTES-VEHICULOS.war

## Repositorio GitHub

https://github.com/MauricioPolo306/SIGTAL-GA7-AA2-EV02.git

## Evidencia

GA7-220501096-AA2-EV02 - Módulos de software codificados y probados

## Autor

Mauricio Jose Polo Acosta

SENA
