package com.mycompany.sigtal.dao;

import com.mycompany.sigtal.modelo.Cliente;
import com.mycompany.sigtal.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    /**
     * Registra un nuevo cliente.
     *
     * @return
     *  OK = registro exitoso
     *  DOCUMENTO_DUPLICADO = documento ya registrado
     *  TELEFONO_DUPLICADO = teléfono ya registrado
     *  CORREO_DUPLICADO = correo ya registrado
     *  ERROR = otro error de base de datos
     */
    public String registrar(Cliente cliente) {

        String sql = """
                INSERT INTO clientes
                (nombre, documento, telefono, correo, direccion)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, cliente.getNombre());
            sentencia.setString(2, cliente.getDocumento());
            sentencia.setString(3, cliente.getTelefono());
            sentencia.setString(4, cliente.getCorreo());
            sentencia.setString(5, cliente.getDireccion());

            int resultado = sentencia.executeUpdate();

            return resultado > 0 ? "OK" : "ERROR";

        } catch (SQLException e) {

            if (esDuplicado(e)) {
                return identificarDuplicado(cliente);
            }

            System.err.println("Error al registrar cliente: " + e.getMessage());
            return "ERROR";
        }
    }

    /**
     * Identifica qué dato está causando el duplicado.
     */
    private String identificarDuplicado(Cliente cliente) {

        if (existeDocumento(cliente.getDocumento(), 0)) {
            return "DOCUMENTO_DUPLICADO";
        }

        if (cliente.getTelefono() != null
                && !cliente.getTelefono().trim().isEmpty()
                && existeTelefono(cliente.getTelefono(), 0)) {

            return "TELEFONO_DUPLICADO";
        }

        if (cliente.getCorreo() != null
                && !cliente.getCorreo().trim().isEmpty()
                && existeCorreo(cliente.getCorreo(), 0)) {

            return "CORREO_DUPLICADO";
        }

        return "ERROR";
    }

    /**
     * Comprueba si un documento ya existe.
     *
     * @param documento documento a comprobar
     * @param idClienteExcluir ID que se debe ignorar al editar.
     */
    public boolean existeDocumento(String documento, int idClienteExcluir) {

        String sql;

        if (idClienteExcluir > 0) {
            sql = """
                    SELECT id_cliente
                    FROM clientes
                    WHERE documento = ?
                    AND id_cliente <> ?
                    LIMIT 1
                    """;
        } else {
            sql = """
                    SELECT id_cliente
                    FROM clientes
                    WHERE documento = ?
                    LIMIT 1
                    """;
        }

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, documento);

            if (idClienteExcluir > 0) {
                sentencia.setInt(2, idClienteExcluir);
            }

            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next();
            }

        } catch (SQLException e) {
            System.err.println("Error al comprobar documento: " + e.getMessage());
            return false;
        }
    }

    /**
     * Comprueba si un teléfono ya existe.
     */
    public boolean existeTelefono(String telefono, int idClienteExcluir) {

        String sql;

        if (idClienteExcluir > 0) {
            sql = """
                    SELECT id_cliente
                    FROM clientes
                    WHERE telefono = ?
                    AND id_cliente <> ?
                    LIMIT 1
                    """;
        } else {
            sql = """
                    SELECT id_cliente
                    FROM clientes
                    WHERE telefono = ?
                    LIMIT 1
                    """;
        }

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, telefono);

            if (idClienteExcluir > 0) {
                sentencia.setInt(2, idClienteExcluir);
            }

            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next();
            }

        } catch (SQLException e) {
            System.err.println("Error al comprobar teléfono: " + e.getMessage());
            return false;
        }
    }

    /**
     * Comprueba si un correo ya existe.
     */
    public boolean existeCorreo(String correo, int idClienteExcluir) {

        String sql;

        if (idClienteExcluir > 0) {
            sql = """
                    SELECT id_cliente
                    FROM clientes
                    WHERE correo = ?
                    AND id_cliente <> ?
                    LIMIT 1
                    """;
        } else {
            sql = """
                    SELECT id_cliente
                    FROM clientes
                    WHERE correo = ?
                    LIMIT 1
                    """;
        }

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, correo);

            if (idClienteExcluir > 0) {
                sentencia.setInt(2, idClienteExcluir);
            }

            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next();
            }

        } catch (SQLException e) {
            System.err.println("Error al comprobar correo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todos los clientes.
     */
    public List<Cliente> listar() {

        List<Cliente> clientes = new ArrayList<>();

        String sql = """
                SELECT id_cliente, nombre, documento,
                       telefono, correo, direccion
                FROM clientes
                ORDER BY id_cliente DESC
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {

                Cliente cliente = new Cliente();

                cliente.setIdCliente(resultado.getInt("id_cliente"));
                cliente.setNombre(resultado.getString("nombre"));
                cliente.setDocumento(resultado.getString("documento"));
                cliente.setTelefono(resultado.getString("telefono"));
                cliente.setCorreo(resultado.getString("correo"));
                cliente.setDireccion(resultado.getString("direccion"));

                clientes.add(cliente);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }

        return clientes;
    }

    /**
     * Busca un cliente por su ID.
     */
    public Cliente buscarPorId(int idCliente) {

        String sql = """
                SELECT id_cliente, nombre, documento,
                       telefono, correo, direccion
                FROM clientes
                WHERE id_cliente = ?
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idCliente);

            try (ResultSet resultado = sentencia.executeQuery()) {

                if (resultado.next()) {

                    Cliente cliente = new Cliente();

                    cliente.setIdCliente(resultado.getInt("id_cliente"));
                    cliente.setNombre(resultado.getString("nombre"));
                    cliente.setDocumento(resultado.getString("documento"));
                    cliente.setTelefono(resultado.getString("telefono"));
                    cliente.setCorreo(resultado.getString("correo"));
                    cliente.setDireccion(resultado.getString("direccion"));

                    return cliente;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
        }

        return null;
    }

    /**
     * Actualiza un cliente existente.
     */
    public String actualizar(Cliente cliente) {

        /*
         * Validamos primero para evitar que un cliente
         * utilice el documento, teléfono o correo de otro.
         */
        if (existeDocumento(cliente.getDocumento(), cliente.getIdCliente())) {
            return "DOCUMENTO_DUPLICADO";
        }

        if (cliente.getTelefono() != null
                && !cliente.getTelefono().trim().isEmpty()
                && existeTelefono(cliente.getTelefono(), cliente.getIdCliente())) {

            return "TELEFONO_DUPLICADO";
        }

        if (cliente.getCorreo() != null
                && !cliente.getCorreo().trim().isEmpty()
                && existeCorreo(cliente.getCorreo(), cliente.getIdCliente())) {

            return "CORREO_DUPLICADO";
        }

        String sql = """
                UPDATE clientes
                SET nombre = ?,
                    documento = ?,
                    telefono = ?,
                    correo = ?,
                    direccion = ?
                WHERE id_cliente = ?
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, cliente.getNombre());
            sentencia.setString(2, cliente.getDocumento());
            sentencia.setString(3, cliente.getTelefono());
            sentencia.setString(4, cliente.getCorreo());
            sentencia.setString(5, cliente.getDireccion());
            sentencia.setInt(6, cliente.getIdCliente());

            return sentencia.executeUpdate() > 0
                    ? "OK"
                    : "ERROR";

        } catch (SQLException e) {

            if (esDuplicado(e)) {
                return identificarDuplicado(cliente);
            }

            System.err.println("Error al actualizar cliente: " + e.getMessage());
            return "ERROR";
        }
    }

    /**
     * Detecta errores de restricción UNIQUE.
     */
    private boolean esDuplicado(SQLException e) {

        /*
         * MariaDB/MySQL utiliza normalmente el código 1062
         * para entradas duplicadas.
         */
        return e.getErrorCode() == 1062;
    }

    /**
     * Elimina un cliente.
     */
    public boolean eliminar(int idCliente) {

        String sql = """
                DELETE FROM clientes
                WHERE id_cliente = ?
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idCliente);

            return sentencia.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println("Error al eliminar cliente: " + e.getMessage());

            return false;
        }
    }
}